package se.iths.katharina.webshop.model;

public abstract class Discount {
    protected String description;

    public Discount(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public abstract double calculatePrice(double originalPrice);

}
