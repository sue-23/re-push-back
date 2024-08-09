package org.shooong.push.domain.enumData;

public enum DiscountType {
    PERCENT("%"),
    FIXED("원");

    private String symbol;

    DiscountType(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol(){
        return symbol;
    }


}