package icu.dbgdev.pay.controller;

import club.dbg.cms.util.ResponseBuild;
import icu.dbgdev.pay.service.product.ProductService;
import icu.dbgdev.pay.service.product.pojo.ProductDTO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public ResponseBuild<List<ProductDTO>> list() {
        return ResponseBuild.ok(productService.getProductList());
    }
}
