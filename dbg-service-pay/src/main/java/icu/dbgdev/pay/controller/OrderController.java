package icu.dbgdev.pay.controller;

import club.dbg.cms.util.ResponseBuild;
import icu.dbgdev.pay.service.order.OrderService;
import icu.dbgdev.pay.service.order.pojo.OrderDTO;
import icu.dbgdev.pay.service.order.pojo.OrderForm;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    public ResponseBuild<OrderDTO> detail(@RequestParam("id") Long id) {
        return ResponseBuild.ok(orderService.getOrderDetail(id));
    }
}
