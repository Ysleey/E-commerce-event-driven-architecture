package com.ecommerce.order.adapter.in.observability;

import java.util.Optional;

public final class CorrelationContext {

    private static final ThreadLocal<String> CORRELATION_ID = new ThreadLocal<>();

    private CorrelationContext() {
    }

    public static void setCorrelationId(String correlationId) {
        CORRELATION_ID.set(correlationId);
    }

    public static Optional<String> getCorrelationId() {
        return Optional.ofNullable(CORRELATION_ID.get());
    }

    public static void clear() {
        CORRELATION_ID.remove();
    }
}
