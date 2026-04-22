package com.alnnu.listaComprasApi.utils.exceptions;

public class ActiveListException extends RuntimeException {
  public ActiveListException() {
    super("There is another list active");
  }
}
