package com.fernando.vote.poolget.exceptions;

public class PollNotFoundException extends RuntimeException{
    public PollNotFoundException(String message) {
        super(message);
    }
}
