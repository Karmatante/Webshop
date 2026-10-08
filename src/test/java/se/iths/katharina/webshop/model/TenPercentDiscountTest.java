package se.iths.katharina.webshop.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TenPercentDiscountTest {

    @Test
    @DisplayName("Returnera pris med 10 % rabatt")
    void shouldReturnTenPercentDiscount() {
        //Arrange
        TenPercentDiscount tenPercentDiscount = new TenPercentDiscount("10% rabatt");
        double price = 100.0;
        //act
        double discountedPrice = tenPercentDiscount.calculatePrice(price);
        //Assert
        Assertions.assertEquals(90.0, discountedPrice);
    }
}
