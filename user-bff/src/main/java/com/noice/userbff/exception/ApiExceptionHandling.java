package com.noice.userbff.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.net.BindException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class ApiExceptionHandling {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleBodyValidation(MethodArgumentNotValidException ex, HttpServletRequest request){
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Bad Request");
        problemDetail.setDetail(ex.getMessage());
        Map<String, List<String>> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(fe ->
                errors.computeIfAbsent(fe.getField(), k -> new ArrayList<>()).add(defaultMessage(fe))
        );
        ex.getBindingResult().getGlobalErrors().forEach(ge ->
                errors.computeIfAbsent(ge.getObjectName(), k -> new ArrayList<>()).add(defaultMessage(ge))
        );
        log.error("Validation Error", ex);
        problemDetail.setProperty("errors", errors);
        problemDetail.setProperty("error", "VALIDATION_ERROR");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_PROBLEM_JSON).body(problemDetail);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ProblemDetail> handleHandlerMethodValidation(
            HandlerMethodValidationException ex,
            HttpServletRequest request
    ) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setTitle("Parameter validation failed");
        pd.setInstance(URI.create(request.getRequestURI()));

        Map<String, List<String>> errors = new HashMap<>();
        ex.getParameterValidationResults().forEach(r -> {
            String name = r.getMethodParameter().getParameterName();
            r.getResolvableErrors().forEach(e ->
                    errors.computeIfAbsent(name, k -> new ArrayList<>()).add(e.getDefaultMessage()));
        });
        pd.setProperty("errors", errors);
        pd.setProperty("error", "PARAM_VALIDATION_ERROR");
        log.error("Parameter validation failed", ex);

        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_PROBLEM_JSON).body(pd);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ProblemDetail> handleConstraintViolation(
            ConstraintViolationException ex, HttpServletRequest request
    ) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setTitle("Constraint violation");
        pd.setInstance(URI.create(request.getRequestURI()));
        pd.setDetail(ex.getMessage());
        Map<String, List<String>> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(v -> {
            String path = v.getPropertyPath() != null ? v.getPropertyPath().toString() : "param";
            errors.computeIfAbsent(path, k -> new ArrayList<>()).add(v.getMessage());
        });
        pd.setProperty("errors", errors);
        pd.setProperty("error", "CONSTRAINT_VIOLATION");
        log.error("Constraint violation", ex);
        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_PROBLEM_JSON).body(pd);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ProblemDetail> handleNotReadable(HttpMessageNotReadableException ex, HttpServletRequest request) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setTitle("Malformed request body");
        pd.setInstance(URI.create(request.getRequestURI()));

        pd.setDetail(ex.getMessage());
        Throwable cause = ex.getMostSpecificCause();
        if (cause instanceof com.fasterxml.jackson.databind.JsonMappingException jme) {
            List<String> path = jme.getPath().stream()
                    .map(ref -> ref.getFieldName() != null ? ref.getFieldName() : "[" + ref.getIndex() + "]")
                    .toList();
            pd.setProperty("jsonPath", String.join(".", path));
        }

        pd.setProperty("error", "MALFORMED_BODY");
        log.error("Malformed request body", ex);
        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_PROBLEM_JSON).body(pd);
    }

    @ExceptionHandler({ MissingServletRequestParameterException.class, MethodArgumentTypeMismatchException.class })
    public ResponseEntity<ProblemDetail> handleBadRequestArgs(Exception ex, HttpServletRequest request) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setTitle("Invalid request parameters");
        pd.setInstance(URI.create(request.getRequestURI()));
        pd.setDetail(ex.getMessage());
        pd.setProperty("error", "INVALID_PARAMETERS");
        log.error("Invalid request parameters", ex);
        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_PROBLEM_JSON).body(pd);
    }

    @ExceptionHandler(ErrorResponseException.class)
    public ResponseEntity<ProblemDetail> handleErrorResponse(ErrorResponseException ex, HttpServletRequest request) {
        ProblemDetail pd = ex.getBody();
        if (pd.getInstance() == null) pd.setInstance(URI.create(request.getRequestURI()));
        return ResponseEntity.status(ex.getStatusCode()).contentType(MediaType.APPLICATION_PROBLEM_JSON).body(pd);
    }

    @ExceptionHandler(NoiceBusinessLogicException.class)
    public ResponseEntity<ProblemDetail> handleBusinessException(Exception ex, HttpServletRequest request) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setTitle("Business Exception");
        pd.setInstance(URI.create(request.getRequestURI()));
        pd.setDetail(ex.getMessage());
        pd.setProperty("error", "BAD_REQUEST");
        log.error("Business Exception", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(pd);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleOther(Exception ex, HttpServletRequest request) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        pd.setTitle("Internal server error");
        pd.setInstance(URI.create(request.getRequestURI()));
        pd.setDetail(ex.getMessage());
        pd.setProperty("error", "INTERNAL_ERROR");
        log.error("Exception Thrown", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(pd);
    }


    private static String defaultMessage(Object error) {
        if (error instanceof FieldError fe && fe.getDefaultMessage() != null && !fe.getDefaultMessage().isBlank()) {
            return fe.getDefaultMessage();
        }
        return "Invalid value";
    }
}
