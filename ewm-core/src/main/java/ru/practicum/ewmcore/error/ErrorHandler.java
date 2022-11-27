package ru.practicum.ewmcore.error;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.ServletWebRequest;

/**
 *
 */
@Data
@ControllerAdvice
@Slf4j
public class ErrorHandler {
    private static final String RETURN_ERROR_LOG = "Returning HTTP {}: {}, path: `{}'";

    @ExceptionHandler({ApiError.class})
    public ResponseEntity<Object> apiErrorException(final ApiError e,
                                                                   ServletWebRequest request) {
        final var ex = new ApiErrorDto();
        BeanUtils.copyProperties(e, ex);
        log.error(RETURN_ERROR_LOG, e.getStatus(), e.getMessage(), getRequestPath(request));
        return new ResponseEntity<>(ex, ex.getStatus());
    }

    @ExceptionHandler({MissingServletRequestParameterException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> missingServletRequestParameterException(
            final MissingServletRequestParameterException e,
            ServletWebRequest request) {
        final var ex = new ApiErrorDto().setStatus(HttpStatus.BAD_REQUEST);
        BeanUtils.copyProperties(e, ex);
        log.error(RETURN_ERROR_LOG, e.getCause(), e.getMessage(), getRequestPath(request));
        return new ResponseEntity<>(ex, HttpStatus.BAD_REQUEST);
    }
    private ResponseEntity<Object> handleExceptionInternal(HttpStatus status, ApiError apiError) {
        return new ResponseEntity<>(apiError, status);
    }

    private String getRequestPath(final ServletWebRequest request) {
        return request != null ? request.getRequest().getRequestURI() : "#{unknown}";
    }


}
