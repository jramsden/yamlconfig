package ramsdenj.yamlconfig;

import java.io.IOException;

import ramsdenj.yamlconfig.model.ConfigurationKeyNotFoundException;
import ramsdenj.yamlconfig.model.ConfigurationValueConversionException;

public interface YamlConfig {
    
    public void refresh() throws IOException;
    public String getSetting(String path) throws ConfigurationKeyNotFoundException, ConfigurationValueConversionException;
    public <T> T getSetting(String path, Class<T> clazz) throws ConfigurationKeyNotFoundException, ConfigurationValueConversionException;
}
