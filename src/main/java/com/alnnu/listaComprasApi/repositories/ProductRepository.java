package com.alnnu.listaComprasApi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alnnu.listaComprasApi.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

}
