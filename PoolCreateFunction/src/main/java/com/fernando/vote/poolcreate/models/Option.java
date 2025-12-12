package com.fernando.vote.poolcreate.models;

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
