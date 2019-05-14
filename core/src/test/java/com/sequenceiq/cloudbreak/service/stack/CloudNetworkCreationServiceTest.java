package com.sequenceiq.cloudbreak.service.stack;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.sequenceiq.cloudbreak.api.endpoint.v4.environment.requests.EnvironmentNetworkV4Request;
import com.sequenceiq.cloudbreak.cloud.event.model.EventStatus;
import com.sequenceiq.cloudbreak.cloud.event.platform.CloudNetworkCreationRequest;
import com.sequenceiq.cloudbreak.cloud.event.platform.CloudNetworkCreationResult;
import com.sequenceiq.cloudbreak.cloud.model.CreatedCloudNetwork;
import com.sequenceiq.cloudbreak.domain.Credential;
import com.sequenceiq.flow.reactor.ErrorHandlerAwareReactorEventFactory;

import reactor.bus.Event;
import reactor.bus.EventBus;

@RunWith(MockitoJUnitRunner.class)
public class CloudNetworkCreationServiceTest {

    public static final String ENV_NAME = "TEST_ENV";

    public static final String CLOUD_PLATFORM = "AWS";

    public static final String REGION = "US-WEST";

    public static final String NETWORK_CIDR = "0.0.0.0/16";

    public static final Set<String> SUBNET_CIDRS = Set.of("1.1.1.1/8", "2.2.2.2/8");

    @InjectMocks
    private CloudNetworkCreationService underTest;

    @Mock
    private EventBus eventBus;

    @Mock
    private ErrorHandlerAwareReactorEventFactory eventFactory;

    @Mock
    private CloudNetworkCreationRequestFactory cloudNetworkCreationRequestFactory;

    @Test
    public void testCreatedCloudNetworkShouldCreateANewCloudNetwork() throws Exception {
        Credential credential = new Credential();
        CloudNetworkCreationRequest cloudNetworkCreationRequest = Mockito.mock(CloudNetworkCreationRequest.class);
        Event<CloudNetworkCreationRequest> event = new Event<>(cloudNetworkCreationRequest);
        CreatedCloudNetwork createdCloudNetwork = new CreatedCloudNetwork();
        CloudNetworkCreationResult cloudNetworkCreationResult = createCloudNetworkResult(EventStatus.OK, createdCloudNetwork);
        EnvironmentNetworkV4Request networkV4Request = createNetworkRequest();

        when(cloudNetworkCreationRequestFactory.create(ENV_NAME, credential, CLOUD_PLATFORM, REGION, networkV4Request))
                .thenReturn(cloudNetworkCreationRequest);
        when(eventFactory.createEvent(any(CloudNetworkCreationRequest.class))).thenReturn(event);
        when(cloudNetworkCreationRequest.await()).thenReturn(cloudNetworkCreationResult);
        when(cloudNetworkCreationRequest.selector()).thenReturn("selector");

        CreatedCloudNetwork actual = underTest.createCloudNetwork(ENV_NAME, credential, CLOUD_PLATFORM, REGION, networkV4Request);

        verify(cloudNetworkCreationRequestFactory).create(ENV_NAME, credential, CLOUD_PLATFORM, REGION, networkV4Request);
        verify(eventFactory).createEvent(any(CloudNetworkCreationRequest.class));
        verify(eventBus).notify(cloudNetworkCreationRequest.selector(), event);
        Assert.assertEquals(createdCloudNetwork, actual);
    }

    @Test(expected = GetCloudParameterException.class)
    public void testCreatedCloudNetworkShouldThrowExceptionWhenTheEventBusReturnsWithFailedStatus() throws Exception {
        Credential credential = new Credential();
        CloudNetworkCreationRequest cloudNetworkCreationRequest = Mockito.mock(CloudNetworkCreationRequest.class);
        Event<CloudNetworkCreationRequest> event = new Event<>(cloudNetworkCreationRequest);
        CreatedCloudNetwork createdCloudNetwork = new CreatedCloudNetwork();
        CloudNetworkCreationResult cloudNetworkCreationResult = createCloudNetworkResult(EventStatus.FAILED, createdCloudNetwork);
        EnvironmentNetworkV4Request networkV4Request = createNetworkRequest();

        when(cloudNetworkCreationRequestFactory.create(ENV_NAME, credential, CLOUD_PLATFORM, REGION, networkV4Request))
                .thenReturn(cloudNetworkCreationRequest);
        when(eventFactory.createEvent(any(CloudNetworkCreationRequest.class))).thenReturn(event);
        when(cloudNetworkCreationRequest.await()).thenReturn(cloudNetworkCreationResult);
        when(cloudNetworkCreationRequest.selector()).thenReturn("selector");

        underTest.createCloudNetwork(ENV_NAME, credential, CLOUD_PLATFORM, REGION, networkV4Request);

        verify(cloudNetworkCreationRequestFactory).create(ENV_NAME, credential, CLOUD_PLATFORM, REGION, networkV4Request);
        verify(eventFactory).createEvent(any(CloudNetworkCreationRequest.class));
        verify(eventBus).notify(cloudNetworkCreationRequest.selector(), event);
    }

    private EnvironmentNetworkV4Request createNetworkRequest() {
        EnvironmentNetworkV4Request networkRequest = new EnvironmentNetworkV4Request();
        networkRequest.setNetworkCidr(NETWORK_CIDR);
        networkRequest.setSubnetCidrs(SUBNET_CIDRS);
        return networkRequest;
    }

    private CloudNetworkCreationResult createCloudNetworkResult(EventStatus eventStatus, CreatedCloudNetwork createdCloudNetwork) {
        CloudNetworkCreationResult cloudNetworkCreationResult = Mockito.mock(CloudNetworkCreationResult.class);
        when(cloudNetworkCreationResult.getStatus()).thenReturn(eventStatus);
        when(cloudNetworkCreationResult.getCreatedCloudNetwork()).thenReturn(createdCloudNetwork);
        when(cloudNetworkCreationResult.getErrorDetails()).thenReturn(new IllegalStateException());
        return cloudNetworkCreationResult;
    }

}