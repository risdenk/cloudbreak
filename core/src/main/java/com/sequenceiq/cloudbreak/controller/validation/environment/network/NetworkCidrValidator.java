package com.sequenceiq.cloudbreak.controller.validation.environment.network;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.util.SubnetUtils;
import org.springframework.stereotype.Component;

import com.sequenceiq.cloudbreak.api.endpoint.v4.environment.requests.EnvironmentNetworkV4Request;
import com.sequenceiq.cloudbreak.controller.validation.ValidationResult;

@Component
public class NetworkCidrValidator {

    public void validate(EnvironmentNetworkV4Request networkV4Request, ValidationResult.ValidationResultBuilder resultBuilder) {
        if (hasNetworkCidr(networkV4Request) && hasSubnetCidrs(networkV4Request)) {
            SubnetUtils networkCidrUtil = new SubnetUtils(networkV4Request.getNetworkCidr());
            networkCidrUtil.setInclusiveHostCount(true);
            Set<SubnetUtils.SubnetInfo> validSubnets = new HashSet<>();

            for (String subnetCidr: networkV4Request.getSubnetCidrs()) {
                SubnetUtils subnetUtil = new SubnetUtils(subnetCidr);
                subnetUtil.setInclusiveHostCount(true);
                if (isInNetworkRange(networkCidrUtil.getInfo(), subnetUtil.getInfo()) && hasNotConflictWithOterSubnets(subnetUtil.getInfo(), validSubnets)) {
                    validSubnets.add(subnetUtil.getInfo());
                } else {
                    resultBuilder.error("The subnet CIDRs are not valid in the VPC CIDR.");
                }
            }
        }
    }

    private boolean hasNetworkCidr(EnvironmentNetworkV4Request networkV4Request) {
        return StringUtils.isNotEmpty(networkV4Request.getNetworkCidr());
    }

    private boolean hasSubnetCidrs(EnvironmentNetworkV4Request networkV4Request) {
        return networkV4Request.getSubnetCidrs() != null && !networkV4Request.getSubnetCidrs().isEmpty();
    }

    private boolean isInNetworkRange(SubnetUtils.SubnetInfo networkUtil, SubnetUtils.SubnetInfo subnetUtil) {
        return networkUtil.isInRange(subnetUtil.getLowAddress()) && networkUtil.isInRange(subnetUtil.getHighAddress());
    }

    private boolean hasNotConflictWithOterSubnets(SubnetUtils.SubnetInfo subnetUtil, Set<SubnetUtils.SubnetInfo> validSubnets) {
        return validSubnets.stream().noneMatch(subnet -> subnetUtil.isInRange(subnet.getLowAddress()) && subnetUtil.isInRange(subnet.getHighAddress()));
    }
}
