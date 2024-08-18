package com.smit.tire_change_app.exceptions;

public class InvalidWorkshopIdException extends Throwable {
    private final String message;

    private final Throwable cause;

    public InvalidWorkshopIdException(String message) {
        this(message, null);
    }

    public InvalidWorkshopIdException(String message, Throwable cause){
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
