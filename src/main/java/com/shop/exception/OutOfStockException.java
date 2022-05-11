package com.shop.exception;

public class OutOfStockException extends RuntimeException{      //상품의 주문 수량 보다 재고의 수가 적을 때 발생 시킬 exception

    public OutOfStockException(String message) {
        super(message);
    }

}
