package com.fernando.vote.disconnecthandlerfunction.models;

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
