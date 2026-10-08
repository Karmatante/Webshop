package se.iths.katharina.webshop;

import se.iths.katharina.webshop.model.Product;

import java.util.List;

public interface ProductStorage {

    void saveProduct(Product product);

    List<Product> getProducts();

    Product getProduct(String articleNumber);

}
