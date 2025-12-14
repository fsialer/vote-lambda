package com.fernando.vote.connecthandlerfunction.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConnectionClient {
    private String connectionId;
    private String poolId;
}
