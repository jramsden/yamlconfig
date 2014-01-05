package ramsdenj.yamlconfig.model;

public class ConfigurationValueConversionException extends Exception {

    private static final long serialVersionUID = 8629301891130167597L;

    public ConfigurationValueConversionException() {
        super();
    }

    public ConfigurationValueConversionException(String message) {
        super(message);
    }
    
    public ConfigurationValueConversionException(String message, Throwable cause) {
        super(message, cause);
    }
}
