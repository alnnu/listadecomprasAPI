package com.alnnu.listaComprasApi.entities.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddProductOnListDTO {

  private List<Long> ids;
}
