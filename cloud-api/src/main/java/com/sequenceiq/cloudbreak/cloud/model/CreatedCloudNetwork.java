package com.sequenceiq.cloudbreak.cloud.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CreatedCloudNetwork {

    private String networkId;

    private Set<String> subnetIds = new HashSet<>();

    private Map<String, Object> properties = new HashMap<>();

    public CreatedCloudNetwork() {
    }

    public CreatedCloudNetwork(String networkId, Set<String> subnetIds, Map<String, Object> properties) {
        this.networkId = networkId;
        this.subnetIds = subnetIds;
        this.properties = properties;
    }

    public CreatedCloudNetwork(String networkId, Set<String> subnetIds) {
        this.networkId = networkId;
        this.subnetIds = subnetIds;
    }

    public String getNetworkId() {
        return networkId;
    }

    public Set<String> getSubnetIds() {
        return subnetIds;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }
}
