package com.test.customer.controller.exception;

import com.test.customer.service.exception.CustomerAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.net.URI;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(com.test.customer.service.exception.CustomerNotFoundException.class)
    public ResponseEntity<ProblemDetail> customerNotFoundException(final Exception ex, final WebRequest request) {
        final ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problem.setTitle("Customer Not Found");
        problem.setDetail(ex.getMessage());
        problem.setInstance(URI.create(request.getDescription(false).replace("uri=", "")));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problem);
    }

    @ExceptionHandler(CustomerAlreadyExistsException.class)
    public ResponseEntity<ProblemDetail> validationException(final Exception ex, final WebRequest request) {
        final ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle("Validation Exception");
        problem.setDetail(ex.getMessage());
        problem.setInstance(URI.create(request.getDescription(false).replace("uri=", "")));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        final ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle("Validation Failed");
        problem.setInstance(URI.create(request.getDescription(false).replace("uri=", "")));
        final List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();
        problem.setDetail("Invalid request payload. See 'errors' for details.");
        problem.setProperty("errors", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleAllExceptions(final Exception ex, final WebRequest request) {
        final ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problem.setTitle("Internal Server Error");
        problem.setDetail(ex.getMessage());
        problem.setInstance(URI.create(request.getDescription(false).replace("uri=", "")));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problem);
    }

}
