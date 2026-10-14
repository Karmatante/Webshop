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

        showWelcomeMessage();

        Discount discount = configureDiscount();

        String choice = outInputHandler.menu();

        while (!choice.equals("4")) {
            if (choice.equals("1")) {
                registerProduct();
            }
            if (choice.equals("2")) {
                showProductList(discount);
            }
            if (choice.equals("3")) {
                showProductDetails(discount);
            }
            if (!choice.equals("1") && !choice.equals("2") && !choice.equals("3")) {
                outInputHandler.info("Ogiltigt val. Försök igen.");
            }
            choice = outInputHandler.menu();
        }
        showGoodbyeMessage();

    }

    private void registerProduct() {
        outInputHandler.info("");
        outInputHandler.showHeader("LÄGG TILL NY PRODUKT");
        outInputHandler.info("Fyll i uppgifterna nedan för att registrera en ny produkt.");
        outInputHandler.info("");
        try {
            String articleNumber = readValidArticleNumber();

            String title = readNonBlankText("Ange produkttitel: ", "Produkttiteln får inte vara tom. Försök igen.");
            while (title.contains(";")) {
                outInputHandler.info(Ansi.RED + "Produkttiteln får inte innehålla semikolon. Försök igen." + Ansi.RESET);
                title = readNonBlankText("Ange produkttitel: ", "Produkttiteln får inte vara tom. Försök igen.");
            }
            double price = readValidPrice();

            String description = readNonBlankText("Ange en produktbeskrivning: ", "Produktbeskrivningen får inte vara tom. Försök igen.");

            Product product = new Product(articleNumber, title, price, description);
            showProductReview(product);

            handleSaveDecision(product);

        } catch (UncheckedIOException e) {
            outInputHandler.info(Ansi.BOLD + Ansi.RED + "Produktregistreringen kunde inte fortsätta eftersom produktfilen inte kunde läsas." + Ansi.RESET);
        }
        outInputHandler.info("");
        outInputHandler.prompt(Ansi.BOLD + Ansi.ITALIC + Ansi.TEAL + "[Tryck Enter för att återgå till huvudmenyn...]" + Ansi.RESET);
    }

    private void handleSaveDecision(Product product) {
        String saveChoice = outInputHandler.prompt("Ditt val [1-2]: ");
        while (!saveChoice.equals("1") && !saveChoice.equals("2")) {
            outInputHandler.info("Ogiltigt val. Ange 1 eller 2.");
            saveChoice = outInputHandler.prompt("Ditt val [1-2]: ");
        }
        if (saveChoice.equals("1")) {
            try {
                saveProductToStorage(product);
                outInputHandler.info(Ansi.GREEN + Ansi.BOLD + "Produkten har sparats!" + Ansi.RESET);
                outInputHandler.info(Ansi.ITALIC + product.getTitle() + " (" + product.getArticleNumber() + ")" + Ansi.RESET);
            } catch (UncheckedIOException e) {
                outInputHandler.info(Ansi.BOLD + Ansi.RED + "Produkten kunde inte sparas. Återgå till huvudmenyn och försök igen." + Ansi.RESET);
            }
        }
        if (saveChoice.equals("2")) {
            outInputHandler.info("Produktregistreringen har avslutats.");
        }
    }

    private String readNonBlankText(String promptText, String errorMessage) {
        String text = outInputHandler.prompt(promptText);
        while (text.isBlank()) {
            outInputHandler.info(Ansi.RED + errorMessage + Ansi.RESET);
            text = outInputHandler.prompt(promptText);
        }
        return text;
    }

    private String readValidArticleNumber() {
        String articleNumber = outInputHandler.prompt("Ange artikelnummer: ");
        while (articleNumber.isBlank() || articleNumber.contains(";") || getProductFromStorage(articleNumber) != null) {
            if (articleNumber.isBlank()) {
                outInputHandler.info(Ansi.RED + "Artikelnumret får inte vara tomt. Försök igen." + Ansi.RESET);
            } else if (articleNumber.contains(";")) {
                outInputHandler.info(Ansi.RED + "Artikelnumret får inte innehålla semikolon. Försök igen." + Ansi.RESET);
            } else {
                outInputHandler.info(Ansi.RED + "Artikelnummer finns redan. Försök igen." + Ansi.RESET);
            }
            articleNumber = outInputHandler.prompt("Ange artikelnummer: ");
        }
        return articleNumber;
    }

    private double readValidPrice() {
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
        return price;
    }

    private void showProductReview(Product product) {
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
        outInputHandler.info(Ansi.ORANGE + Ansi.ITALIC + " ⚠ Produkten är ännu inte sparad." + Ansi.RESET);
        outInputHandler.info("");
        outInputHandler.info("Vill du spara produkten?");
        outInputHandler.info(Ansi.GREEN + "[1] Bekräfta och spara" + Ansi.RESET);
        outInputHandler.info(Ansi.RED + "[2] Avbryt utan att spara" + Ansi.RESET);
    }

    private void showProductDetails(Discount discount) {
        outInputHandler.showHeader("VISA PRODUKTINFORMATION");
        try {
            String articleNumber = outInputHandler.prompt("Ange artikelnummer: ");
            outInputHandler.info(Ansi.TEAL + "───────────────────────────────────────────────" + Ansi.RESET);
            Product product = getProductFromStorage(articleNumber);
            if (product != null) {
                displayProduct(discount, product);
            } else {
                outInputHandler.info(Ansi.ORANGE + "⚠ INGEN PRODUKT HITTADES" + Ansi.RESET);
                outInputHandler.info("Ingen produkt hittades med artikelnummer " + Ansi.ITALIC + Ansi.GRAY + articleNumber + Ansi.RESET);
            }
            outInputHandler.info(Ansi.TEAL + "───────────────────────────────────────────────" + Ansi.RESET);

        } catch (UncheckedIOException e) {
            outInputHandler.info(Ansi.BOLD + Ansi.RED + "Produktinformationen kunde inte hämtas. Återgå till huvudmenyn och försök igen." + Ansi.RESET);
        }
        outInputHandler.prompt(Ansi.BOLD + Ansi.ITALIC + Ansi.TEAL + "[Tryck Enter för att återgå till huvudmenyn...]" + System.lineSeparator() + Ansi.RESET);
    }

    private void displayProduct(Discount discount, Product product) {
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
    }

    private void showProductList(Discount discount) {
        outInputHandler.showHeader("PRODUKTÖVERSIKT");
        try {
            List<Product> products = getProductsFromStorage();
            for (Product product : products) {
                outInputHandler.info(Ansi.TEAL + "───────────────────────────────────────────────" + Ansi.RESET);
                displayProduct(discount, product);

            }
            if (products.isEmpty()) {
                outInputHandler.info("Det finns inga registrerade produkter ännu.");
            }
        } catch (UncheckedIOException e) {
            outInputHandler.info(Ansi.BOLD + Ansi.RED + "Produktlistan kunde inte läsas. Återgå till huvudmenyn och försök igen." + Ansi.RESET);
        } catch (IllegalArgumentException e) {
            outInputHandler.info("Produktfilen innehåller felaktiga produktuppgifter. Produktlistan kan inte visas.");
        }

        outInputHandler.info("");
        outInputHandler.prompt(Ansi.BOLD + Ansi.ITALIC + Ansi.TEAL + "[Tryck Enter för att återgå till huvudmenyn...]" + System.lineSeparator() + Ansi.RESET);
    }

    private Discount configureDiscount() {
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
        return discount;
    }

    private void showGoodbyeMessage() {
        outInputHandler.info("");
        outInputHandler.showHeader("AVSLUTAR POSH & SPICY");
        outInputHandler.info("Tack för att du använde POSH & SPICY!");
        outInputHandler.info("Produktadministrationen avslutas.");
        outInputHandler.info(Ansi.TEAL + Ansi.ITALIC + "På återseende!" + Ansi.RESET);
    }

    private void showWelcomeMessage() {
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


        outInputHandler.info(welcomeText);
    }
}
