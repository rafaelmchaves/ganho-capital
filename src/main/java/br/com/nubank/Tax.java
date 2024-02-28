package br.com.nubank;

import java.math.BigDecimal;

public class Tax {

    private final BigDecimal tax;

    public Tax(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getTax() {
        return tax;
    }
    
}
