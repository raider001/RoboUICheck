package com.kalynx.uitestframework.data;

import java.util.Optional;

public class FailedResult<T> extends Result<T> {

    public FailedResult() {
        this("");
    }
    public FailedResult(String info) {
        super(false, info);
    }
    public FailedResult(String info, Optional<T> data) {
        super(false, data, info);
    }
}
