package ramsdenj.yamlconfig.providers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import ramsdenj.yamlconfig.ConfigurationInstanceProvider;
import ramsdenj.yamlconfig.model.ConfigurationStream;
import ramsdenj.yamlconfig.model.ConfigurationInstance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DirectoryConfigurationInstanceProvider implements ConfigurationInstanceProvider {

    private static final Logger LOG = LoggerFactory.getLogger(DirectoryConfigurationInstanceProvider.class);
    
    private File configurationDirectory;
    
    public DirectoryConfigurationInstanceProvider(File configurationDirectory) {
        this.configurationDirectory = configurationDirectory;
    }
    
    public List<ConfigurationInstance> loadConfigurations() throws IOException {
        
        LOG.info("Reading configuration files: root={}", configurationDirectory.getAbsolutePath());
        List<ConfigurationInstance> instances = new ArrayList<ConfigurationInstance>();
        for (File configFile : configurationDirectory.listFiles()) {
            LOG.info("Reading configuration from file: file={}", configFile.getName());
            InputStream inputStream = new BufferedInputStream(new FileInputStream(configFile));
            instances.addAll(ConfigurationStream.deserialize(inputStream).getConfigurations());
        }
        
        return instances;
    }
}
