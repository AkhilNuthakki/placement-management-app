package com.project.placement_management_app.exception;

public class PreConditionFailedException extends RuntimeException{
    private static final long serialVersionUID = 4L;
    public PreConditionFailedException(String message){
        super(message);
    }
}
