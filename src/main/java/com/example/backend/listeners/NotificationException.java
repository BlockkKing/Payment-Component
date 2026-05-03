package com.example.backend.listeners;

public class NotificationException extends RuntimeException {

    private final Long paymentId;

    //Посмотреть GlobalExceptionHandler

    public NotificationException (Long paymentId, String message, Throwable cause) {
        super(message,cause);
        this.paymentId = paymentId;
    }

    public Long getPaymentId() {
        return paymentId;
    }
}
