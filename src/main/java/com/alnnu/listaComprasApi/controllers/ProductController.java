package com.alnnu.listaComprasApi.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alnnu.listaComprasApi.entities.ProductEntity;
import com.alnnu.listaComprasApi.services.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/product")
@RequiredArgsConstructor
public class ProductController {

  private final ProductService service;

  @GetMapping("/all")
  public ResponseEntity<List<ProductEntity>> getAll() {
    return new ResponseEntity<List<ProductEntity>>(service.getAllProducts(), HttpStatus.OK);
  }
}
