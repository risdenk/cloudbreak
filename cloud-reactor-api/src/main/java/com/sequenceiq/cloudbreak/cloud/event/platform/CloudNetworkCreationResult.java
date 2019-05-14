package com.sequenceiq.cloudbreak.cloud.event.platform;

import com.sequenceiq.cloudbreak.cloud.event.CloudPlatformRequest;
import com.sequenceiq.cloudbreak.cloud.event.CloudPlatformResult;
import com.sequenceiq.cloudbreak.cloud.model.CreatedCloudNetwork;

public class CloudNetworkCreationResult extends CloudPlatformResult<CloudPlatformRequest<?>> {
    private CreatedCloudNetwork createdCloudNetwork;

    public CloudNetworkCreationResult(CreatedCloudNetwork createdCloudNetwork) {
        this.createdCloudNetwork = createdCloudNetwork;
    }

    public CloudNetworkCreationResult(CloudPlatformRequest<?> request, CreatedCloudNetwork createdCloudNetwork) {
        super(request);
        this.createdCloudNetwork = createdCloudNetwork;
    }

    public CloudNetworkCreationResult(String statusReason, Exception errorDetails, CloudPlatformRequest<?> request) {
        super(statusReason, errorDetails, request);
    }

    public CreatedCloudNetwork getCreatedCloudNetwork() {
        return createdCloudNetwork;
    }
}
