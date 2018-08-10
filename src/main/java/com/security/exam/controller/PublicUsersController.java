package com.security.exam.controller;

import com.security.exam.service.UserAuthenticationService;
import com.security.exam.service.UserRegistrationService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import java.util.logging.Level;
import java.util.logging.Logger;

import static com.google.common.base.Strings.emptyToNull;
import static java.util.Optional.ofNullable;
import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

@RestController
@RequestMapping("/public/users")
@FieldDefaults(level = PRIVATE, makeFinal = true)
@AllArgsConstructor(access = PACKAGE)
final class PublicUsersController {

    @NonNull
    UserAuthenticationService authentication;
    @NonNull
    UserRegistrationService registration;

    private static final Logger LOGGER = Logger.getLogger(PublicUsersController.class.getName());

    @PostMapping("/register")
    String register(
            final HttpServletRequest request,
            @RequestParam("username") final String username,
            @RequestParam(value = "password", required = false) final String password) {
        registration.register(username, ofNullable(emptyToNull(password)));
        LOGGER.log(Level.INFO, "/public/users : {}", "success");
        return authentication.login(username, password).orElseThrow(RuntimeException::new);
    }

    @PostMapping("/login")
    String login(
            final HttpServletRequest request,
            @RequestParam("username") final String username,
            @RequestParam("password") final String password) {
        return authentication
                .login(username, password)
                .orElseThrow(() -> new RuntimeException("invalid login and/or password"));
    }
}