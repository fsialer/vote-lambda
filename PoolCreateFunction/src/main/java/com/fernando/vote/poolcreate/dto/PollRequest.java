package com.fernando.vote.poolcreate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PollRequest {
    @JsonProperty("question")
    private String question;
    @JsonProperty("options")
    private Set<String> options;
}
