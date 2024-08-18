package com.smit.tire_change_app.exceptions;

public class WorkshopNotFoundException extends Throwable{

    private final String message;

    private final Throwable cause;

    public WorkshopNotFoundException(String message) {
        this(message, null);
    }

    public WorkshopNotFoundException(String message, Throwable cause){
        this.message = message;
        this.cause = cause;
    }

    @Override
    public Throwable getCause(){
        return cause;
    }

    @Override
    public String getMessage(){
        return message;
    }
}
