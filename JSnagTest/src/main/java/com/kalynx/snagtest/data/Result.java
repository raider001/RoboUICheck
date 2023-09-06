package com.kalynx.snagtest.data;

import java.util.Optional;

public class Result<T> {
    private final boolean isSuccess;
    private final T data;
    private String info;

    public Result(boolean isSuccess, String info) {
        this(isSuccess, Optional.empty(), info);
    }
    public Result(boolean isSuccess, Optional<T> data, String info) {
        this.isSuccess = isSuccess;
        this.data = data.orElse(null);
        this.info = info;
    }

    public boolean isSuccess() {
        return isSuccess;
    }
    public boolean isFailure() { return !isSuccess; }
    public T getData() {
        return data;
    }

    public String getInfo() {
        return info;
    }
}
