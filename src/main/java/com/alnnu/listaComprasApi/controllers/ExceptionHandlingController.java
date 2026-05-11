package com.alnnu.listaComprasApi.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.alnnu.listaComprasApi.utils.exceptions.ActiveListException;
import com.alnnu.listaComprasApi.utils.exceptions.NotFoundException;

@RestControllerAdvice
public class ExceptionHandlingController {

  @ExceptionHandler(ActiveListException.class)
  public ResponseEntity<Map<String, String>> activeList(ActiveListException exception) {
    Map<String, String> map = new HashMap<String, String>();

    map.put("msg", exception.getMessage());

    return new ResponseEntity<Map<String, String>>(map, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<Map<String, String>> notFound(NotFoundException exception) {
    Map<String, String> map = new HashMap<String, String>();

    map.put("msg", exception.getMessage());

    return new ResponseEntity<Map<String, String>>(map, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(NullPointerException.class)
  public ResponseEntity<Map<String, String>> nullPointer(NullPointerException exception) {
    Map<String, String> map = new HashMap<String, String>();

    map.put("msg", exception.getMessage());

    return new ResponseEntity<Map<String, String>>(map, HttpStatus.BAD_REQUEST);
  }
}
