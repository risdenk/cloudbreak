package com.sequenceiq.cloudbreak.core.flow2.stack.provision;

import com.sequenceiq.cloudbreak.core.flow2.AbstractAction;
import com.sequenceiq.cloudbreak.core.flow2.FlowState;
import com.sequenceiq.cloudbreak.core.flow2.RestartAction;
import com.sequenceiq.cloudbreak.core.flow2.restart.FillInMemoryStateStoreRestartAction;
import com.sequenceiq.cloudbreak.core.flow2.stack.provision.action.CheckImageAction;

public enum StackCreationState implements FlowState {
    INIT_STATE,
    STACK_CREATION_FAILED_STATE,
    SETUP_STATE,
    IMAGESETUP_STATE,
    IMAGE_CHECK_STATE(CheckImageAction.class),
    CREATE_CREDENTIAL_STATE,
    START_PROVISIONING_STATE(null, StartProvisioningRestartAction.class),
    PROVISIONING_FINISHED_STATE,
    COLLECTMETADATA_STATE,
    GET_TLS_INFO_STATE,
    TLS_SETUP_STATE,
    STACK_CREATION_FINISHED_STATE,
    FINAL_STATE;

    private Class<? extends AbstractAction> action;

    private Class<? extends RestartAction> restartAction = FillInMemoryStateStoreRestartAction.class;

    StackCreationState() {
    }

    StackCreationState(Class<? extends AbstractAction> action) {
        this.action = action;
    }

    StackCreationState(Class<? extends AbstractAction> action, Class<? extends RestartAction> restartAction) {
        this.action = action;
        this.restartAction = restartAction;
    }

    @Override
    public Class<? extends AbstractAction> action() {
        return action;
    }

    @Override
    public Class<? extends RestartAction> restartAction() {
        return restartAction;
    }
}
