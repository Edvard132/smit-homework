package com.smit.tire_change_app.exceptions;

public class InvalidTireChangeTimeIdException extends Throwable{

    private final String message;

    private final Throwable cause;

    public InvalidTireChangeTimeIdException(String message) {
        this(message, null);
    }

    public InvalidTireChangeTimeIdException(String message, Throwable cause){
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
