package com.fernando.vote.poolget.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Poll {
    private String poolId;
    private String question;
    private Boolean active;
    private String dateClosed;
    private List<Option> options;
}
