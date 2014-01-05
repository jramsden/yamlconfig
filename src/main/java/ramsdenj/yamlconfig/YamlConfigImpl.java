package ramsdenj.yamlconfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import ramsdenj.yamlconfig.model.ConfigurationInstance;
import ramsdenj.yamlconfig.model.ConfigurationKeyNotFoundException;
import ramsdenj.yamlconfig.model.ConfigurationScope;
import ramsdenj.yamlconfig.model.ConfigurationSettings;
import ramsdenj.yamlconfig.model.ConfigurationValueConversionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

public class YamlConfigImpl implements YamlConfig {

    private static final Logger LOG = LoggerFactory.getLogger(YamlConfigImpl.class);
    private static final String PATH_SEPARATOR = "->";
    
    private String application;
    private String group;
    private String environment;
    private String realm;
    private ConfigurationInstanceProvider configurationInstanceProvider;
    
    private Map<String, Object> keys;
    private ReadWriteLock lock;
    
    public YamlConfigImpl(String realm, String environment, String group, String application, ConfigurationInstanceProvider configurationInstanceProvider) {
        this.realm = realm;
        this.environment = environment;
        this.group = group;
        this.application = application;
        this.configurationInstanceProvider = configurationInstanceProvider;
        this.lock = new ReentrantReadWriteLock();
    }

    public String getSetting(String path) throws ConfigurationKeyNotFoundException, ConfigurationValueConversionException {
        return getSetting(path, String.class);
    }
    
    @SuppressWarnings("unchecked")
    public <T> T getSetting(String path, Class<T> clazz) throws ConfigurationKeyNotFoundException, ConfigurationValueConversionException {
        
        Object configValue = keys;
        
        String[] tokens = path.split(PATH_SEPARATOR);
        
        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i];
            if (!(configValue instanceof Map)) {
                throw new ConfigurationKeyNotFoundException("Configuration key not found: " + path);
            }
            
            configValue = ((Map<String, Object>)configValue).get(token);
        }
        
        try {
            if (clazz.isInstance(configValue)) {
                LOG.info("Setting found is correct class");
                return clazz.cast(configValue);
            } else {
                LOG.info("Setting found is incorrect class.  Attempting to deserialize into correct class.");
                Yaml yaml = new Yaml(new Constructor(clazz));
                return clazz.cast(yaml.load(yaml.dump(configValue)));
            }
        } catch (Exception e) {
            throw new ConfigurationValueConversionException("Failed to convert configuration value to correct type.", e);
        }
    }
    
    public void refresh() throws IOException {
        LOG.info("Beginning configuration refresh.");
        
        // Read all configuration files from the config root.
        Map<ConfigurationScope, List<ConfigurationInstance>> allConfiguration = loadConfiguration();
        
        // Flatten configuration according to scope: global -> group -> application -> machine
        Map<String, Object> configuration = flattenConfiguration(allConfiguration);
        
        lock.writeLock().lock();
        try {
            keys = configuration;
        } finally {
            lock.writeLock().unlock();
        }
        LOG.info("Configuration successfully refreshed.");
    }
    
    private Map<ConfigurationScope, List<ConfigurationInstance>> loadConfiguration() throws IOException {
        Map<ConfigurationScope, List<ConfigurationInstance>> configuration = new HashMap<ConfigurationScope, List<ConfigurationInstance>>();
        for (ConfigurationScope scope : ConfigurationScope.values()) {
            configuration.put(scope, new ArrayList<ConfigurationInstance>());
        }
        
        for (ConfigurationInstance instance : configurationInstanceProvider.loadConfigurations()) {
            ConfigurationSettings configurationSettings = instance.getConfigurationSettings();
            if (configurationSettings == null) {
                LOG.info("Skipping configuration.  ConfigurationSettings not specified.");
                continue;
            } else if (!environment.equals(configurationSettings.getEnvironment())) {
                LOG.info("Skipping configuration.  Incorrect environment:  ConfigurationSettings->environment={}", configurationSettings.getEnvironment());
                continue;
            } else if (!realm.equals(configurationSettings.getRealm())) {
                LOG.info("Skipping configuration.  Incorrect realm: ConfigurationSettings->realm={}", configurationSettings.getRealm());
                continue;
            } else if (configurationSettings.getScope() == ConfigurationScope.GROUP &&
                    !group.equals(configurationSettings.getNamespace())) {
                LOG.info("Skipping configuration.  Incorrect group: ConfigurationSettings->group={}", configurationSettings.getNamespace());
                continue;
            } else if (configurationSettings.getScope() == ConfigurationScope.APPLICATION &&
                    !application.equals(configurationSettings.getNamespace())) {
                LOG.info("Skipping configuration.  Incorrect application: ConfigurationSettings->application={}", configurationSettings.getNamespace());
                continue;
            }
            
            configuration.get(instance.getConfigurationSettings().getScope()).add(instance);
        }
        
        return configuration;
    }
    
    private Map<String, Object> flattenConfiguration(Map<ConfigurationScope, List<ConfigurationInstance>> configuration) {
        Map<String, Object> flattened = new HashMap<String, Object>();
        for (ConfigurationInstance instance : configuration.get(ConfigurationScope.GLOBAL)) {
            flattened.putAll(instance.getKeys());
        }
        for (ConfigurationInstance instance : configuration.get(ConfigurationScope.GROUP)) {
            flattened.putAll(instance.getKeys());
        }
        for (ConfigurationInstance instance : configuration.get(ConfigurationScope.APPLICATION)) {
            flattened.putAll(instance.getKeys());
        }
        for (ConfigurationInstance instance : configuration.get(ConfigurationScope.MACHINE)) {
            flattened.putAll(instance.getKeys());
        }
        
        return flattened;
    }
}
