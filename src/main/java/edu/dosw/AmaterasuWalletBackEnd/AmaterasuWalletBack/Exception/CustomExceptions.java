package edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Exception;

public class CustomExceptions {

    public static class WalletNotFoundException extends RuntimeException {
        public WalletNotFoundException(String message) {
            super(message);
        }
    }

    public static class InsufficientFundsException extends RuntimeException {
        public InsufficientFundsException(String message) {
            super(message);
        }
    }

    public static class InvalidAmountException extends RuntimeException {
        public InvalidAmountException(String message) {
            super(message);
        }
    }

    public static class WalletAlreadyExistsException extends RuntimeException {
        public WalletAlreadyExistsException(String message) {
            super(message);
        }
    }

    public static class OperationNotAllowedException extends RuntimeException {
        public OperationNotAllowedException(String message) {
            super(message);
        }
    }
}