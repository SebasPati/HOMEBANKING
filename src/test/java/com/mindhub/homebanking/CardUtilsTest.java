package com.mindhub.homebanking;

import com.mindhub.homebanking.Utils.CardUtils;
import com.mindhub.homebanking.models.Card;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;



@SpringBootTest
@AutoConfigureTestDatabase(replace = NONE)
public class CardUtilsTest {
    @Test
    public void cardNumberIsCreated(){

        int min2 = 1000;
        int max2 = 9999;

        int cardNumber = CardUtils.GetRandomNumber(min2,max2);
        assertThat(cardNumber,is(not(0)));
    }

    @Test
    public void cardCvvIsCreated(){
        int cardCvv = CardUtils.getCvv();
        assertThat(cardCvv,is(not(0)));
    }

}
