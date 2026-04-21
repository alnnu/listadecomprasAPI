package com.alnnu.listaComprasApi.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class helloworld {

  @GetMapping("/")
  public ResponseEntity<String> getHelloword() {
    return new ResponseEntity<>("helloworld!!", HttpStatus.OK);
  }
}
