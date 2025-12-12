package com.fernando.vote.poolcreate.models;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Pool {
    private String poolId;
    private String question;
    private Set<Option> options;
}
