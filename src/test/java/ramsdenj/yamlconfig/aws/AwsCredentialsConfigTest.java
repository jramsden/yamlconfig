package ramsdenj.yamlconfig.aws;

import org.junit.Test;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import static org.junit.Assert.*;

public class AwsCredentialsConfigTest {

    @Test
    public void shouldDeserializeConfiguration() {
        String configuration = "{ credentialsSource: CONFIGURATION, accessKey: myaccesskey, secretKey: mysecretkey }";
        AwsCredentialsConfig config = loadCredentialsConfiguration(configuration);
        
        assertNotNull(config);
        assertEquals(AwsCredentialsConfig.CredentialsSource.CONFIGURATION, config.getCredentialsSource());
        assertEquals("myaccesskey", config.getAccessKey());
        assertEquals("mysecretkey", config.getSecretKey());
    }
    
    @Test
    public void shouldDeserializePropertiesFile() {
        String configuration = "{ credentialsSource: PROPERTIES_FILE }";
        AwsCredentialsConfig config = loadCredentialsConfiguration(configuration);
        
        assertNotNull(config);
        assertEquals(AwsCredentialsConfig.CredentialsSource.PROPERTIES_FILE, config.getCredentialsSource());
        assertNull(config.getAccessKey());
        assertNull(config.getSecretKey());
    }
    
    @Test
    public void shouldDeserializeEnvironment() {
        String configuration = "{ credentialsSource: ENVIRONMENT }";
        AwsCredentialsConfig config = loadCredentialsConfiguration(configuration);
        
        assertNotNull(config);
        assertEquals(AwsCredentialsConfig.CredentialsSource.ENVIRONMENT, config.getCredentialsSource());
        assertNull(config.getAccessKey());
        assertNull(config.getSecretKey());
    }
    
    @Test
    public void shouldDeserializeInstanceProfile() {
        String configuration = "{ credentialsSource: INSTANCE_PROFILE }";
        AwsCredentialsConfig config = loadCredentialsConfiguration(configuration);
        
        assertNotNull(config);
        assertEquals(AwsCredentialsConfig.CredentialsSource.INSTANCE_PROFILE, config.getCredentialsSource());
        assertNull(config.getAccessKey());
        assertNull(config.getSecretKey());
    }
    
    private AwsCredentialsConfig loadCredentialsConfiguration(String config) {
        Constructor ctor = new Constructor(AwsCredentialsConfig.class);
        Yaml yaml = new Yaml(ctor);
        return (AwsCredentialsConfig)yaml.load(config);
    }
}
