package icu.dbgdev.pay.service.order.pojo;

import java.util.List;

public class OrderForm {
    private List<ProductForm> productList;

    public List<ProductForm> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductForm> productList) {
        this.productList = productList;
    }
}
