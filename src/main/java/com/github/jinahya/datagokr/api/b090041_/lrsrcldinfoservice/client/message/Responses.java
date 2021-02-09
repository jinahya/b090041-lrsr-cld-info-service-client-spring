package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message;

import java.util.function.Function;

import static java.util.Objects.requireNonNull;

public final class Responses {

    public static boolean isResultSuccessful(final Response response) {
        requireNonNull(response, "response is null");
        return requireNonNull(response.getHeader(), "response.getHeader() is null").isResultCodeSuccess();
    }

    public static <T extends RuntimeException> Response requireResultSuccessful(
            final Response response, final Function<? super Header, ? extends T> function) {
        if (isResultSuccessful(response)) {
            throw function.apply(response.getHeader());
        }
        return response;
    }

    private Responses() {
        throw new AssertionError("instantiation is not allowed");
    }
}
