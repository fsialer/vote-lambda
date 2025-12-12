package com.fernando.vote.workerfunction.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Vote {
    private String pk;
    private String sk;
    private long votes;
}
