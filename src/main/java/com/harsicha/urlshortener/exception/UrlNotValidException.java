package com.harsicha.urlshortener.exception;

public class UrlNotValidException extends Exception{

    public UrlNotValidException(String errorMessage) {
        super(errorMessage);
    }

}
