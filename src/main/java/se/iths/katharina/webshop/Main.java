package se.iths.katharina.webshop;

public class Main {
    static void main() {

        ProductFileStorage productFileStorage = new ProductFileStorage();

        ConsoleOutInputHandler consoleOutInputHandler = new ConsoleOutInputHandler();

        WebShopManager webShopManager = new WebShopManager(productFileStorage, consoleOutInputHandler);

        webShopManager.startWebshop();


    }
}
