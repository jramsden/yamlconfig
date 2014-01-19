package ramsdenj.yamlconfig;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import ramsdenj.yamlconfig.model.ConfigurationKeyNotFoundException;
import ramsdenj.yamlconfig.model.ConfigurationValueConversionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

public class YamlConfigImpl implements YamlConfig {

    private static final Logger LOG = LoggerFactory.getLogger(YamlConfigImpl.class);
    private static final String PATH_SEPARATOR = "->";
    
    private ConfigurationInstanceProvider configurationInstanceProvider;
    private ConfigurationCompactor configurationCompactor;
    
    private Map<String, Object> keys;
    private ReadWriteLock lock;
    
    public YamlConfigImpl(String namespace, ConfigurationInstanceProvider configurationInstanceProvider) throws IOException {
        this.configurationInstanceProvider = configurationInstanceProvider;
        this.configurationCompactor = new ConfigurationCompactor(namespace);
        this.lock = new ReentrantReadWriteLock();
        
        refresh();
    }

    public String getSetting(String path) throws ConfigurationKeyNotFoundException, ConfigurationValueConversionException {
        return getSetting(path, String.class);
    }
    
    @SuppressWarnings("unchecked")
    public <T> T getSetting(String path, Class<T> clazz) throws ConfigurationKeyNotFoundException, ConfigurationValueConversionException {
        
        Object configValue = null;
        lock.readLock().lock();
        try {
            configValue = keys;
            String[] tokens = path.split(PATH_SEPARATOR);
            
            for (int i = 0; i < tokens.length; i++) {
                String token = tokens[i];
                if (!(configValue instanceof Map)) {
                    throw new ConfigurationKeyNotFoundException("Configuration key not found: " + path);
                }
                
                Map<String, Object> configMapping = (Map<String, Object>)configValue;
                if (!configMapping.containsKey(token)) {
                    throw new ConfigurationKeyNotFoundException("Configuration key not found: " + path);
                }
                configValue = configMapping.get(token);
        }
        } finally {
            lock.readLock().unlock();
        }
        
        try {
            if (clazz.isInstance(configValue)) {
                return clazz.cast(configValue);
            } else {
                Yaml yaml = new Yaml(new Constructor(clazz));
                return clazz.cast(yaml.load(yaml.dump(configValue)));
            }
        } catch (Exception e) {
            throw new ConfigurationValueConversionException("Failed to convert configuration value to correct type.", e);
        }
    }
    
    public void refresh() throws IOException {
        LOG.info("Beginning configuration refresh.");
        
        // Flatten configuration.
        Map<String, Object> configuration = configurationCompactor.compact(configurationInstanceProvider);
        
        // Replace configuration.
        lock.writeLock().lock();
        try {
            keys = configuration;
        } finally {
            lock.writeLock().unlock();
        }
        LOG.info("Configuration successfully refreshed.");
    }
}
