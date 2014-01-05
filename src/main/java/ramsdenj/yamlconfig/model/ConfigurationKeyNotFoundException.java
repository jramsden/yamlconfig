package ramsdenj.yamlconfig.model;

public class ConfigurationKeyNotFoundException extends Exception {

    private static final long serialVersionUID = 7006282580923832716L;
    
    public ConfigurationKeyNotFoundException() {
        super();
    }
    
    public ConfigurationKeyNotFoundException(String message) {
        super(message);
    }
    
    public ConfigurationKeyNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
