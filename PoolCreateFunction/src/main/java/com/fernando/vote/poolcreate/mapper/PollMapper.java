package com.fernando.vote.poolcreate.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fernando.vote.poolcreate.models.Option;
import com.fernando.vote.poolcreate.models.Poll;
import com.fernando.vote.poolcreate.dto.PollRequest;

import java.util.stream.Collectors;

public class PollMapper {
    private final ObjectMapper objectMapper=new ObjectMapper();
    public Poll poolRequestToPool(PollRequest pollRequest){
        return Poll.builder()
                .question(pollRequest.getQuestion())
                .options(pollRequest.getOptions().stream().map(option-> Option.builder().text(option).build()).collect(Collectors.toSet()))
                .build();
    }

    public PollRequest bodyToPollRequest(String body) throws JsonProcessingException {
       return objectMapper.readValue(body, PollRequest.class);
    }
}
