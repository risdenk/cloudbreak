package com.sequenceiq.authorization.service;

import java.lang.annotation.Annotation;

import javax.inject.Inject;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import com.sequenceiq.authorization.annotation.DisableCheckPermissions;
import com.sequenceiq.authorization.resource.AuthorizationResourceAction;
import com.sequenceiq.authorization.resource.AuthorizationResourceType;

@Component
public class DisabledPermissionChecker implements PermissionChecker<DisableCheckPermissions> {

    @Inject
    private CommonPermissionCheckingUtils commonPermissionCheckingUtils;

    @Override
    public <T extends Annotation> Object checkPermissions(T rawMethodAnnotation, AuthorizationResourceType resource, String userCrn,
            ProceedingJoinPoint proceedingJoinPoint, MethodSignature methodSignature, long startTime) {
        return commonPermissionCheckingUtils.proceed(proceedingJoinPoint, methodSignature, startTime);
    }

    @Override
    public Class<DisableCheckPermissions> supportedAnnotation() {
        return DisableCheckPermissions.class;
    }

    @Override
    public AuthorizationResourceAction.ActionType actionType() {
        return AuthorizationResourceAction.ActionType.RESOURCE_INDEPENDENT;
    }
}
