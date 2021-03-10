package icu.dbgdev.pay.service.order;

import icu.dbgdev.pay.service.order.pojo.OrderDTO;
import icu.dbgdev.pay.service.order.pojo.OrderForm;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    public OrderDTO createOrder(OrderForm orderForm) {
        int timestamp = (int) (System.currentTimeMillis() / 1000);
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderId(System.currentTimeMillis());
        orderDTO.setCreateTime(timestamp);
        return orderDTO;
    }
}
