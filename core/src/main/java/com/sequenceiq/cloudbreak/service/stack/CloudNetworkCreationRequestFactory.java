package com.sequenceiq.cloudbreak.service.stack;

import java.util.Optional;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.sequenceiq.cloudbreak.api.endpoint.v4.environment.base.EnvironmentNetworkAzureV4Params;
import com.sequenceiq.cloudbreak.api.endpoint.v4.environment.requests.EnvironmentNetworkV4Request;
import com.sequenceiq.cloudbreak.cloud.event.platform.CloudNetworkCreationRequest;
import com.sequenceiq.cloudbreak.cloud.model.ExtendedCloudCredential;
import com.sequenceiq.cloudbreak.converter.spi.CredentialToExtendedCloudCredentialConverter;
import com.sequenceiq.cloudbreak.domain.Credential;

@Service
class CloudNetworkCreationRequestFactory {

    @Inject
    private CredentialToExtendedCloudCredentialConverter credentialToExtendedCloudCredentialConverter;

    CloudNetworkCreationRequest create(String envName, Credential credential, String cloudPlatform, String region,
            EnvironmentNetworkV4Request networkRequest) {
        ExtendedCloudCredential cloudCredential = credentialToExtendedCloudCredentialConverter.convert(credential);

        return new CloudNetworkCreationRequest(envName, cloudCredential, cloudCredential, cloudPlatform, region, networkRequest.getNetworkCidr(),
                networkRequest.getSubnetCidrs(), getNoPublicIp(networkRequest), getNoFirewallRules(networkRequest));
    }

    private Boolean getNoPublicIp(EnvironmentNetworkV4Request networkRequest) {
        return Optional.of(networkRequest)
            .map(EnvironmentNetworkV4Request::getAzure)
            .map(EnvironmentNetworkAzureV4Params::getNoPublicIp)
            .orElse(null);
    }

    private Boolean getNoFirewallRules(EnvironmentNetworkV4Request networkRequest) {
        return Optional.of(networkRequest)
            .map(EnvironmentNetworkV4Request::getAzure)
            .map(EnvironmentNetworkAzureV4Params::getNoFirewallRules)
            .orElse(null);
    }
}
