package com.fernando.vote.poolget.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Option {
    private String optionId;
    private String text;
}
