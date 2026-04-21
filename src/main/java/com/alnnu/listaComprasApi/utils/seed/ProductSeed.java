package com.alnnu.listaComprasApi.utils.seed;

import com.alnnu.listaComprasApi.entities.ProductEntity;
import com.alnnu.listaComprasApi.repositories.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

import org.springframework.core.io.Resource;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductSeed implements CommandLineRunner {

  private final ProductRepository productRepository;
  private final ObjectMapper objectMapper;

  @Override
  public void run(String... args) throws Exception {
    if (productRepository.count() > 0)
      return; // evita duplicatas

    Resource resource = new ClassPathResource("static/products.json");
    List<ProductEntity> products = objectMapper.readValue(
        resource.getInputStream(),
        new TypeReference<List<ProductEntity>>() {
        });

    productRepository.saveAll(products);
    System.out.println("✅ " + products.size() + " produtos inseridos.");
  }
}
