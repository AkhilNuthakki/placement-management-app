package com.project.placement_management_app.exception;

public class PasswordNotMatchedException extends RuntimeException{
    private static final long serialVersionUID = 3L;
    public PasswordNotMatchedException(String message){
        super(message);
    }
}
