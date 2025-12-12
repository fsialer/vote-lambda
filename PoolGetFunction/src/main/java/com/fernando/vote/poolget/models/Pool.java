package com.fernando.vote.poolget.models;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Pool {
    private String poolId;
    private String question;
    private Boolean active;
    private List<Option> options;
}
