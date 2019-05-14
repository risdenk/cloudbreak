package com.sequenceiq.cloudbreak.controller.validation.environment.network;

import static com.sequenceiq.cloudbreak.common.mappable.CloudPlatform.AZURE;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.sequenceiq.cloudbreak.api.endpoint.v4.environment.requests.EnvironmentNetworkV4Request;
import com.sequenceiq.cloudbreak.common.mappable.CloudPlatform;
import com.sequenceiq.cloudbreak.controller.validation.ValidationResult;

@Component
public class AzureEnvironmentNetworkValidator implements EnvironmentNetworkValidator {

    @Inject
    private NetworkCidrValidator networkCidrValidator;

    @Override
    public void validate(EnvironmentNetworkV4Request networkV4Request, ValidationResult.ValidationResultBuilder resultBuilder) {
        if (networkV4Request != null) {
            validateSubnetCidrs(networkV4Request, resultBuilder);
            validateNetworkCidr(networkV4Request, resultBuilder);
            validateNetworkId(networkV4Request, resultBuilder);
            validateSubnetIds(networkV4Request, resultBuilder);
            validateResourceGroup(networkV4Request, resultBuilder);
            networkCidrValidator.validate(networkV4Request, resultBuilder);
        } else {
            resultBuilder.error(missingParamsErrorMsg());
        }
    }

    @Override
    public CloudPlatform getCloudPlatform() {
        return AZURE;
    }

    private void validateSubnetCidrs(EnvironmentNetworkV4Request networkV4Request, ValidationResult.ValidationResultBuilder resultBuilder) {
        if (missingExistingNetwork(networkV4Request) && missingSubnetCidrs(networkV4Request)) {
            resultBuilder.error(missingParamErrorMessage("subnetCidrs"));
        }
    }

    private void validateNetworkCidr(EnvironmentNetworkV4Request networkV4Request, ValidationResult.ValidationResultBuilder resultBuilder) {
        if (missingExistingNetwork(networkV4Request) && missingNetworkCidr(networkV4Request)) {
            resultBuilder.error(missingParamErrorMessage("networkCidr"));
        }
    }

    private void validateNetworkId(EnvironmentNetworkV4Request networkV4Request, ValidationResult.ValidationResultBuilder resultBuilder) {
        if (missingNewNetworkData(networkV4Request) && missingNetworkId(networkV4Request)) {
            resultBuilder.error(missingParamErrorMessage("networkId"));
        }
    }

    private void validateSubnetIds(EnvironmentNetworkV4Request networkV4Request, ValidationResult.ValidationResultBuilder resultBuilder) {
        if (missingNewNetworkData(networkV4Request) && missingSubnetIds(networkV4Request)) {
            resultBuilder.error(missingParamErrorMessage("subnet identifier(subnetIds)'"));
        }
    }

    private void validateResourceGroup(EnvironmentNetworkV4Request networkV4Request, ValidationResult.ValidationResultBuilder resultBuilder) {
        if (missingNewNetworkData(networkV4Request) && !hasResourceGroup(networkV4Request)) {
            resultBuilder.error(missingParamErrorMessage("resourceGroupName"));
        }
    }

    private boolean missingExistingNetwork(EnvironmentNetworkV4Request networkV4Request) {
        return missingSubnetIds(networkV4Request) || missingNetworkId(networkV4Request);
    }

    private boolean missingSubnetIds(EnvironmentNetworkV4Request networkV4Request) {
        return networkV4Request.getSubnetIds() == null || networkV4Request.getSubnetIds().isEmpty();
    }

    private boolean missingNetworkId(EnvironmentNetworkV4Request networkV4Request) {
        return networkV4Request.getAzure() == null || !StringUtils.isNotEmpty(networkV4Request.getAzure().getNetworkId());
    }

    private boolean missingNewNetworkData(EnvironmentNetworkV4Request networkV4Request) {
        return missingNetworkCidr(networkV4Request) || missingSubnetCidrs(networkV4Request);
    }

    private boolean missingNetworkCidr(EnvironmentNetworkV4Request networkV4Request) {
        return !StringUtils.isNotEmpty(networkV4Request.getNetworkCidr());
    }

    private boolean missingSubnetCidrs(EnvironmentNetworkV4Request networkV4Request) {
        return networkV4Request.getSubnetCidrs() == null || networkV4Request.getSubnetCidrs().isEmpty();
    }

    private boolean hasResourceGroup(EnvironmentNetworkV4Request networkV4Request) {
        return networkV4Request.getAzure() != null && StringUtils.isNotEmpty(networkV4Request.getAzure().getResourceGroupName());
    }
}
