package se.iths.katharina.webshop.model;

public class Product {
    private String articleNumber;
    private String title;
    private double price;
    private String description;

    public Product(String articleNumber, String title, double price, String description) {
        this.articleNumber = articleNumber;
        this.title = title;
        this.price = price;
        this.description = description;
    }

    //region getters and setters
    public String getArticleNumber() {
        return articleNumber;
    }

    public void setArticleNumber(String articleNumber) {
        this.articleNumber = articleNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    //endregion

    public String toFileLine() {
        return articleNumber + ";" + title + ";" + price + ";" + description;
    }

    public static Product fromFileLine(String line) {
        String[] parts = line.split(";", 4);
        if (parts.length != 4) {
            throw new IllegalArgumentException("Fel format: " + line);
        }
        String articleNumber = parts[0];
        String title = parts[1];
        double price = Double.parseDouble(parts[2]);
        String description = parts[3];

        return new Product(articleNumber, title, price, description);
    }

}
