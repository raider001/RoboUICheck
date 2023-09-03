package com.kalynx.snagtest.data;

import java.util.Optional;

public class SuccessfulResult<T> extends Result<T> {

    public SuccessfulResult(Optional<T> data) {
        this(data, "");
    }
    public SuccessfulResult(Optional<T> data, String info) {
        super(true, data, info);
    }
}
