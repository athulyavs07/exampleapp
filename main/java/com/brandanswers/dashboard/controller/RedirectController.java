package com.brandanswers.dashboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RedirectController {

  @GetMapping(value = {"/{regex:\\w+}", "/**/{regex:\\w+}","/addnew-employee"})
  public String forward404() {
    return "forward:/";
  }

}

