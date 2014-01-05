package ramsdenj.yamlconfig;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import ramsdenj.yamlconfig.model.ConfigurationInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

public class ConfigurationInstanceProviderImpl implements ConfigurationInstanceProvider {

    private static final Logger LOG = LoggerFactory.getLogger(ConfigurationInstanceProviderImpl.class);
    
    private String configurationRoot;
    
    public ConfigurationInstanceProviderImpl(String configurationRoot) {
        this.configurationRoot = configurationRoot;
    }
    
    public List<ConfigurationInstance> loadConfigurations() throws IOException {
        File root = new File(configurationRoot);
        
        LOG.info("Reading configuration files: root={}", root.getAbsolutePath());
        List<ConfigurationInstance> instances = new ArrayList<ConfigurationInstance>();
        for (File configFile : root.listFiles()) {
            LOG.info("Reading configuration from file: file={}", configFile.getName());
            instances.add(loadConfigurationInstanceFromFile(configFile));
        }
        
        return instances;
    }

    private ConfigurationInstance loadConfigurationInstanceFromFile(File configFile) throws IOException {
        InputStream is = new BufferedInputStream(new FileInputStream(configFile));
        Yaml yaml = new Yaml(new Constructor(ConfigurationInstance.class));
        return (ConfigurationInstance)yaml.load(is);
    }
}
