package se.iths.katharina.webshop;

public class ConsoleOutInputHandler implements OutInputHandler {
    @Override
    public String prompt(String message) {
        return IO.readln(message);
    }

    @Override
    public void info(String message) {
        IO.println(message);
    }

    @Override
    public String menu() {

        IO.println();
        IO.println(Ansi.BOLD + Ansi.GREEN + "HUVUDMENY" + Ansi.RESET);
        IO.println(Ansi.TEAL + "───────────────────────────────────────────────" + Ansi.RESET);
        IO.println(Ansi.BOLD + "[1]" + Ansi.RESET + " Lägg till produkt");
        IO.println(Ansi.BOLD + "[2]" + Ansi.RESET + " Lista alla produkter");
        IO.println(Ansi.BOLD + "[3]" + Ansi.RESET + " Visa information om en produkt");
        IO.println(Ansi.TEAL + "───────────────────────────────────────────────" + Ansi.RESET);
        IO.println(Ansi.BOLD + "[4]" + Ansi.RESET + " Avsluta applikationen");
        IO.println();
        return IO.readln(Ansi.BOLD + "Välj en funktion [1]-[4]: " + Ansi.RESET);
    }

    @Override
    public void showHeader(String textHeader) {
        IO.println("");
        IO.println(Ansi.TEAL + "▌  " + Ansi.RESET
                + Ansi.BOLD + textHeader + Ansi.RESET);
    }
}
