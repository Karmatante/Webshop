package se.iths.katharina.webshop;

import se.iths.katharina.webshop.model.Discount;
import se.iths.katharina.webshop.model.HalfPriceDiscount;
import se.iths.katharina.webshop.model.Product;
import se.iths.katharina.webshop.model.TenPercentDiscount;

import java.io.UncheckedIOException;
import java.util.List;

public class WebShopManager {
    private final ProductStorage productStorage;
    private final OutInputHandler outInputHandler;

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

        //region Discount Handling
        outInputHandler.showHeader("RABATTINSTÄLLNING");

        outInputHandler.info("Här kan du ange en rabattkod som används när produktpriser visas i applikationen.");
        outInputHandler.info(Ansi.ITALIC + "Rabatten påverkar inte produkternas ordinarie priser." + Ansi.RESET);
        outInputHandler.info("");
        outInputHandler.info(Ansi.GREEN + "Ange rabattkod eller tryck Enter för att fortsätta med standardrabatten." + Ansi.RESET);
        outInputHandler.info("");
        String discountCode = outInputHandler.prompt(Ansi.BOLD + "Ange rabattkod: " + Ansi.RESET);
        Discount discount;
        if (discountCode.equals("halfprice")) {
            discount = new HalfPriceDiscount("halfprice");
        } else {
            discount = new TenPercentDiscount("tenpercent");
        }
        //endregion

        String choice = outInputHandler.menu();

