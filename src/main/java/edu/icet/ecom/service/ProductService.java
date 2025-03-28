package edu.icet.ecom.service;

import edu.icet.ecom.dto.Product;

import java.util.List;

public interface ProductService {
    void addProduct(Product product);
    Product searchProductById(Integer id);
    List<Product> searchProductByName(String name);
    void updateProduct(Product product);
    void deleteProduct(Integer code);
    List<Product> getAllProduct();

}
