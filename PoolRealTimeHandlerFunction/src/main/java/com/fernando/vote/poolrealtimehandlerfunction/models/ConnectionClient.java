package com.fernando.vote.poolrealtimehandlerfunction.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConnectionClient {
    private String connectionId;
    private String pollId;
}
