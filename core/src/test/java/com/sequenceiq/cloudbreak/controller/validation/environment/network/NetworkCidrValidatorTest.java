package com.sequenceiq.cloudbreak.controller.validation.environment.network;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.sequenceiq.cloudbreak.api.endpoint.v4.environment.requests.EnvironmentNetworkV4Request;
import com.sequenceiq.cloudbreak.controller.validation.ValidationResult;

public class NetworkCidrValidatorTest {

    public static final String NETWORK_CIDR = "10.0.0.0/16";

    private NetworkCidrValidator underTest;

    private EnvironmentNetworkV4Request networkV4Request;

    private ValidationResult.ValidationResultBuilder resultBuilder;

    @Before
    public void before() {
        underTest = new NetworkCidrValidator();
        resultBuilder = new ValidationResult.ValidationResultBuilder();
        networkV4Request = new EnvironmentNetworkV4Request();
    }

    @Test
    public void testValidateShouldReturnWithoutErrorsWhenTheParametersAreValid() {
        networkV4Request.setNetworkCidr(NETWORK_CIDR);
        networkV4Request.setSubnetCidrs(Set.of("10.0.1.0/24", "10.0.2.0/24"));

        underTest.validate(networkV4Request, resultBuilder);

        assertFalse(resultBuilder.build().hasError());
    }

    @Test
    public void testValidateShouldReturnWithErrorsWhenTheSubnetIsNotCorrect() {
        networkV4Request.setNetworkCidr(NETWORK_CIDR);
        networkV4Request.setSubnetCidrs(Set.of("10.0.1.0/8"));

        underTest.validate(networkV4Request, resultBuilder);

        assertTrue(resultBuilder.build().hasError());
    }

    @Test
    public void testValidateShouldReturnWithErrorWhenTheSubnetsAreMatching() {
        networkV4Request.setNetworkCidr(NETWORK_CIDR);
        networkV4Request.setSubnetCidrs(Set.of("10.0.1.0/24", "10.0.1.1/24"));

        underTest.validate(networkV4Request, resultBuilder);

        assertTrue(resultBuilder.build().hasError());
    }

}