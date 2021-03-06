package com.sequenceiq.environment.exception.mapper;

import javax.ws.rs.core.Response.Status;

import org.springframework.stereotype.Component;

import com.sequenceiq.cloudbreak.common.exception.ExceptionResponse;

@Component
public class DefaultExceptionMapper extends BaseExceptionMapper<Exception> {

    @Override
    protected Object getEntity(Exception exception) {
        return new ExceptionResponse("Internal server error: " + exception.getMessage());
    }

    @Override
    Status getResponseStatus() {
        return Status.INTERNAL_SERVER_ERROR;
    }

    @Override
    Class<Exception> getExceptionType() {
        return Exception.class;
    }

}
