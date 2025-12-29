package com.fernando.vote.poolcreate.repository;


import com.fernando.vote.poolcreate.models.Poll;

public interface PollRepository {
    void savePoolTransact(Poll poll);
}
