package com.sequenceiq.cloudbreak.conf;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Configuration;

import com.cedarsoftware.util.io.JsonReader;

@Configuration
public class JsonIoConfig {

    @PostConstruct
    public void setupJsonReader() {
        JsonReader.assignInstantiator("com.google.common.collect.RegularImmutableMap", new JsonReader.MapFactory());
        JsonReader.assignInstantiator("com.google.common.collect.EmptyImmutableBiMap", new JsonReader.MapFactory());
        JsonReader.assignInstantiator("java.util.Collections$EmptyMap", new JsonReader.MapFactory());
        JsonReader.assignInstantiator("java.util.Collections$SingletonMap", new JsonReader.MapFactory());

        JsonReader.assignInstantiator("com.google.common.collect.SingletonImmutableList", new JsonReader.CollectionFactory());
        JsonReader.assignInstantiator("com.google.common.collect.RegularImmutableList", new JsonReader.CollectionFactory());
        JsonReader.assignInstantiator("java.util.Collections$EmptyList", new JsonReader.CollectionFactory());
        JsonReader.assignInstantiator("java.util.Collections$SingletonList", new JsonReader.CollectionFactory());

        JsonReader.assignInstantiator("com.google.common.collect.RegularImmutableSet", new JsonReader.CollectionFactory());
        JsonReader.assignInstantiator("java.util.Collections$EmptySet", new JsonReader.CollectionFactory());
        JsonReader.assignInstantiator("java.util.Collections$SingletonSet", new JsonReader.CollectionFactory());
    }
}
