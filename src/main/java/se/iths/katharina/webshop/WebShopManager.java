package se.iths.katharina.webshop;

import se.iths.katharina.webshop.model.Discount;
import se.iths.katharina.webshop.model.HalfPriceDiscount;
import se.iths.katharina.webshop.model.Product;
import se.iths.katharina.webshop.model.TenPercentDiscount;

import java.util.List;

public class WebShopManager {
    private ProductStorage productStorage;
    private OutInputHandler outInputHandler;

    public WebShopManager(ProductStorage productStorage, OutInputHandler outInputHandler) {
        this.productStorage = productStorage;
        this.outInputHandler = outInputHandler;
    }

    void saveProductToStorage(Product product) {
        productStorage.saveProduct(product);
    }

    List<Product> getProductsFromStorage() {
        return productStorage.getProducts();
    }

    Product getProductFromStorage(String articleNumber) {
        return productStorage.getProduct(articleNumber);
    }

    void startWebshop() {

        //region Greeting Webshop
        String welcomeText =
                "========================================" + System.lineSeparator()
                        + "             JAVA WEBSHOP" + System.lineSeparator()
                        + "========================================";
        //endregion
        outInputHandler.info(welcomeText);

        String discountCode = outInputHandler.prompt("Ange rabattkod: ");
        Discount discount;
        if (discountCode.equals("halfprice")) {
            discount = new HalfPriceDiscount("halfprice");
        } else {
            discount = new TenPercentDiscount("tenpercent");
        }

        String choice = outInputHandler.menu();
        while (!choice.equals("4")) {
            if (choice.equals("1")) {
                String articleNumber = outInputHandler.prompt("Ange artikelnummer:");
                String title = outInputHandler.prompt("Ange produkttitel: ");
                String priceInput = outInputHandler.prompt("Ange pris: ");
                double price = Double.parseDouble(priceInput);
                String description = outInputHandler.prompt("Ange en produktbeskrivning: ");

                Product product = new Product(articleNumber, title, price, description);
                saveProductToStorage(product);
            }
            if (choice.equals("2")) {
                List<Product> products = getProductsFromStorage();
                for (Product product : products) {
                    double discountedPrice = discount.calculatePrice(product.getPrice());
                    String articleNumber = "Artikelnummer: " + product.getArticleNumber();
                    String title = "Produkttitel: " + product.getTitle();
                    String price = "Pris: " + String.format("%.2f", product.getPrice());
                    String description = "Beskrivning: " + product.getDescription();
                    String discountedPriceText = "Rabatterat pris: " + String.format("%.2f", discountedPrice);

                    String productInformation =
                            articleNumber + System.lineSeparator()
                                    + title + System.lineSeparator()
                                    + price + System.lineSeparator()
                                    + discountedPriceText + System.lineSeparator()
                                    + description;

                    outInputHandler.info(productInformation);
                }
            }
            if (choice.equals("3")) {
                String articleNumber = outInputHandler.prompt("Ange artikelnummer: ");
                Product product = getProductFromStorage(articleNumber);
                if (product != null) {
                    double discountedPrice = discount.calculatePrice(product.getPrice());
                    String articleNumberText = "Artikelnummer: " + product.getArticleNumber();
                    String titleText = "Produkttitel: " + product.getTitle();
                    String priceText = "Pris: " + String.format("%.2f", product.getPrice());
                    String descriptionText = "Produktbeskrivning: " + product.getDescription();
                    String discountedPriceText = "Rabatterat pris: " + String.format("%.2f", discountedPrice);
                    String productInfo =
                            articleNumberText + System.lineSeparator()
                                    + titleText + System.lineSeparator()
                                    + priceText + System.lineSeparator()
                                    + descriptionText + System.lineSeparator()
                                    + discountedPriceText;
                    outInputHandler.info(productInfo);
                } else {
                    outInputHandler.info("Produkten hittades inte.");
                }
            }
            choice = outInputHandler.menu();
        }


    }
}
