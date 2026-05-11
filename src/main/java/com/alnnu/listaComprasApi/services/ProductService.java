package com.alnnu.listaComprasApi.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.alnnu.listaComprasApi.entities.ProductEntity;
import com.alnnu.listaComprasApi.repositories.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository repository;

  public List<ProductEntity> getAllProducts() {
    List<ProductEntity> entities = repository.findAll();

    return entities;
  }
}
