package com.sequenceiq.cloudbreak.cloud.azure;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.microsoft.azure.CloudError;
import com.microsoft.azure.CloudException;
import com.microsoft.azure.management.resources.Deployment;
import com.microsoft.azure.management.resources.ResourceGroup;
import com.sequenceiq.cloudbreak.cloud.NetworkConnector;
import com.sequenceiq.cloudbreak.cloud.azure.client.AzureClient;
import com.sequenceiq.cloudbreak.cloud.azure.client.AzureClientService;
import com.sequenceiq.cloudbreak.cloud.exception.CloudConnectorException;
import com.sequenceiq.cloudbreak.cloud.model.CloudCredential;
import com.sequenceiq.cloudbreak.cloud.model.CreatedCloudNetwork;
import com.sequenceiq.cloudbreak.cloud.model.NetworkCreationRequest;
import com.sequenceiq.cloudbreak.cloud.model.Platform;
import com.sequenceiq.cloudbreak.cloud.model.Variant;

@Service
public class AzureNetworkConnector implements NetworkConnector {

    private static final Logger LOGGER = LoggerFactory.getLogger(AzureNetworkConnector.class);

    private static final String NETWORK_ID_KEY = "networkId";

    private static final String SUBNET_ID_KEY = "subnetId";

    @Inject
    private AzureClientService azureClientService;

    @Inject
    private AzureNetworkTemplateBuilder azureNetworkTemplateBuilder;

    @Override
    public CreatedCloudNetwork createNetworkWithSubnets(NetworkCreationRequest networkRequest) {
        AzureClient azureClient = azureClientService.getClient(networkRequest.getCloudCredential());
        String template = azureNetworkTemplateBuilder.build(networkRequest);
        String parameters = azureNetworkTemplateBuilder.buildParameters();
        String envName = networkRequest.getEnvName();
        String stackName = createStackName(envName);
        Deployment templateDeployment;
        try {
            Map<String, String> tags = Collections.emptyMap();
            Map<String, String> costFollowerTags = Collections.emptyMap();
            ResourceGroup resourceGroup = azureClient.createResourceGroup(envName, networkRequest.getRegion().value(), tags, costFollowerTags);
            templateDeployment = azureClient.createTemplateDeployment(resourceGroup.name(), stackName, template, parameters);
        } catch (CloudException e) {
            LOGGER.info("Provisioning error, cloud exception happened: ", e);
            if (e.body() != null && e.body().details() != null) {
                String details = e.body().details().stream().map(CloudError::message).collect(Collectors.joining(", "));
                throw new CloudConnectorException(String.format("Stack provisioning failed, status code %s, error message: %s, details: %s",
                        e.body().code(), e.body().message(), details));
            } else {
                throw new CloudConnectorException(String.format("Stack provisioning failed: '%s', please go to Azure Portal for detailed message", e));
            }
        } catch (Exception e) {
            LOGGER.warn("Provisioning error:", e);
            throw new CloudConnectorException(String.format("Error in provisioning stack %s: %s", stackName, e.getMessage()));
        }
        Map<String, Map> outputMap = (HashMap) templateDeployment.outputs();
        String networkName = (String) outputMap.get(NETWORK_ID_KEY).get("value");
        Set<String> subnetNames = createSubnetNames(networkRequest.getSubnetCidrs().size(), outputMap);

        return new CreatedCloudNetwork(networkName, subnetNames, createProperties(envName, stackName));
    }

    @Override
    public CreatedCloudNetwork deleteNetworkWithSubnets(CloudCredential cloudCredential) {
        return null;
    }

    @Override
    public Platform platform() {
        return AzureConstants.PLATFORM;
    }

    @Override
    public Variant variant() {
        return AzureConstants.VARIANT;
    }

    private String createStackName(String envName) {
        return String.join("-", envName, "network", "stack");
    }

    private Set<String> createSubnetNames(int numberOfSubnets, Map<String, Map> outputMap) {
        Set<String> subnetNames = new HashSet<>();
        for (int i = 0; i < numberOfSubnets; i++) {
            if (outputMap.containsKey(SUBNET_ID_KEY + i)) {
                subnetNames.add((String) outputMap.get(SUBNET_ID_KEY + i).get("value"));
            } else {
                throw new CloudConnectorException("Subnet could not be found in the Azure deployment output.");
            }
        }
        return subnetNames;
    }

    private Map<String, Object> createProperties(String resourceGroupName, String stackName) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("resourceGroupName", resourceGroupName);
        properties.put("stackName", stackName);
        return properties;
    }
}
