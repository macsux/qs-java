package com.docusign.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.ControllerAdvice;

import javax.servlet.http.HttpSession;

@ControllerAdvice
@Scope("session")
public class GlobalControllerAdvice {

    @Autowired
    private HttpSession httpSession;
}