package com.alnnu.listaComprasApi.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Data
@Entity
@Table(name = "List")
@AllArgsConstructor
public class ListEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  private Boolean active;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "product_List")
  private List<ProductEntity> products;

  @CreationTimestamp
  private Instant createdAt;

  @UpdateTimestamp
  private Instant updatedAt;

  // create a new list
  public ListEntity() {
    this.active = true;
    this.products = new ArrayList<ProductEntity>();
  }

  public void addProduct(ProductEntity product) {
    products.add(product);
  }

  public void removeProduct(Long productId) {
    products.removeIf((product) -> product.getId() == productId);
  }

  public void desactivade() {
    active = false;
  }
}
