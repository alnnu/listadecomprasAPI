package com.alnnu.listaComprasApi.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.alnnu.listaComprasApi.entities.ListEntity;
import com.alnnu.listaComprasApi.entities.ProductEntity;
import com.alnnu.listaComprasApi.repositories.ListRepository;
import com.alnnu.listaComprasApi.repositories.ProductRepository;
import com.alnnu.listaComprasApi.utils.exceptions.ActiveListException;
import com.alnnu.listaComprasApi.utils.exceptions.NotFoundException;

import jakarta.persistence.Entity;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ListService {

  private final ListRepository repository;

  private final ProductRepository productRepository;

  /*
   * Create an empty list;
   */
  public ListEntity createOne() {
    if (repository.existsByActiveTrue()) {
      throw new ActiveListException();
    }

    ListEntity entity = new ListEntity();

    repository.save(entity);
    return entity;
  }

  /*
   * Add products to the active list;
   * Create a new list if there is no active list;
   */
  public ListEntity addProducts(List<Long> productIds) {

    ListEntity entity = repository.findByActiveTrue().orElse(createOne());
    ProductEntity product;

    for (Long id : productIds) {
      product = productRepository.findById(id)
          .orElseThrow(() -> new NotFoundException("Product with id {" + id + "} found"));
      entity.addProduct(product);
    }

    repository.save(entity);

    return entity;
  }

}
