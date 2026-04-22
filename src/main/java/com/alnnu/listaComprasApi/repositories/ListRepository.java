package com.alnnu.listaComprasApi.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alnnu.listaComprasApi.entities.ListEntity;

@Repository
public interface ListRepository extends JpaRepository<ListEntity, Long> {
  boolean existsByActiveTrue();

  Optional<ListEntity> findByActiveTrue();
}
