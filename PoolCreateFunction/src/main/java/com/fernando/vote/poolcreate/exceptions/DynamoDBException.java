package com.fernando.vote.poolcreate.exceptions;

public class DynamoDBException extends RuntimeException{
    public DynamoDBException(String message) {
        super(message);
    }
}
