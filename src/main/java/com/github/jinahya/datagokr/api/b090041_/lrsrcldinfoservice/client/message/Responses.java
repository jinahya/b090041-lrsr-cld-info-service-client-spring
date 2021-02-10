package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message;

import java.util.function.Function;

import static java.util.Objects.requireNonNull;

/**
 * A utility class for {@link Response}.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
public final class Responses {

    // -------------------------------------------------------------------------------------------------- static methods

    /**
     * Indicates specified response has a successful result code.
     *
     * @param response the response.
     * @return {@code true} if {@code response} has a successful result code; {@code false} otherwise.
     * @see Header#isResultCodeSuccess()
     */
    public static boolean isResultSuccessful(final Response response) {
        requireNonNull(response, "response is null");
        return requireNonNull(response.getHeader(), "response.getHeader() is null").isResultCodeSuccess();
    }

    /**
     * Verifies specified response has a successful result code and throws an exception evaluated by specified function
     * when the response is not a successful result.
     *
     * @param response the response.
     * @param function the function.
     * @param <T>      exception type parameter
     * @return given {@code response}.
     * @see #isResultSuccessful(Response)
     */
    public static <T extends RuntimeException> Response requireResultSuccessful(
            final Response response, final Function<? super Header, ? extends T> function) {
        if (!isResultSuccessful(response)) {
            throw function.apply(response.getHeader());
        }
        return response;
    }

    // ---------------------------------------------------------------------------------------------------- constructors
    private Responses() {
        throw new AssertionError("instantiation is not allowed");
    }
}
