package ramsdenj.yamlconfig.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.junit.Test;

import static org.junit.Assert.*;

public class ConfigurationStreamTest {

    @Test
    public void shouldDeserialize() throws IOException {
        String configuration = "{ configurations: [ { namespace: testNamespace, keys: { key: value } } ] }";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(configuration.getBytes("utf-8"));
        ConfigurationStream config = ConfigurationStream.deserialize(inputStream);
        
        assertNotNull(config);
        List<ConfigurationInstance> configurations = config.getConfigurations();
        assertNotNull(configurations);
        assertEquals(1, configurations.size());
        
        ConfigurationInstance instance = configurations.get(0);
        assertNotNull(instance);
        assertEquals("testNamespace", instance.getNamespace());
        assertEquals(1, instance.getKeys().size());
        assertEquals("value", instance.getKeys().get("key"));
    }
}
