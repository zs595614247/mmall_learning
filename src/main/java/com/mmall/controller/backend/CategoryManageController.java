package com.mmall.controller.backend;

import com.mmall.common.ServerResponse;
import com.mmall.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/manage/category/")
public class CategoryManageController {

    private final ICategoryService iCategoryService;

    @Autowired
    public CategoryManageController(ICategoryService iCategoryService) {
        this.iCategoryService = iCategoryService;
    }

    @PostMapping("add_category.do")
    public ServerResponse addCategory(String categoryName, @RequestParam(value = "parentId", defaultValue = "0") Integer parentId) {
        return iCategoryService.addCategory(categoryName, parentId);
    }

    @PostMapping("set_category_name.do")
    public ServerResponse setCategoryName(String categoryName, Integer categoryId) {
        return iCategoryService.updateCategoryName(categoryName, categoryId);
    }

    @PostMapping("get_category.do")
    public ServerResponse getChildrenParallelCategory(HttpServletRequest request, @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        return iCategoryService.getChildrenParallelCategory(categoryId);
    }

    @PostMapping("get_deep_category.do")
    public ServerResponse getCategoryAndDeepChildrenCategory(HttpServletRequest request, @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        return iCategoryService.selectCategoryAndChildrenById(categoryId);
    }
}
