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
                System.lineSeparator()
                        + Ansi.BOLD + "P O S H  &  S P I C Y" + Ansi.RESET
                        + System.lineSeparator()
                        + Ansi.TEAL + "WEBSHOP · PRODUKTADMINISTRATION" + Ansi.RESET
                        + System.lineSeparator()
                        + Ansi.TEAL + "────────────────────────────────────" + Ansi.RESET
                        + System.lineSeparator()
                        + "Välkommen till produktadministrationen!"
                        + System.lineSeparator()
                        + "Hantera produkter, priser och produktinformation."
                        + System.lineSeparator();
//endregion

        outInputHandler.info(welcomeText);

        outInputHandler.showHeader("RABATTINSTÄLLNING");

        outInputHandler.info("Här kan du ange en rabattkod som används när produktpriser visas i applikationen.");
        outInputHandler.info(Ansi.ITALIC + "Rabatten påverkar inte produkternas ordinarie priser." + Ansi.RESET);
        IO.println();
        outInputHandler.info(Ansi.GREEN + "Ange rabattkod eller tryck Enter för att fortsätta med standardrabatten." + Ansi.RESET);
        IO.println();
        String discountCode = outInputHandler.prompt(Ansi.BOLD + "Ange rabattkod: " + Ansi.RESET);
        Discount discount;
        if (discountCode.equals("halfprice")) {
            discount = new HalfPriceDiscount("halfprice");
        } else {
            discount = new TenPercentDiscount("tenpercent");
        }

        String choice = outInputHandler.menu();
        while (!choice.equals("4")) {
            if (choice.equals("1")) {
                outInputHandler.showHeader("LÄGG TILL NY PRODUKT");
                String articleNumber = outInputHandler.prompt("Ange artikelnummer:");
                String title = outInputHandler.prompt("Ange produkttitel: ");
                double price = 0;
                boolean validPrice = false;

                while (!validPrice) {
                    String priceInput = outInputHandler.prompt("Ange pris: ");

                    String correctedPriceInput = priceInput.replace(',', '.');

                    try {
                        price = Double.parseDouble(correctedPriceInput);
                        validPrice = true;
                    } catch (NumberFormatException e) {
                        outInputHandler.info(Ansi.ITALIC + "Ogiltigt pris! Ange ett giltigt tal, exempelvis 79,99 eller 79.99. Försök igen." + Ansi.RESET);
                    }
                }
                String description = outInputHandler.prompt("Ange en produktbeskrivning: ");
                Product product = new Product(articleNumber, title, price, description);
                saveProductToStorage(product);
                IO.println();
                outInputHandler.info("Produktregistreringen har avslutats.");
                IO.println();
                IO.println("───────────────────────────────────────────────");
                outInputHandler.prompt(Ansi.BOLD + Ansi.ITALIC + Ansi.TEAL + "[Tryck Enter för att återgå till huvudmenyn...]" + Ansi.RESET);
            }
            if (choice.equals("2")) {
                outInputHandler.showHeader("PRODUKTÖVERSIKT");
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
                outInputHandler.showHeader("VISA PRODUKTINFORMATION");
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

            if (!choice.equals("1") && !choice.equals("2") && !choice.equals("3")) {
                outInputHandler.info("Ogiltigt val. Försök igen.");
            }

            choice = outInputHandler.menu();
        }


    }
}
