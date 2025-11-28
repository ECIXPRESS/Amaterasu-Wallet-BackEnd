package edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        LOGGER.warn("JSON malformado recibido: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Solicitud JSON Inválida",
                "El formato del JSON es incorrecto. Verifique la sintaxis.");
    }

    @ExceptionHandler(CustomExceptions.WalletNotFoundException.class)
    public ResponseEntity<Object> handleWalletNotFound(CustomExceptions.WalletNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, "Wallet No Encontrada", ex.getMessage());
    }

    @ExceptionHandler(CustomExceptions.InsufficientFundsException.class)
    public ResponseEntity<Object> handleInsufficientFunds(CustomExceptions.InsufficientFundsException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Fondos Insuficientes", ex.getMessage());
    }

    @ExceptionHandler(CustomExceptions.InvalidAmountException.class)
    public ResponseEntity<Object> handleInvalidAmount(CustomExceptions.InvalidAmountException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Monto Inválido", ex.getMessage());
    }

    @ExceptionHandler(CustomExceptions.WalletAlreadyExistsException.class)
    public ResponseEntity<Object> handleWalletAlreadyExists(CustomExceptions.WalletAlreadyExistsException ex) {
        return buildErrorResponse(HttpStatus.CONFLICT, "Wallet Ya Existe", ex.getMessage());
    }

    @ExceptionHandler(CustomExceptions.OperationNotAllowedException.class)
    public ResponseEntity<Object> handleOperationNotAllowed(CustomExceptions.OperationNotAllowedException ex) {
        return buildErrorResponse(HttpStatus.FORBIDDEN, "Operación No Permitida", ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgument(IllegalArgumentException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Solicitud Inválida", ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex) {
        LOGGER.error("Unhandled exception caught by GlobalExceptionHandler", ex);
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", "Error Interno del Servidor");
        body.put("message", "Ocurrió un error inesperado. Por favor, contacte al administrador.");
        body.put("details", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Object> buildErrorResponse(HttpStatus status, String error, String message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", error);
        body.put("message", message);
        return new ResponseEntity<>(body, status);
    }
}