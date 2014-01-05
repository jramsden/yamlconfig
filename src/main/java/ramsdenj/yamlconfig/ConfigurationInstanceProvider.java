package ramsdenj.yamlconfig;

import java.io.IOException;
import java.util.List;

import ramsdenj.yamlconfig.model.ConfigurationInstance;

public interface ConfigurationInstanceProvider {

    List<ConfigurationInstance> loadConfigurations() throws IOException;
}
