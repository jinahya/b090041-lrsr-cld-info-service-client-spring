package com.github.jinahya.datagokr.api.b090041.lrsrcldinfoservice.client;

import static java.util.Objects.requireNonNull;

abstract class AbstractLrsrCldInfoServiceClientTest<T extends AbstractLrsrCldInfoServiceClient> {

    AbstractLrsrCldInfoServiceClientTest(final Class<T> clientClass) {
        super();
        this.clientClass = requireNonNull(clientClass, "clientClass is null");
    }

    /**
     * Returns a new instance of {@link #clientClass}.
     *
     * @return a new instance of {@link #clientClass}.
     */
    protected T clientInstance() {
        try {
            return clientClass.getConstructor().newInstance();
        } catch (final ReflectiveOperationException roe) {
            throw new RuntimeException("failed to instantiate " + clientClass, roe);
        }
    }

    protected final Class<T> clientClass;
}