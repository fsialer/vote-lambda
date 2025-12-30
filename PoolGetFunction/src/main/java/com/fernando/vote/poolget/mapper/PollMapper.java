package com.fernando.vote.poolget.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fernando.vote.poolget.dto.Poll;

public class PollMapper {
    public Poll stringToPoll(String str) {
        try {
            ObjectMapper obj = new ObjectMapper();

            return obj.readValue(str, Poll.class);
        } catch (JsonProcessingException e) {
            return null;

        }
    }

    public String pollToString(Poll poll){
        try {
            ObjectMapper obj = new ObjectMapper();

            return obj.writeValueAsString(poll);
        } catch (JsonProcessingException e) {
            return null;

        }

    }
}
