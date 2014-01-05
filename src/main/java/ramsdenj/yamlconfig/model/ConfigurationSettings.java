package ramsdenj.yamlconfig.model;

public class ConfigurationSettings {

    private ConfigurationScope scope;
    private String namespace;
    private String environment;
    private String realm;
    
    public ConfigurationSettings() { }
    
    public ConfigurationSettings(ConfigurationScope scope, String namespace, String environment, String realm) {
        this.scope = scope;
        this.namespace = namespace;
        this.environment = environment;
        this.realm = realm;
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
    
    public String getRealm() {
        return realm;
    }
    
    public void setRealm(String realm) {
        this.realm = realm;
    }
    
    @Override
    public String toString() {
        return "ConfigSettings [scope=" + scope + ", namespace="
                + namespace + ", environment=" + environment + ", realm="
                + realm + "]";
    }
}
