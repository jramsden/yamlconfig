package ramsdenj.yamlconfig.model;

public class ConfigurationSettings {

    private ConfigurationScope scope;
    private String namespace;
    private String environment;
    private String region;
    
    public ConfigurationSettings() { }
    
    public ConfigurationSettings(ConfigurationScope scope, String namespace, String environment, String region) {
        this.scope = scope;
        this.namespace = namespace;
        this.environment = environment;
        this.region = region;
    }

    public ConfigurationScope getScope() {
        return scope;
    }
    
    public void setScope(ConfigurationScope scope) {
        this.scope = scope;
    }
    
    public String getNamespace() {
        return namespace;
    }
    
    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }
    
    public String getEnvironment() {
        return environment;
    }
    
    public void setEnvironment(String environment) {
        this.environment = environment;
    }
    
    public String getRegion() {
        return region;
    }
    
    public void setRegion(String region) {
        this.region = region;
    }
    
    @Override
    public String toString() {
        return "ConfigSettings [scope=" + scope + ", namespace="
                + namespace + ", environment=" + environment + ", region="
                + region + "]";
    }
}
