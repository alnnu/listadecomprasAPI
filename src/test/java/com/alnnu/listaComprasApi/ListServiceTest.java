package com.alnnu.listaComprasApi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.alnnu.listaComprasApi.entities.ListEntity;
import com.alnnu.listaComprasApi.entities.ProductEntity;
import com.alnnu.listaComprasApi.entities.dto.ProductIdsDTO;
import com.alnnu.listaComprasApi.repositories.ListRepository;
import com.alnnu.listaComprasApi.repositories.ProductRepository;
import com.alnnu.listaComprasApi.services.ListService;
import com.alnnu.listaComprasApi.utils.exceptions.ActiveListException;
import com.alnnu.listaComprasApi.utils.exceptions.NotFoundException;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@ExtendWith(MockitoExtension.class)
public class ListServiceTest {

  @Mock
  ListRepository repositoryMock;

  @Mock
  ProductRepository productRepositoryMock;

  @InjectMocks
  ListService service;

  /*
   * Test if the service is creating a list: For this create a list using the
   * service and assert if value returned is not null and verify the mock call
   */
  @Test
  @DisplayName("Create List Test Pass")
  void createListPass() {

    when(repositoryMock.existsByActiveTrue()).thenReturn(false);

    ListEntity entity = service.createOne();

    assertNotNull(entity, "should exist one list active");
    verify(repositoryMock, times(1)).existsByActiveTrue();
  }

  /*
   * Test if the service emit a ActiveListException if try to create a list with
   * another list active: For this return true for
   * repositoryMock.existsByActiveTrue and try to create a list, assert the
   * exception and verify the mock call
   */
  @Test
  @DisplayName("Create List Test Fail")
  void createListFail() {

    when(repositoryMock.existsByActiveTrue()).thenReturn(true);

    assertThrows(ActiveListException.class, () -> service.createOne());
    verify(repositoryMock, times(1)).existsByActiveTrue();
  }

  /*
   * Test adding a product to an active list: for this addProducts assertNotNull
   * for the list, assert the product in the list is equal to product and verify
   * mocks calls
   */
  @Test
  @DisplayName("Add a single product to an active list")
  void addProductAtiveList() {
    List<Long> ids = new ArrayList<Long>();

    ids.add(1l);

    ProductIdsDTO productIdsDTO = new ProductIdsDTO(ids);

    ListEntity list = new ListEntity();

    ProductEntity product = new ProductEntity(1l, "test", "test", "test");

    when(repositoryMock.findByActiveTrue()).thenReturn(Optional.of(list));
    when(productRepositoryMock.findById(1l)).thenReturn(Optional.of(product));

    list = service.addProducts(productIdsDTO);

    assertNotNull(list);
    assertEquals(list.getProducts().get(0), product);
    verify(repositoryMock, times(1)).findByActiveTrue();
    verify(productRepositoryMock, times(1)).findById(1l);
  }

  /*
   * Test adding two products to an active list
   */
  @Test
  @DisplayName("Add two product to an active list")
  void addTwoProductAtiveList() {
    List<Long> ids = new ArrayList<Long>();

    ids.add(1l);
    ids.add(2l);

    ListEntity list = new ListEntity();

    ProductEntity productOne = new ProductEntity(1l, "test1", "test", "test");
    ProductEntity productTwo = new ProductEntity(2l, "test2", "test", "test");

    ProductIdsDTO dto = new ProductIdsDTO(ids);

    when(repositoryMock.findByActiveTrue()).thenReturn(Optional.of(list));

    when(productRepositoryMock.findById(1l)).thenReturn(Optional.of(productOne));
    when(productRepositoryMock.findById(2l)).thenReturn(Optional.of(productTwo));

    list = service.addProducts(dto);

    assertNotNull(list);

    assertEquals(list.getProducts().get(0), productOne);
    assertEquals(list.getProducts().get(1), productTwo);

    verify(repositoryMock, times(1)).findByActiveTrue();
    verify(productRepositoryMock, times(2)).findById(any());
  }

  /*
   * Test adding one product that dont exists with a active list
   */
  @Test
  @DisplayName("Add a product that dont exists")
  void addProductNotExistActiveList() {
    List<Long> ids = new ArrayList<Long>();

    ids.add(1l);

    ProductIdsDTO dto = new ProductIdsDTO(ids);

    ListEntity list = new ListEntity();

    when(repositoryMock.findByActiveTrue()).thenReturn(Optional.of(list));
    when(productRepositoryMock.findById(1l)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> service.addProducts(dto));
    verify(repositoryMock, times(1)).findByActiveTrue();
    verify(productRepositoryMock, times(1)).findById(1l);

  }

  /*
   * Test add duplicaded product
   */
  @Test
  @DisplayName("Add duplicaded product")
  void addDuplicatedProductActiveList() {
    List<Long> ids = new ArrayList<Long>();

    ids.add(1l);
    ids.add(2l);
    ids.add(2l);

    ListEntity list = new ListEntity();

    ProductIdsDTO dto = new ProductIdsDTO(ids);

    ProductEntity productOne = new ProductEntity(1l, "test1", "test", "test");
    ProductEntity productTwo = new ProductEntity(2l, "test2", "test", "test");

    when(repositoryMock.findByActiveTrue()).thenReturn(Optional.of(list));

    when(productRepositoryMock.findById(1l)).thenReturn(Optional.of(productOne));
    when(productRepositoryMock.findById(2l)).thenReturn(Optional.of(productTwo));

    list = service.addProducts(dto);

    assertNotNull(list);

    assertEquals(list.getProducts().get(0), productOne);
    assertEquals(list.getProducts().get(1), productTwo);
    assert (list.getProducts().size() == 2);

    verify(repositoryMock, times(1)).findByActiveTrue();
    verify(productRepositoryMock, times(3)).findById(any());

  }

  /*
   * Test if send a array with no id
   */
  @Test
  @DisplayName("Add product with no id")
  void addProductsNoIdActiveList() {
    List<Long> ids = new ArrayList<Long>();

    ListEntity list = new ListEntity();

    ProductIdsDTO dto = new ProductIdsDTO(ids);

    list = service.addProducts(dto);

    assertNotNull(list);

    assertEquals(list.getProducts().size(), 0);
  }

  /*
   * Test sending a null product
   */
  @Test
  @DisplayName("Add a null product")
  void addNullProductListActive() {
    List<Long> ids = new ArrayList<Long>();

    ids.add(null);

    ProductIdsDTO dto = new ProductIdsDTO(ids);

    assertThrows(NullPointerException.class, () -> service.addProducts(dto));

  }

  /*
   * Test sending a null product list
   */
  @Test
  @DisplayName("Add a null list product")
  void addNullProductListListActive() {

    assertThrows(NullPointerException.class, () -> service.addProducts(null));

  }
}
