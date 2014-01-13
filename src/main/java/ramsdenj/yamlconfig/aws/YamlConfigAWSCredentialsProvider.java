package ramsdenj.yamlconfig.aws;

import ramsdenj.yamlconfig.YamlConfig;
import ramsdenj.yamlconfig.model.ConfigurationKeyNotFoundException;
import ramsdenj.yamlconfig.model.ConfigurationValueConversionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;

public class YamlConfigAWSCredentialsProvider implements AWSCredentialsProvider {

    private static final Logger LOG = LoggerFactory.getLogger(YamlConfigAWSCredentialsProvider.class);
    
    private YamlConfig yamlConfig;
    private String credentialsConfigKey;
    
    private volatile AWSCredentialsProvider provider;
    
    public YamlConfigAWSCredentialsProvider(YamlConfig yamlConfig, String credentialsConfigKey) {
        this.yamlConfig = yamlConfig;
        this.credentialsConfigKey = credentialsConfigKey;
        refresh();
    }
    
    public AWSCredentials getCredentials() {
        return provider.getCredentials();
    }

    public void refresh() {
        
        AwsCredentialsConfig credentialsConfig = null;
        try {
            credentialsConfig = yamlConfig.getSetting(credentialsConfigKey, AwsCredentialsConfig.class);
        } catch (ConfigurationKeyNotFoundException e) {
            LOG.error("Error loading credentials from config.", e);
            provider = null;
        } catch (ConfigurationValueConversionException e) {
            LOG.error("Error converting configuration type from config.");
            provider = null;
        }
        
        AWSCredentialsProvider tmpProvider = null;
        switch (credentialsConfig.getCredentialsSource()) {
        case ENVIRONMENT:
            tmpProvider = new EnvironmentVariableCredentialsProvider();
            break;
        case PROPERTIES_FILE:
            tmpProvider = new ClasspathPropertiesFileCredentialsProvider();
            break;
        case INSTANCE_PROFILE:
            tmpProvider = new InstanceProfileCredentialsProvider();
            break;
        case CONFIGURATION:
            final String accessKey = credentialsConfig.getAccessKey();
            final String secretKey = credentialsConfig.getSecretKey();
            tmpProvider = new AWSCredentialsProvider() {
                
                @Override
                public void refresh() { }
                
                @Override
                public AWSCredentials getCredentials() {
                    return new BasicAWSCredentials(accessKey, secretKey);
                }
            };
            break;
        default:
            break;
        }
        
        provider = tmpProvider;
    }
}
