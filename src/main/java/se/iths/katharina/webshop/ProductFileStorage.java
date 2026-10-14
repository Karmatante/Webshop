package se.iths.katharina.webshop;

import se.iths.katharina.webshop.model.Product;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class ProductFileStorage implements ProductStorage {

    private Path file = Path.of("products.txt");

    @Override
    public void saveProduct(Product product) {
        String productInformation = product.toFileLine() + System.lineSeparator();
        try {
            Files.writeString(file, productInformation, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public List<Product> getProducts() {
        List<Product> products = new ArrayList<>();
        if (Files.notExists(file)) {
            return products;
        }
        try {
            List<String> lines = Files.readAllLines(file);
            for (String line : lines) {
                try {
                    Product createdProduct = Product.fromFileLine(line);
                    products.add(createdProduct);
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Felaktig produktpost i filen; " + line, e);
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return products;
    }

    @Override
    public Product getProduct(String articleNumber) {
        List<Product> products = getProducts();
        for (Product product : products) {
            if (product.getArticleNumber().equals(articleNumber)) {
                return product;
            }
        }
        return null;
    }
}
