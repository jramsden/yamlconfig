package ramsdenj.yamlconfig;

import org.junit.Test;
import ramsdenj.yamlconfig.model.ConfigurationInstance;
import ramsdenj.yamlconfig.model.ConfigurationScope;
import ramsdenj.yamlconfig.model.ConfigurationSettings;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import static org.junit.Assert.*;

public class ConfigurationInstanceTest {
    
    private static final String yamlConfig = "configurationSettings:\n  scope: APPLICATION\n  namespace: LocalTranscoderSample\n  environment: alpha\n  realm: us-east-1\nkeys:\n  key1: val1";
    
    @Test
    public void shouldLoadConfigurationInstance() {
        Yaml yaml = new Yaml(new Constructor(ConfigurationInstance.class));
        ConfigurationInstance instance = (ConfigurationInstance)yaml.load(yamlConfig);
        
        // Basic structural validation.
        assertNotNull(instance);
        assertNotNull(instance.getConfigurationSettings());
        assertNotNull(instance.getKeys());
        
        // Validate ConfigurationSettings.
        ConfigurationSettings settings = instance.getConfigurationSettings();
        assertEquals(ConfigurationScope.APPLICATION, settings.getScope());
        assertEquals("LocalTranscoderSample", settings.getNamespace());
        assertEquals("alpha", settings.getEnvironment());
        assertEquals("us-east-1", settings.getRealm());
        
        // Validate keys.
        assertEquals("val1", instance.getKeys().get("key1"));
    }
}
