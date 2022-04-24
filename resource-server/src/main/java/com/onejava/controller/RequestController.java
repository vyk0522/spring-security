package com.onejava.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class RequestController {

    /*
    This API is secured and requires access token with mod_custom scope
     */
    @PreAuthorize("hasAuthority('SCOPE_scope1')")
    @GetMapping("/")
    public String getWelcomeMessage(Principal principal) {
        return "Welcome, " + principal.getName();
    }
}
