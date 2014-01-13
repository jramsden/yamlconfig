package ramsdenj.yamlconfig.model;

import java.util.Map;

public class ConfigurationInstance {
    
    private String namespace;
    private Map<String, Object> keys;
    
    public ConfigurationInstance() { }
    
    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }
    
    public ConfigurationInstance withNamespace(String namespace) {
        this.namespace = namespace;
        return this;
    }
    
    public Map<String, Object> getKeys() {
        return keys;
    }
    
    public void setKeys(Map<String, Object> keys) {
        this.keys = keys;
    }
    
    public ConfigurationInstance withKeys(Map<String, Object> keys) {
        this.keys = keys;
        return this;
    }

    @Override
    public String toString() {
        return "ConfigurationInstance [namespace=" + namespace
                + ", keys=" + keys + "]";
    }
}
