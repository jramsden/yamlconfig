package ramsdenj.yamlconfig.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import ramsdenj.yamlconfig.ConfigurationInstanceProvider;

public class ConfigurationCompactor {
    
    private String namespace;
    
    public ConfigurationCompactor(String namespace) {
        if (namespace == null || namespace == "" ||namespace.startsWith(".") || namespace.endsWith(".")) {
            throw new IllegalArgumentException("Invalid namespace: " + namespace);
        }
        
        this.namespace = namespace;
    }
    
    public Map<String, Object> compact(ConfigurationInstanceProvider configurationInstanceProvider) throws IOException {
        
        // Load all pertinent configuration instances.
        SortedMap<String, List<ConfigurationInstance>> configuration = new TreeMap<String, List<ConfigurationInstance>>(); 
        for (ConfigurationInstance configurationInstance : configurationInstanceProvider.loadConfigurations()) {
            if (!namespace.startsWith(configurationInstance.getNamespace())) {
                // Configuration not included in this namespace.
                continue;
            } else if (!namespace.equals(configurationInstance.getNamespace()) && namespace.charAt(configurationInstance.getNamespace().length()) != '.') {
                // Namespaces don't match unless we have a whole match, or the
                // first non-matching character is a period.
                continue;
            }
            
            List<ConfigurationInstance> nsConfiguration = configuration.get(configurationInstance.getNamespace());
            if (null == nsConfiguration) {
                nsConfiguration = new ArrayList<ConfigurationInstance>();
                configuration.put(configurationInstance.getNamespace(), nsConfiguration);
            }
            
            nsConfiguration.add(configurationInstance);
        }
        
        // Compact configuration settings into a single map.
        Map<String, Object> keySet = new HashMap<String, Object>();
        for (Map.Entry<String, List<ConfigurationInstance>> entry : configuration.entrySet()) {
            for (ConfigurationInstance instance : entry.getValue()) {
                keySet.putAll(instance.getKeys());
            }
        }
        
        return keySet;
    }

    public String getNamespace() {
        return namespace;
    }
}
