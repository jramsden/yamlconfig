package ramsdenj.yamlconfig;

import ramsdenj.yamlconfig.model.ConfigurationKeyNotFoundException;
import ramsdenj.yamlconfig.model.ConfigurationValueConversionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;

public class YamlConfigAWSCredentialsProvider implements AWSCredentialsProvider {

    private static final Logger LOG = LoggerFactory.getLogger(YamlConfigAWSCredentialsProvider.class);
    
    private YamlConfig yamlConfig;
    private volatile AWSCredentials credentials;
    
    public YamlConfigAWSCredentialsProvider(YamlConfig yamlConfig) {
        this.yamlConfig = yamlConfig;
        refresh();
    }
    
    public AWSCredentials getCredentials() {
        return credentials;
    }

    public void refresh() {
        try {
            credentials = new BasicAWSCredentials(
                    yamlConfig.getSetting("aws_credentials_config->access_key"),
                    yamlConfig.getSetting("aws_credentials_config->secret_key"));
        } catch (ConfigurationKeyNotFoundException e) {
            LOG.error("Error loading credentials from config.", e);
            credentials = null;
        } catch (ConfigurationValueConversionException e) {
            LOG.error("Error converting configuration type from config.");
            credentials = null;
        }
    }
}
