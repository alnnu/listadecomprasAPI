package com.alnnu.listaComprasApi.controllers;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alnnu.listaComprasApi.entities.ListEntity;
import com.alnnu.listaComprasApi.entities.dto.ProductIdsDTO;
import com.alnnu.listaComprasApi.services.ListService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/list")
@RequiredArgsConstructor
public class ListController {
  private final ListService service;

  @GetMapping("/active")
  public ResponseEntity<ListEntity> getactive() {

    return ResponseEntity.ok(service.getActiveList());
  }

  @GetMapping("/all")
  public ResponseEntity<Page<ListEntity>> getAllList(Pageable pageable) {

    return ResponseEntity.ok(service.getAllList(pageable));
  }

  @PostMapping("/create")
  public ResponseEntity<ListEntity> createList() {

    return new ResponseEntity<ListEntity>(service.createOne(), HttpStatus.CREATED);
  }

  @PutMapping("/add/product")
  public ResponseEntity<ListEntity> addProductList(@RequestBody ProductIdsDTO dto) {

    return new ResponseEntity<ListEntity>(service.addProducts(dto), HttpStatus.OK);
  }

  @PutMapping("/remove/product")
  public ResponseEntity<ListEntity> removeProductList(@RequestBody ProductIdsDTO dto) {

    return new ResponseEntity<ListEntity>(service.removePorducs(dto), HttpStatus.OK);
  }

  @PutMapping("/{id}/deactivate")
  public ResponseEntity<Map<String, String>> deactivateList(@PathVariable Long id) {

    return new ResponseEntity<Map<String, String>>(service.DesactivateList(id), HttpStatus.OK);
  }
}
