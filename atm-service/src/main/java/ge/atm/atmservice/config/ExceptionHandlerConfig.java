package ge.atm.atmservice.config;

import ge.atm.atmservice.domain.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice("ge.atm.atmservice")
public class ExceptionHandlerConfig {

    @ExceptionHandler(ResponseStatusException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> onBindException(ResponseStatusException ex) {
        return new ResponseEntity<>(new ErrorResponse(ex.getStatus().value(), ex.getMessage()), ex.getStatus());
    }
}

