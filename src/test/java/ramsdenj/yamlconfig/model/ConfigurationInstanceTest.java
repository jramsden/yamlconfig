package ramsdenj.yamlconfig.model;

import org.junit.Test;
import ramsdenj.yamlconfig.model.ConfigurationInstance;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import static org.junit.Assert.*;

public class ConfigurationInstanceTest {
    
    private static final String NAMESPACE = "ramsdenj";
    private static final String KEY = "key";
    private static final String VALUE = "value";
    
    private static final String yamlConfig = "namespace: " + NAMESPACE + "\n" +
                                             "keys:\n" +
                                             "  " + KEY + ": " + VALUE + "\n";
    
    @Test
    public void shouldLoadConfigurationInstance() {
        Yaml yaml = new Yaml(new Constructor(ConfigurationInstance.class));
        ConfigurationInstance instance = (ConfigurationInstance)yaml.load(yamlConfig);
        
        // Basic structural validation.
        assertNotNull(instance);
        assertNotNull(instance.getNamespace());
        assertNotNull(instance.getKeys());
        
        // Validate configuration values.
        assertEquals(NAMESPACE, instance.getNamespace());
        assertEquals(VALUE, instance.getKeys().get(KEY));
    }
}
