package com.silverkey.exception;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GlobalExceptionMapper
        implements ExceptionMapper<BusinessException> {

    @Override
    public Response toResponse(BusinessException exception) {

        ErrorResponse error = new ErrorResponse(exception.getMessage());

        return Response.status(exception.getStatus())
                .type(MediaType.APPLICATION_JSON)
                .entity(error)
                .build();
    }
}
