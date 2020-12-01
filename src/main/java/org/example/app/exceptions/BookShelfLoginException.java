package org.example.app.exceptions;

public class BookShelfLoginException extends Exception {

    private final String message;

    public BookShelfLoginException(String massage) {
        this.message = massage;
    }
    @Override
    public String getMessage(){
        return message;
    }

}
