package se.iths.katharina.webshop;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import se.iths.katharina.webshop.model.Product;

import java.util.List;

public class WebShopManagerTest {


    @Test
    @DisplayName("Kontrollera att produkter sparas")
    void shouldReturnTwoSavedProducts() {
        //Arrange
        ProductListStorage productListStorage = new ProductListStorage();
        ConsoleOutInputHandler consoleOutInputHandler = new ConsoleOutInputHandler();
        WebShopManager webShopManager = new WebShopManager(productListStorage, consoleOutInputHandler);

        Product product1 = new Product("A001", "Laptop", 799.99, "16-inch laptop");
        Product product2 = new Product("A002", "Hörlurar", 99.99, "trådlösa hörlurar");

        //Act
        webShopManager.saveProductToStorage(product1);
        webShopManager.saveProductToStorage(product2);
        List<Product> products = webShopManager.getProductsFromStorage();

        //Assert
        int size = products.size();
        Assertions.assertEquals(2, size);
    }


    @Test
    @DisplayName("Kontrollera att produkt returneras genom artikelnummer")
    void shouldReturnProductByArticleNumber() {
        //Arrange
        ProductListStorage productListStorage = new ProductListStorage();
        ConsoleOutInputHandler consoleOutInputHandler = new ConsoleOutInputHandler();
        WebShopManager webShopManager = new WebShopManager(productListStorage, consoleOutInputHandler);

        Product product1 = new Product("A001", "Laptop", 799.99, "16-inch laptop");

        //Act
        webShopManager.saveProductToStorage(product1);
        Product givenProduct = webShopManager.getProductFromStorage("A001");

        //Assert
        String articleNumber = givenProduct.getArticleNumber();
        Assertions.assertEquals("A001", articleNumber);
    }

    @Test
    @DisplayName("Kontrollerar att resultat blir null vid ej hittad produkt")
    void shouldReturnNullWhenProductNotFound() {
        //Arrange
        ProductListStorage productListStorage = new ProductListStorage();
        ConsoleOutInputHandler consoleOutInputHandler = new ConsoleOutInputHandler();
        WebShopManager webShopManager = new WebShopManager(productListStorage, consoleOutInputHandler);

        Product product1 = new Product("A001", "Laptop", 799.99, "16-inch laptop");
        Product product2 = new Product("A002", "Hörlurar", 99.99, "trådlösa hörlurar");

        //Act
        webShopManager.saveProductToStorage(product1);
        webShopManager.saveProductToStorage(product2);
        Product givenProduct = webShopManager.getProductFromStorage("B001");

        //Assert
        Assertions.assertNull(givenProduct);
    }


}
