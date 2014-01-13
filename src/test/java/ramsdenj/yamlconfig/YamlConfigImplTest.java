package ramsdenj.yamlconfig;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

import org.junit.Test;
import ramsdenj.yamlconfig.model.ConfigurationInstance;

public class YamlConfigImplTest {
    
    public static class TestPojo {
        private int property;
        
        public TestPojo() { }
        
        public TestPojo(int property) {
            this.property = property;
        }
        
        public int getProperty() {
            return property;
        }
        public void setProperty(int property) {
            this.property = property;
        }
    }
    
    private static final String namespace = "mycompany.group.application.prod.us-east-1";
    
    @Test
    public void shouldReadStringSetting() throws Exception {
        Map<String, Object> keys = new HashMap<String, Object>();
        keys.put("k1", "v1");
        ConfigurationInstance instance = new ConfigurationInstance().withNamespace(namespace).withKeys(keys);
        
        ConfigurationInstanceProvider provider = createLocalProvider(instance);
        YamlConfig config = new YamlConfigImpl(namespace, provider);
        config.refresh();
        
        assertEquals("v1", config.getSetting("k1"));
        assertEquals("v1", config.getSetting("k1", String.class));
    }
    
    @Test
    public void shouldReadNonString() throws Exception {
        Map<String, Object> keys = new HashMap<String, Object>();
        keys.put("k1", 1);
        ConfigurationInstance instance = new ConfigurationInstance().withNamespace(namespace).withKeys(keys);
        
        ConfigurationInstanceProvider provider = createLocalProvider(instance);
        YamlConfig config = new YamlConfigImpl(namespace, provider);
        config.refresh();
        
        assertEquals(1, (int)config.getSetting("k1", Integer.class));
    }
    
    @Test
    public void shouldReadPojoSetting() throws Exception {
        int pojoPropertyValue = 2;
        Map<String, Object> keys = new HashMap<String, Object>();
        Map<String, Object> keysl2 = new HashMap<String, Object>();
        keys.put("k1", keysl2);
        keysl2.put("property", pojoPropertyValue);
        
        ConfigurationInstance instance = new ConfigurationInstance().withNamespace(namespace).withKeys(keys);
        ConfigurationInstanceProvider provider = createLocalProvider(instance);
        YamlConfig config = new YamlConfigImpl(namespace, provider);
        config.refresh();
        
        TestPojo pojo = config.getSetting("k1", TestPojo.class);
        assertNotNull(pojo);
        assertEquals(pojoPropertyValue, pojo.getProperty());
    }
    
    @Test
    public void shouldReadNestedSetting() throws Exception {
        Map<String, Object> keys = new HashMap<String, Object>();
        Map<String, Object> keysl2 = new HashMap<String, Object>();
        keysl2.put("kl2", "value");
        keys.put("kl1", keysl2);
        
        ConfigurationInstance instance = new ConfigurationInstance().withNamespace(namespace).withKeys(keys);
        ConfigurationInstanceProvider provider = createLocalProvider(instance);
        YamlConfig config = new YamlConfigImpl(namespace, provider);
        config.refresh();
        
        assertEquals("value", config.getSetting("kl1->kl2"));
    }
    
    private ConfigurationInstanceProvider createLocalProvider(final ConfigurationInstance ... configurationInstances) {
        return new ConfigurationInstanceProvider() {
            
            public List<ConfigurationInstance> loadConfigurations() throws IOException {
                return Arrays.asList(configurationInstances);
            }
        };
    }
}
