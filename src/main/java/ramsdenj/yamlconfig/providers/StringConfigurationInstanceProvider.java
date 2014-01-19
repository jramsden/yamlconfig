package ramsdenj.yamlconfig.providers;

import java.io.IOException;
import java.util.List;

import org.yaml.snakeyaml.Yaml;

import ramsdenj.yamlconfig.ConfigurationInstanceProvider;
import ramsdenj.yamlconfig.model.ConfigurationInstance;
import ramsdenj.yamlconfig.model.ConfigurationStream;

public class StringConfigurationInstanceProvider implements ConfigurationInstanceProvider {

    private String configuration;
    
    public StringConfigurationInstanceProvider(String configuration) {
        this.configuration = configuration;
    }
    
    @Override
    public List<ConfigurationInstance> loadConfigurations() throws IOException {
        Yaml yaml = new Yaml();
        return yaml.loadAs(configuration, ConfigurationStream.class).getConfigurations();
    }
}
