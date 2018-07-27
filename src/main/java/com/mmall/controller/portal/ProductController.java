package com.mmall.controller.portal;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.service.IProductService;
import com.mmall.vo.ProductDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product/")
public class ProductController {


    private IProductService iProductService;

    @Autowired
    public ProductController(IProductService iProductService) {
        this.iProductService = iProductService;
    }

    @PostMapping("detail.do")
    public ServerResponse<ProductDetailVo> detail(Integer productId) {
        return iProductService.getProductDetail(productId);
    }

    @GetMapping(value = "{productId}")
    public ServerResponse<ProductDetailVo> detailRESTFul(@PathVariable("productId") Integer productId) {
        return iProductService.getProductDetail(productId);
    }

    @PostMapping("list.do")
    public ServerResponse<PageInfo> list(@RequestParam(value = "keyword",required = false) String keyword
            ,@RequestParam(value = "categoryId",required = false) Integer categoryId
            ,@RequestParam(value = "pageNum",defaultValue = "1") int pageNum
            ,@RequestParam(value = "pageSize",defaultValue = "10") int pageSize
            ,@RequestParam(value = "orderBy",defaultValue = "") String orderBy) {
        return iProductService.getProductByKeywordCategoryId(keyword,categoryId,pageNum,pageSize,orderBy);
    }
}
