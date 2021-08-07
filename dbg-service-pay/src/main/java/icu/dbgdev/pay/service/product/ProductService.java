package icu.dbgdev.pay.service.product;

import icu.dbgdev.pay.service.product.pojo.ProductDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductService {
    private final Map<Integer, ProductDTO> productMap = new HashMap<Integer, ProductDTO>(){
        {
            put(1, new ProductDTO(1, "肥宅水", new BigDecimal("3.00")));
            put(2, new ProductDTO(2, "花生", new BigDecimal("2.50")));
            put(3, new ProductDTO(3, "瓜子", new BigDecimal("2.50")));
            put(4, new ProductDTO(4, "矿泉水", new BigDecimal("2.00")));
            put(5, new ProductDTO(5, "八宝粥", new BigDecimal("3.50")));
            put(6, new ProductDTO(6, "啤酒", new BigDecimal("2.50")));
            put(7, new ProductDTO(7, "酸奶", new BigDecimal("4.00")));
            put(8, new ProductDTO(8, "纯牛奶", new BigDecimal("3.00")));
        }
    };

    private final List<ProductDTO> productList = new ArrayList<>(productMap.values());

    public List<ProductDTO> getProductList() {
        return productList;
    }

    public ProductDTO getProductDetail(Integer productId) {
        return productMap.get(productId);
    }
}
