package ramsdenj.yamlconfig.aws;

public class AwsCredentialsConfig {

    public static enum CredentialsSource {
        CONFIGURATION,
        PROPERTIES_FILE,
        ENVIRONMENT,
        INSTANCE_PROFILE;
    }
    
    private CredentialsSource credentialsSource;
    private String accessKey;
    private String secretKey;
    
    public AwsCredentialsConfig() { }
    
    public AwsCredentialsConfig(CredentialsSource credentialsSource, String accessKey, String secretKey) {
        this.credentialsSource = credentialsSource;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    public CredentialsSource getCredentialsSource() {
        return credentialsSource;
    }

    public void setCredentialsSource(CredentialsSource credentialsSource) {
        this.credentialsSource = credentialsSource;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
