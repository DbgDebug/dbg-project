package icu.dbgdev.pay.service.order;

import com.alibaba.fastjson.JSON;
import icu.dbgdev.pay.service.order.pojo.OrderDTO;
import icu.dbgdev.pay.service.order.pojo.OrderForm;
import icu.dbgdev.pay.service.order.pojo.ProductForm;
import icu.dbgdev.pay.service.product.ProductService;
import icu.dbgdev.pay.service.product.pojo.ProductDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class OrderService {
    private final static Logger log = LoggerFactory.getLogger(OrderService.class);

    private final ProductService productService;

    private final Map<Long, OrderDTO> orderMap = new ConcurrentHashMap<>();

    private final ReentrantLock refreshLock = new ReentrantLock();

    private final AtomicInteger atomicInteger = new AtomicInteger();

    public OrderService(ProductService productService) {
        this.productService = productService;
    }

    public OrderDTO createOrder(OrderForm orderForm) {
        log.info(JSON.toJSONString(orderForm));
        BigDecimal totalAmount = new BigDecimal("0.00");
        List<ProductDTO> productList = new ArrayList<>(orderForm.getProductList().size());
        for (ProductForm productForm : orderForm.getProductList()) {
            ProductDTO productDTO = productService.getProductDetail(productForm.getProductId());
            BigDecimal productTotal = productDTO.getPrice().multiply(new BigDecimal(productForm.getQuantity()));
            totalAmount = totalAmount.add(productTotal);
            productList.add(productDTO);
        }
        int timestamp = (int) (System.currentTimeMillis() / 1000);
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setTotalAmount(totalAmount);
        orderDTO.setCreateTime(timestamp);
        orderDTO.setProductList(productList);
        orderDTO.setOrderId(System.currentTimeMillis());
        orderMap.put(orderDTO.getOrderId(), orderDTO);

        if (atomicInteger.addAndGet(1) > 100) {
            atomicInteger.set(0);
            CompletableFuture.runAsync(this::refreshOrderMap);
        }

        return orderDTO;
    }

    public OrderDTO getOrderDetail(Long orderId) {
        return orderMap.remove(orderId);
    }

    public void refreshOrderMap() {
        final ReentrantLock lock = refreshLock;
        if (!lock.tryLock()) {
            return;
        }
        try {
            Set<Map.Entry<Long, OrderDTO>> entrySet = orderMap.entrySet();
            Iterator<Map.Entry<Long, OrderDTO>> iterator = entrySet.iterator();
            int timestamp = (int) (System.currentTimeMillis() / 1000);
            while (iterator.hasNext()) {
                Map.Entry<Long, OrderDTO> entry = iterator.next();
                OrderDTO orderDTO = entry.getValue();
                if (timestamp - orderDTO.getCreateTime() > 1700) {
                    iterator.remove();
                }
            }
        } finally {
            lock.unlock();
        }

    }
}
