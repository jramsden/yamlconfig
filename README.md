yamlconfig
==========

Java Application Configuration

Retrieving settings from configuration:

```yaml
configurations:
  -
    configuration:
      namespace: ramsdenj
      keys:
        sampleKey: sampleValue
        sampleKeyMap:
          nestedKeyOne: nestedValueOne
          nestedKeyTwo: nestedValueTwo
  -
    configuration:
      namespace: ramsdenj.myappgroup.myapp
      keys:
        sampleKey: sampleValueOverride
  -
    configuration:
      namespace: ramsdenj.myappgroup.myapp.dev.us-east-1
      keys:
        sampleKeyTwo: sampleValueTwo
```

```java
YamlConfig yamlConfig = new YamlConfigImpl("ramsdenj", configurationInstanceProvider);
yamlConfig.getSetting("sampleKey");                     // returns "sampleValue"
yamlConfig.getSetting("sampleKeyMap->nestedKeyOne")     // returns "nestedValueOne"
```

```java
YamlConfig yamlConfig = new YamlConfigImpl("ramsdenj.myappgroup.myapp", configurationInstanceProvider);
yamlConfig.getSetting("sampleKey");     // returns "sampleValueOverride"
yamlConfig.getSetting("sampleKeyTwo");  // throws ConfigurationKeyNotFoundException
```

You can also retrieve POJOs:
```
configurations:
  -
  configuration:
    namespace: ramsden.myapp
    keys:
      point:
        x: 1.0
        y: 2.2
```

```java
YamlConfig yamlConfig = new YamlConfigImpl("ramsdenj.myapp.dev");
yamlConfig.getSetting("point", Point.class);            // returns an instance of Point.
```

You should never define the same key in the same scope more than once.  If the
same key is defined more than once in the same scope, the key will be overwritten
so the last key encountered will be the one taken.  Since the order configuration
is read is not guaranteed, this will result in non-deterministic behavior.