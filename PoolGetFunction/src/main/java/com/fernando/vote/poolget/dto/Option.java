package com.fernando.vote.poolget.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Option {
    private String optionId;
    private String text;
    private long votes;
}
