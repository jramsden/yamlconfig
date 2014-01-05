package ramsdenj.yamlconfig.model;

import java.util.Map;

public class ConfigurationInstance {
    
    private ConfigurationSettings configurationSettings;
    private Map<String, Object> keys;
    
    public ConfigurationInstance() { }
    
    public ConfigurationSettings getConfigurationSettings() {
        return configurationSettings;
    }

    public void setConfigurationSettings(ConfigurationSettings configurationSettings) {
        this.configurationSettings = configurationSettings;
    }
    
    public ConfigurationInstance withConfigurationSettings(ConfigurationSettings configurationSettings) {
        this.configurationSettings = configurationSettings;
        return this;
    }
    
    public Map<String, Object> getKeys() {
        return keys;
    }
    
    public void setKeys(Map<String, Object> keys) {
        this.keys = keys;
    }
    
    public ConfigurationInstance withKeys(Map<String, Object> keys) {
        if (this.keys == null) {
            this.keys = keys;
        } else {
            this.keys.putAll(keys);
        }
        return this;
    }

    @Override
    public String toString() {
        return "ConfigurationInstance [configurationSettings=" + configurationSettings
                + ", keys=" + keys + "]";
    }
}
