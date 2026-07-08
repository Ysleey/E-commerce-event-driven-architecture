package com.ecommerce.order.adapter.in.controller.dto;

public class UpdateShippingAddressRequest {
    private String newAddress;

    public String getNewAddress() {
        return newAddress;
    }

    public void setNewAddress(String newAddress) {
        this.newAddress = newAddress;
    }
}