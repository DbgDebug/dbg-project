package icu.dbgdev.pay.controller;

import club.dbg.cms.util.ResponseBuild;
import icu.dbgdev.pay.service.order.OrderService;
import icu.dbgdev.pay.service.order.pojo.OrderDTO;
import icu.dbgdev.pay.service.order.pojo.OrderForm;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseBuild<OrderDTO> createOrder(@RequestBody OrderForm orderForm) {
        return ResponseBuild.ok(orderService.createOrder(orderForm));
    }
}
