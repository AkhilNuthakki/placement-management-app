package com.project.placement_management_app.exception;

public class ResourceAlreadyExistsException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public ResourceAlreadyExistsException(String message){
        super(message);
    }
}
