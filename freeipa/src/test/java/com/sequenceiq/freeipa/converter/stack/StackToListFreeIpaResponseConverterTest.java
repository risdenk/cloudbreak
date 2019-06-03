package com.sequenceiq.freeipa.converter.stack;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sequenceiq.freeipa.api.v1.freeipa.stack.model.common.Status;
import com.sequenceiq.freeipa.api.v1.freeipa.stack.model.list.ListFreeIpaResponse;
import com.sequenceiq.freeipa.entity.Stack;
import com.sequenceiq.freeipa.entity.StackStatus;

@ExtendWith(MockitoExtension.class)
class StackToListFreeIpaResponseConverterTest {

    private static final String ENV_CRN = "envCrn";

    private static final String NAME = "freeIpa";

    private static final String CRN_1 = "crn1";

    private static final String CRN_2 = "crn2";

    @InjectMocks
    private StackToListFreeIpaResponseConverter underTest;

    @Test
    void testConvertList() {
        List<Stack> stackList = createStackList();

        List<ListFreeIpaResponse> actual = underTest.convertList(stackList);

        assertEquals(CRN_1, actual.get(0).getCrn());
        assertEquals(ENV_CRN, actual.get(0).getEnvironmentCrn());
        assertEquals(NAME, actual.get(0).getName());
        assertEquals(Status.AVAILABLE, actual.get(0).getStatus());
        assertEquals(CRN_2, actual.get(1).getCrn());
        assertEquals(ENV_CRN, actual.get(1).getEnvironmentCrn());
        assertEquals(NAME, actual.get(1).getName());
        assertEquals(Status.AVAILABLE, actual.get(1).getStatus());
    }

    private List<Stack> createStackList() {
        return List.of(createStack(CRN_1), createStack(CRN_2));
    }

    private Stack createStack(String crn) {
        Stack stack = new Stack();
        stack.setEnvironment(ENV_CRN);
        stack.setName(NAME);
        stack.setResourceCrn(crn);
        stack.setStackStatus(createStackStatus());
        return stack;
    }

    private StackStatus createStackStatus() {
        StackStatus stackStatus = new StackStatus();
        stackStatus.setStatus(Status.AVAILABLE);
        return stackStatus;
    }

}