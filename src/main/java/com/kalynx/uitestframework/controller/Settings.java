package com.kalynx.uitestframework.controller;

import com.kalynx.uitestframework.data.FailedResult;
import com.kalynx.uitestframework.data.Result;
import com.kalynx.uitestframework.data.SuccessfulResult;

import java.time.Duration;

public class Settings {
    private Duration pollRate = Duration.ofMillis(200);
    private double matchScore = 0.95;
    private Duration timeout = Duration.ofMillis(1000);

    public Duration getPollRate() {
        return pollRate;
    }

    public Result<String> setPollRate(Duration pollRate) throws Exception {
        if (pollRate.isNegative() || pollRate.isZero()) new FailedResult<>("pollRate must be greater than 0.");
        if (pollRate.toMillis() < pollRate.toMillis())
            throw new Exception("pollRate must be less than timeoutTime: " + timeout + "<" + pollRate);
        this.pollRate = pollRate;

    }

    public double getMatchScore() {
        return matchScore;
    }

    public Result<String> setMatchScore(double matchScore) throws Exception {
        if (matchScore <= 0 || matchScore > 1)
            throw new Exception("matchScore must be greater than 0 or equal to/less than 1.");
        this.matchScore = matchScore;

    }

    public Duration getTimeout() {
        return timeout;
    }

    public void setTimeout(Duration timeout) {
        this.timeout = timeout;
    }

}
