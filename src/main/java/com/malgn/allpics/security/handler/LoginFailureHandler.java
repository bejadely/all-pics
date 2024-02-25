package com.malgn.allpics.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import java.io.IOException;

public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        String noSuchIdMessage = "NO_SUCH_ID";
        String mismatchIdPwdMessage = "MISMATCH_ACCOUNT";
        String unexpectedErrorMessage ="UNEXPECTED_ERROR";
        String errorMessage = null;

        if (exception instanceof BadCredentialsException){
            errorMessage = mismatchIdPwdMessage;
        } else if(exception instanceof UsernameNotFoundException || exception instanceof InternalAuthenticationServiceException){
            errorMessage = noSuchIdMessage;
        } else {
            errorMessage = unexpectedErrorMessage;
        }

        setDefaultFailureUrl("/auth/login-form?error=true&exception=" + errorMessage);

        super.onAuthenticationFailure(request, response, exception);

    }
}
