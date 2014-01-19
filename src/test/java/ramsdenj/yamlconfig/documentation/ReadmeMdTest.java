package ramsdenj.yamlconfig.documentation;

import java.io.IOException;

import org.junit.Test;

import ramsdenj.yamlconfig.YamlConfig;
import ramsdenj.yamlconfig.YamlConfigImpl;
import ramsdenj.yamlconfig.model.ConfigurationKeyNotFoundException;
import ramsdenj.yamlconfig.model.ConfigurationValueConversionException;
import ramsdenj.yamlconfig.providers.StringConfigurationInstanceProvider;
import static org.junit.Assert.*;

public class ReadmeMdTest {

    private static final String overrideSampleConfiguration =
            "configurations:\n" +
            "  -\n" +
            "    namespace: ramsdenj\n" +
            "    keys:\n" +
            "      sampleKey: sampleValue\n" +
            "      sampleKeyMap:\n" +
            "        nestedKeyOne: nestedValueOne\n" +
            "        nestedKeyTwo: nestedValueTwo\n" +
            "  -\n" +
            "    namespace: ramsdenj.myappgroup.myapp\n" +
            "    keys:\n" +
            "      sampleKey: sampleValueOverride\n" +
            "  -\n" +
            "    namespace: ramsdenj.myappgroup.myapp.dev.us-east-1\n" +
            "    keys:\n" +
            "      sampleKeyTwo: sampleValueTwo\n";
    
    @Test
    public void shouldLoadSampleValue() throws ConfigurationKeyNotFoundException, ConfigurationValueConversionException, IOException {
        YamlConfig yamlConfig = new YamlConfigImpl("ramsdenj", new StringConfigurationInstanceProvider(overrideSampleConfiguration));
        assertEquals("sampleValue", yamlConfig.getSetting("sampleKey"));
        assertEquals("nestedValueOne", yamlConfig.getSetting("sampleKeyMap->nestedKeyOne"));
    }
    
    @Test
    public void shouldOverrideKey() throws IOException, ConfigurationKeyNotFoundException, ConfigurationValueConversionException {
        YamlConfig yamlConfig = new YamlConfigImpl("ramsdenj.myappgroup.myapp", new StringConfigurationInstanceProvider(overrideSampleConfiguration));
        assertEquals("sampleValueOverride", yamlConfig.getSetting("sampleKey"));
    }
    
    @Test(expected=ConfigurationKeyNotFoundException.class)
    public void shouldNotApplySubconfiguration() throws IOException, ConfigurationKeyNotFoundException, ConfigurationValueConversionException {
        YamlConfig yamlConfig = new YamlConfigImpl("ramsdenj.myappgroup.myapp", new StringConfigurationInstanceProvider(overrideSampleConfiguration));
        yamlConfig.getSetting("sampleKeyTwo");
    }
    
    private static class Point {
        public double x;
        public double y;
    }
    
    private static final String pojoDeserializationSampleConfiguration = 
            "configurations:\n" +
            "  -\n" +
            "    namespace: ramsdenj\n" + 
            "    keys:\n" +
            "      point:\n" +
            "        x: 1.0\n" +
            "        y: 2.2\n";
    
    @Test
    public void shouldDeserializePojo() throws IOException, ConfigurationKeyNotFoundException, ConfigurationValueConversionException {
        YamlConfig yamlConfig = new YamlConfigImpl("ramsdenj.myappgroup.myapp.dev", new StringConfigurationInstanceProvider(pojoDeserializationSampleConfiguration));
        Point point = yamlConfig.getSetting("point", Point.class);
        
        assertNotNull(point);
        assertEquals(1.0, point.x, 0);
        assertEquals(2.2, point.y, 0);
    }
}