        while (!choice.equals("4")) {
            //region Menu choice 1
            if (choice.equals("1")) {
                outInputHandler.info("");
                outInputHandler.showHeader("LÄGG TILL NY PRODUKT");
                outInputHandler.info("Fyll i uppgifterna nedan för att registrera en ny produkt.");
                outInputHandler.info("");
                String articleNumber = outInputHandler.prompt("Ange artikelnummer: ");
                while (articleNumber.isEmpty() || getProductFromStorage(articleNumber) != null) {
                    if (articleNumber.isEmpty()) {
                        outInputHandler.info(Ansi.RED + "Artikelnumret får inte vara tomt. Försök igen." + Ansi.RESET);
                    } else {
                        outInputHandler.info(Ansi.RED + "Artikelnummer finns redan. Försök igen." + Ansi.RESET);
                    }
                    articleNumber = outInputHandler.prompt("Ange artikelnummer: ");
                }
                String title = outInputHandler.prompt("Ange produkttitel: ");
                while (title.isEmpty()) {
                    outInputHandler.info(Ansi.RED + "Produkttiteln får inte vara tomt. Försök igen." + Ansi.RESET);
                    title = outInputHandler.prompt("Ange produkttitel: ");
                }
                double price = 0;
                boolean validPrice = false;

                while (!validPrice) {
                    String priceInput = outInputHandler.prompt("Ange pris: ");

                    String correctedPriceInput = priceInput.replace(',', '.');

                    try {
                        price = Double.parseDouble(correctedPriceInput);

                        if (price < 0) {
                            outInputHandler.info(Ansi.RED + "Priset får inte vara negativt. Vänligen försök igen." + Ansi.RESET);
                        } else {
                            validPrice = true;
                        }
                    } catch (NumberFormatException e) {
                        outInputHandler.info(Ansi.RED + Ansi.ITALIC + "Ogiltigt pris! Ange ett giltigt tal, exempelvis 79,99 eller 79.99. Försök igen." + Ansi.RESET);
                    }
                }
                String description = outInputHandler.prompt("Ange en produktbeskrivning: ");
                while (description.isEmpty()) {
                    outInputHandler.info(Ansi.RED + "Produktbeskrivningen får inte vara tom. Försök igen." + Ansi.RESET);
                    description = outInputHandler.prompt("Ange produktbeskrivning: ");
                }
                Product product = new Product(articleNumber, title, price, description);

                //region Granska produktuppgifter
                outInputHandler.info("");
                outInputHandler.showHeader("GRANSKA PRODUKTUPPGIFTER");
                outInputHandler.info("Kontrollera att uppgifterna stämmer innan du sparar produkten.");
                outInputHandler.info("");

                outInputHandler.info(Ansi.TEAL + "───────────────────────────────────────────────" + Ansi.RESET);
                outInputHandler.info("ARTIKELNUMMER        " + product.getArticleNumber());
                outInputHandler.info("PRODUKTTITEL         " + product.getTitle());
                outInputHandler.info("PRIS                 " + String.format("%.2f", product.getPrice()) + " kr");
                outInputHandler.info("PRODUKTBESKRIVNING   " + product.getDescription());

                outInputHandler.info(Ansi.TEAL + "───────────────────────────────────────────────" + Ansi.RESET);
                outInputHandler.info(Ansi.ORANGE + Ansi.ITALIC + " ⚠\uFE0E Produkten är ännu inte sparad." + Ansi.RESET);
                outInputHandler.info("");
                outInputHandler.info("Vill du spara produkten?");
                outInputHandler.info(Ansi.GREEN + "[1] Bekräfta och spara" + Ansi.RESET);
                outInputHandler.info(Ansi.RED + "[2] Avbryt utan att spara" + Ansi.RESET);

                String saveChoice = outInputHandler.prompt("Ditt val [1-2]: ");
                while (!saveChoice.equals("1") && !saveChoice.equals("2")) {
                    outInputHandler.info("Ogiltigt val. Ange 1 eller 2.");
                    saveChoice = outInputHandler.prompt("Ditt val [1-2]: ");
                }
                //endregion

                if (saveChoice.equals("1")) {
                    try {
                        saveProductToStorage(product);
                        outInputHandler.info(Ansi.GREEN + Ansi.BOLD + "Produkten har sparats!" + Ansi.RESET);
                        outInputHandler.info(Ansi.ITALIC + product.getTitle() + " (" + product.getArticleNumber() + ")" + Ansi.RESET);
                        outInputHandler.info("");
                    } catch (UncheckedIOException e) {
                        outInputHandler.info(Ansi.BOLD + Ansi.RED + "Produkten kunde inte sparas. Återgå till huvudmenyn och försök igen." + Ansi.RESET);
                    }
                    outInputHandler.prompt(Ansi.BOLD + Ansi.ITALIC + Ansi.TEAL + "[Tryck Enter för att återgå till huvudmenyn...]" + Ansi.RESET);
                }
                if (saveChoice.equals("2")) {
                    outInputHandler.info("Produktregistreringen har avslutats.");
                    outInputHandler.prompt(Ansi.BOLD + Ansi.ITALIC + Ansi.TEAL + "[Tryck Enter för att återgå till huvudmenyn...]" + Ansi.RESET);
                }
            }
            //endregion

            //region Menu choice 2
            if (choice.equals("2")) {
                outInputHandler.showHeader("PRODUKTÖVERSIKT");
                List<Product> products = getProductsFromStorage();
                for (Product product : products) {
                    outInputHandler.info(Ansi.TEAL + "───────────────────────────────────────────────" + Ansi.RESET);
                    double discountedPrice = discount.calculatePrice(product.getPrice());
                    String title = Ansi.BOLD + Ansi.TEAL + product.getTitle().toUpperCase() + Ansi.RESET;
                    String articleNumber = Ansi.GRAY + "Artikelnummer: " + product.getArticleNumber() + Ansi.RESET;
                    String price = "Ordinarie pris: " + String.format("%.2f", product.getPrice()) + " kr";
                    String description = "Produktbeskrivning: " + product.getDescription();
                    String discountedPriceText = Ansi.GREEN + "Rabatterat pris: " + String.format("%.2f", discountedPrice) + " kr" + Ansi.RESET;

                    String productInformation =
                            title + System.lineSeparator()
                                    + articleNumber + System.lineSeparator()
                                    + description + System.lineSeparator()
                                    + price + System.lineSeparator()
                                    + discountedPriceText;

                    outInputHandler.info(productInformation);
                }
                if (products.isEmpty()) {
                    outInputHandler.info("Det finns inga registrerade produkter ännu.");
                }
                outInputHandler.info("");
                outInputHandler.prompt(Ansi.BOLD + Ansi.ITALIC + Ansi.TEAL + "[Tryck Enter för att återgå till huvudmenyn...]" + System.lineSeparator() + Ansi.RESET);
            }
            //endregion

            //region Menu choice 3
            if (choice.equals("3")) {
                outInputHandler.showHeader("VISA PRODUKTINFORMATION");
                String articleNumber = outInputHandler.prompt("Ange artikelnummer: ");
                outInputHandler.info(Ansi.TEAL + "───────────────────────────────────────────────" + Ansi.RESET);
                Product product = getProductFromStorage(articleNumber);
                if (product != null) {
                    double discountedPrice = discount.calculatePrice(product.getPrice());
                    String articleNumberText = Ansi.GRAY + "Artikelnummer: " + product.getArticleNumber() + Ansi.RESET;
                    String titleText = Ansi.TEAL + Ansi.BOLD + product.getTitle().toUpperCase() + Ansi.RESET;
                    String priceText = "Ordinarie pris: " + String.format("%.2f", product.getPrice()) + " kr";
                    String descriptionText = "Produktbeskrivning: " + product.getDescription();
                    String discountedPriceText = Ansi.GREEN + "Rabatterat pris: " + String.format("%.2f", discountedPrice) + " kr" + Ansi.RESET;

                    String productInformation =
                            titleText + System.lineSeparator()
                                    + articleNumberText + System.lineSeparator()
                                    + descriptionText + System.lineSeparator()
                                    + priceText + System.lineSeparator()
                                    + discountedPriceText;

                    outInputHandler.info(productInformation);
                } else {
                    outInputHandler.info(Ansi.ORANGE + "⚠\uFE0E INGEN PRODUKT HITTADES" + Ansi.RESET);
                    outInputHandler.info("Ingen produkt hittades med artikelnummer " + Ansi.ITALIC + Ansi.GRAY + articleNumber + Ansi.RESET);
                }
                outInputHandler.info(Ansi.TEAL + "───────────────────────────────────────────────" + Ansi.RESET);
                outInputHandler.prompt(Ansi.BOLD + Ansi.ITALIC + Ansi.TEAL + "[Tryck Enter för att återgå till huvudmenyn...]" + System.lineSeparator() + Ansi.RESET);

            }

            if (!choice.equals("1") && !choice.equals("2") && !choice.equals("3")) {
                outInputHandler.info("Ogiltigt val. Försök igen.");
            }
            //endregion

            choice = outInputHandler.menu();

        }
        //region Menu choice 4
        outInputHandler.info("");
        outInputHandler.showHeader("AVSLUTAR POSH & SPICY");
        outInputHandler.info("Tack för att du använde POSH & SPICY!");
        outInputHandler.info("Produktadministrationen avslutas.");
        outInputHandler.info(Ansi.TEAL + Ansi.ITALIC + "På återseende!" + Ansi.RESET);
        //endregion
    }
}
