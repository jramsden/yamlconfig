package ramsdenj.yamlconfig;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

import org.easymock.EasyMockSupport;
import org.junit.Test;
import ramsdenj.yamlconfig.model.ConfigurationInstance;
import ramsdenj.yamlconfig.model.ConfigurationScope;
import ramsdenj.yamlconfig.model.ConfigurationSettings;

import static org.easymock.EasyMock.*;

public class YamlConfigImplTest extends EasyMockSupport {
    
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
    
    private static final String realm = "us-east-1";
    private static final String environment = "test";
    private static final String group = "group";
    private static final String application = "application";
    
    @Test
    public void shouldLoadGlobalConfiguration() throws Exception {
        
        Map<String, Object> keys = new HashMap<String, Object>();
        keys.put("k1", "v1");
        
        ConfigurationInstanceProvider provider = createMock(ConfigurationInstanceProvider.class);
        ConfigurationInstance globalConfig = createMock(ConfigurationInstance.class);
        ConfigurationSettings globalSettings = createMock(ConfigurationSettings.class);
        
        expect(provider.loadConfigurations()).andReturn(Arrays.asList(globalConfig));
        expect(globalConfig.getConfigurationSettings()).andReturn(globalSettings);
        expect(globalSettings.getEnvironment()).andReturn(environment);
        expect(globalSettings.getRealm()).andReturn(realm);
        expect(globalSettings.getScope()).andReturn(ConfigurationScope.GLOBAL).times(2);
        expect(globalConfig.getConfigurationSettings()).andReturn(globalSettings);
        expect(globalSettings.getScope()).andReturn(ConfigurationScope.GLOBAL);
        
        expect(globalConfig.getKeys()).andReturn(keys);
        
        replayAll();
        YamlConfig config = new YamlConfigImpl(realm, environment, group, application, provider);
        config.refresh();
        verifyAll();
        
        assertEquals("v1", config.getSetting("k1"));
    }
    
    @Test
    public void shouldReadStringSetting() throws Exception {
        ConfigurationSettings configurationSettings = new ConfigurationSettings(ConfigurationScope.GLOBAL, null, environment, realm);
        Map<String, Object> keys = new HashMap<String, Object>();
        keys.put("k1", "v1");
        ConfigurationInstance instance = new ConfigurationInstance()
            .withConfigurationSettings(configurationSettings)
            .withKeys(keys);
        
        ConfigurationInstanceProvider provider = createLocalProvider(instance);
        YamlConfig config = new YamlConfigImpl(realm, environment, group, application, provider);
        config.refresh();
        
        assertEquals("v1", config.getSetting("k1"));
        assertEquals("v1", config.getSetting("k1", String.class));
    }
    
    @Test
    public void shouldReadNonString() throws Exception {
        ConfigurationSettings configurationSettings = new ConfigurationSettings(ConfigurationScope.GLOBAL, null, environment, realm);
        Map<String, Object> keys = new HashMap<String, Object>();
        keys.put("k1", 1);
        ConfigurationInstance instance = new ConfigurationInstance()
            .withConfigurationSettings(configurationSettings)
            .withKeys(keys);
        
        ConfigurationInstanceProvider provider = createLocalProvider(instance);
        YamlConfig config = new YamlConfigImpl(realm, environment, group, application, provider);
        config.refresh();
        
        assertEquals(1, (int)config.getSetting("k1", Integer.class));
    }
    
    @Test
    public void shouldReadPojoSetting() throws Exception {
        int pojoPropertyValue = 2;
        ConfigurationSettings configurationSettings = new ConfigurationSettings(ConfigurationScope.GLOBAL, null, environment, realm);
        Map<String, Object> keys = new HashMap<String, Object>();
        Map<String, Object> keysl2 = new HashMap<String, Object>();
        keys.put("k1", keysl2);
        keysl2.put("property", pojoPropertyValue);
        
        ConfigurationInstance instance = new ConfigurationInstance().withConfigurationSettings(configurationSettings).withKeys(keys);
        ConfigurationInstanceProvider provider = createLocalProvider(instance);
        YamlConfig config = new YamlConfigImpl(realm, environment, group, application, provider);
        config.refresh();
        
        TestPojo pojo = config.getSetting("k1", TestPojo.class);
        assertNotNull(pojo);
        assertEquals(pojoPropertyValue, pojo.getProperty());
    }
    
    @Test
    public void shouldReadNestedSetting() throws Exception {
        ConfigurationSettings configurationSettings = new ConfigurationSettings(ConfigurationScope.GLOBAL, null, environment, realm);
        Map<String, Object> keys = new HashMap<String, Object>();
        Map<String, Object> keysl2 = new HashMap<String, Object>();
        keysl2.put("kl2", "value");
        keys.put("kl1", keysl2);
        
        ConfigurationInstance instance = new ConfigurationInstance().withConfigurationSettings(configurationSettings).withKeys(keys);
        ConfigurationInstanceProvider provider = createLocalProvider(instance);
        YamlConfig config = new YamlConfigImpl(realm, environment, group, application, provider);
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
