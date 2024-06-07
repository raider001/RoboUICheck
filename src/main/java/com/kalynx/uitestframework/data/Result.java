package com.kalynx.uitestframework.data;

import com.google.gson.Gson;

import java.util.Optional;

public class Result<T> {
    private final boolean isSuccess;
    private final T data;
    private final String info;

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
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
