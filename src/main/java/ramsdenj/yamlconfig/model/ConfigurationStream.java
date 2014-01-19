package ramsdenj.yamlconfig.model;

import java.io.InputStream;
import java.util.List;

import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

public class ConfigurationStream {

    private static volatile Yaml yaml = null;
    
    private List<ConfigurationInstance> configurations;
    
    public List<ConfigurationInstance> getConfigurations() {
        return configurations;
    }
    
    public void setConfigurations(List<ConfigurationInstance> configurations) {
        this.configurations = configurations;
    }
    
    public static ConfigurationStream deserialize(InputStream inputStream) {
        if (yaml == null) {
            synchronized (ConfigurationStream.class) {
                if (yaml == null) {
                    Constructor ctor = new Constructor(ConfigurationStream.class);
                    TypeDescription cfgsDescription = new TypeDescription(ConfigurationStream.class);
                    cfgsDescription.putListPropertyType("configurations", ConfigurationInstance.class);
                    ctor.addTypeDescription(cfgsDescription);
                    yaml = new Yaml(ctor);
                }
            }
        }
        
        return (ConfigurationStream) yaml.load(inputStream);
    }
}
