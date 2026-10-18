package se.iths.katharina.webshop.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ProductTest {

    @Test
    @DisplayName("Ogiltigt pris i produktfilraden ska kasta NumberFormatException")
    void fromFileLine_invalidPrice_throwsException() {
        //Arrange
        String fileLine = "TEST1001;Testprodukt;abc;En beskrivning";

        //Assert+Act
        Assertions.assertThrows(NumberFormatException.class, () -> Product.fromFileLine(fileLine));
    }

    @Test
    @DisplayName("För få fält i produktfilraden ska kasta IllegalArgumentException")
    void fromFileLine_missingFields() {
//Arrange
        String fileLine = "TEST1001;Testprodukt;19.99";
        //Assert+Act
        Assertions.assertThrows(IllegalArgumentException.class, () -> Product.fromFileLine(fileLine));
    }

    @Test
    @DisplayName("Helt tom produktfilrad ska kasta IllegalArgumentException")
    void fromFileLine_emptyProductFileLine() {
        //Arrange
        String fileLine = "";
        //Assert+Act
        Assertions.assertThrows(IllegalArgumentException.class, () -> Product.fromFileLine(fileLine));
    }


    @Test
    @DisplayName("Giltig filrad ska skapa ett korrekt Product-objekt")
    void fromFileLine_validLine_createsProduct() {
        //Arrange
        String fileLine = "T100;Anteckningsbok;89.50;Linjerade sidor";
        //Act
        Product product = Product.fromFileLine(fileLine);
        //Assert
        Assertions.assertEquals("T100", product.getArticleNumber());
    }


}