package com.project.placement_management_app.exception;

public class ResourceNotFoundException extends RuntimeException{
    private static final long serialVersionUID = 2L;
    public ResourceNotFoundException(String message){
        super(message);
    }
}
