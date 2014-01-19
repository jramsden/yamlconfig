package ramsdenj.yamlconfig.providers;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import ramsdenj.yamlconfig.model.ConfigurationInstance;

import static org.junit.Assert.*;

public class StringConfigurationInstanceProviderTest {

    @Test
    public void shouldLoadConfigurationInstance() throws IOException {
        String configStr = "{ configurations: [ { namespace: ramsdenj, keys: { key: value } } ] }";
        StringConfigurationInstanceProvider provider = new StringConfigurationInstanceProvider(configStr);
        
        // Ensure there is only a single configuration instance returned.
        List<ConfigurationInstance> instances = provider.loadConfigurations();
        assertEquals(1, instances.size());
        
        // Do basic structural validations.
        ConfigurationInstance instance = instances.get(0);
        assertNotNull(instance.getNamespace());
        assertNotNull(instance.getKeys());
        assertEquals(1, instance.getKeys().size());
        
        // Check namespace is correct.
        assertEquals("ramsdenj", instance.getNamespace());
        
        // Check key is correct.
        assertEquals("value", instance.getKeys().get("key"));
    }
    
    @Test
    public void shouldLoadConfigurationInstances() throws IOException {
        String configStr = "{ configurations: [ { namespace: ramsdenj, keys: { key: value } }, { namespace: ramsdenj, keys: { key: value } } ] }";
        StringConfigurationInstanceProvider provider = new StringConfigurationInstanceProvider(configStr);
        
        // Ensure two configuration instances returned.
        List<ConfigurationInstance> instances = provider.loadConfigurations();
        assertEquals(2, instances.size());
        
        for (ConfigurationInstance instance : instances) {
            // Do basic structural validations.
            assertNotNull(instance.getNamespace());
            assertNotNull(instance.getKeys());
            assertEquals(1, instance.getKeys().size());
            
            // Check namespace is correct.
            assertEquals("ramsdenj", instance.getNamespace());
            
            // Check key is correct.
            assertEquals("value", instance.getKeys().get("key"));
        }
    }
}
