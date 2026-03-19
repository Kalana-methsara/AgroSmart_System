package lk.ijse.agrosmart_systembackend.exceptions;

import io.jsonwebtoken.ExpiredJwtException;
import lk.ijse.agrosmart_systembackend.util.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({UsernameNotFoundException.class, BadCredentialsException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResponse handleAuthenticationExceptions(Exception e){
        e.printStackTrace();
        return new ApiResponse(401, "Authentication Failed",
                "Invalid username/email or password");
    }

    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResponse handleExpiredJwtException(ExpiredJwtException e){
        e.printStackTrace();
        return new ApiResponse(401, "Expired Token", e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse handleRuntimeException(RuntimeException e){
        e.printStackTrace();
        return new ApiResponse(500, "Internal Server Error", e.getMessage());
    }

    @ExceptionHandler(EmailNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse handleEmailNotFoundException(EmailNotFoundException e){
        e.printStackTrace();
        return new ApiResponse(404, "Email Not Found", e.getMessage());
    }
}