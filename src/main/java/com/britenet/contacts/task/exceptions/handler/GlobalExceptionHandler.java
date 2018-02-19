package com.britenet.contacts.task.exceptions.handler;

import com.britenet.contacts.task.exceptions.notFound.ObjectNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static java.util.stream.Collectors.toList;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request
    )
    {
        BindingResult bindingResult = ex.getBindingResult();

        List<ApiFieldError> apiFieldErrors = bindingResult
                .getFieldErrors()
                .stream()
                .map(fieldError -> new ApiFieldError(fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(toList());

        List<ApiGlobalError> apiGlobalErrors = bindingResult
                .getGlobalErrors()
                .stream()
                .map(globalError -> new ApiGlobalError(globalError.getDefaultMessage()))
                .collect(toList());

        ApiErrorsView apiErrorsView = new ApiErrorsView(apiFieldErrors, apiGlobalErrors);

        return new ResponseEntity<>(apiErrorsView, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(value = {ObjectNotFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    protected ResponseEntity<String> handleNotFoundException(Exception ex, WebRequest req) {
        System.out.println("exceptions: "+ex.getClass().getSimpleName()+" message: " + ex.getMessage());
        ex.printStackTrace();

        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    protected ResponseEntity<String> handleInternalException(Exception ex, WebRequest req) {
        System.out.println("exceptions: "+ex.getClass().getSimpleName()+" message: " + ex.getMessage());
        ex.printStackTrace();

        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(value = {RuntimeException.class, Exception.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    protected ResponseEntity<String> handleBadRequestException(Exception ex, WebRequest req) {
        System.out.println("exceptions: "+ex.getClass().getSimpleName()+" message: " + ex.getMessage());
        ex.printStackTrace();

        return new ResponseEntity<>("There are unrecognized problems on the server side. Please contact with administrator."
                , HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
