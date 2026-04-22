package com.alnnu.listaComprasApi.services;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.alnnu.listaComprasApi.entities.ListEntity;
import com.alnnu.listaComprasApi.entities.ProductEntity;
import com.alnnu.listaComprasApi.repositories.ListRepository;
import com.alnnu.listaComprasApi.repositories.ProductRepository;
import com.alnnu.listaComprasApi.utils.exceptions.ActiveListException;
import com.alnnu.listaComprasApi.utils.exceptions.NotFoundException;

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

  /*
   * Remove products from a list
   */
  public ListEntity removePorducs(List<Long> productIds) {
    ListEntity entity = repository.findByActiveTrue()
        .orElseThrow(() -> new NotFoundException("No active List found"));

    for (Long id : productIds) {
      entity.removeProduct(id);
    }

    repository.save(entity);

    return entity;
  }

  /*
   * Desactivate a List
   */
  public Map<String, String> DesactivateList(Long id) {
    ListEntity entity = repository.findById(id)
        .orElseThrow(() -> new NotFoundException("List with id {" + id + "} not found"));

    entity.desactivade();

    repository.save(entity);

    Map<String, String> res = new HashMap<String, String>();

    res.put("msg", "List with id {" + id + "} desactivaded");

    return res;
  }

  /*
   * Get active list
   */
  public ListEntity getActiveList() {
    ListEntity entity = repository.findByActiveTrue()
        .orElseThrow(() -> new NotFoundException("No active List found"));

    return entity;
  }

  /*
   * Get all list
   */

}
