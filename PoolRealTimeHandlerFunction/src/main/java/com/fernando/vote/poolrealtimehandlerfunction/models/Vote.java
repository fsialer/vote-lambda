package com.fernando.vote.poolrealtimehandlerfunction.models;

public class Vote {
    private String optionId;
    private long votes;

    public Vote(String optionId, long votes) {
        this.optionId = optionId;
        this.votes = votes;
    }

    public String getOptionId() {
        return optionId;
    }

    public void setOptionId(String optionId) {
        this.optionId = optionId;
    }

    public long getVotes() {
        return votes;
    }

    public void setVotes(long votes) {
        this.votes = votes;
    }
}
