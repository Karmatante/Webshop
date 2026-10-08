package se.iths.katharina.webshop.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class HalfPriceDiscountTest {

    @Test
    @DisplayName("Returnera pris med 50 % rabatt")
    void shouldReturnFiftyPercentDiscount() {
        //Arrange
        HalfPriceDiscount halfPriceDiscount = new HalfPriceDiscount("50% rabatt");
        double price = 100.0;
        //act
        double discountedPrice = halfPriceDiscount.calculatePrice(price);
        //Assert
        Assertions.assertEquals(50.0, discountedPrice);
    }
}
