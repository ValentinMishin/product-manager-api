package ru.valentin.product_manager_api.exception;

public class UnSuccessfulImportException extends RuntimeException {
    public UnSuccessfulImportException(String message) {
        super(message);
    }
}
