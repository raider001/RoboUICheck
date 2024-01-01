package com.kalynx.uitestframework.data;

import java.util.Optional;

public class SuccessfulResult<T> extends Result<T> {

    public SuccessfulResult(Optional<T> data) {
        this(data, "");
    }
    public SuccessfulResult(Optional<T> data, String info) {
        super(true, data, info);
    }
    public SuccessfulResult(String info) {
        super(true, Optional.empty(), info);
    }
    public SuccessfulResult() {
        this(Optional.empty());
    }
}
