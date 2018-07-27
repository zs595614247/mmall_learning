package com.mmall.controller.backend;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.service.IFileService;
import com.mmall.service.IProductService;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.ProductDetailVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


/**
 * @author zs595
 */
@RestController
@RequestMapping("/manage/product")
public class ProductManageController {


    private final IProductService iProductService;

    private final IFileService iFileService;

    @Autowired
    public ProductManageController(IProductService iProductService, IFileService iFileService) {
        this.iProductService = iProductService;
        this.iFileService = iFileService;
    }

    @PostMapping("save.do")
    public ServerResponse saveProduct(HttpServletRequest request, Product product) {
        return iProductService.saveOrUpdateProduct(product);
    }

    @PostMapping("set_sale_status.do")
    public ServerResponse setSaleStatus(HttpServletRequest request, Integer productId, Integer status) {
        return iProductService.setSaleStatus(productId, status);
    }

    @PostMapping("detail.do")
    public ServerResponse<ProductDetailVo> getDetail(HttpServletRequest request, Integer productId) {
        return iProductService.manageProductDetail(productId);
    }

    @PostMapping("list.do")
    public ServerResponse<PageInfo> getList(HttpServletRequest request, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        return iProductService.getProductList(pageNum, pageSize);
    }

    @PostMapping("search.do")
    public ServerResponse<PageInfo> productSearch(HttpServletRequest request, String productName, Integer productId, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        return iProductService.searchProduct(productName, productId, pageNum, pageSize);
    }

    @PostMapping("upload.do")
    public ServerResponse upload(HttpServletRequest request, @RequestParam(value = "upload_file", required = false) MultipartFile file) {
        String path = request.getSession().getServletContext().getRealPath("upload");
        String targetFileName = iFileService.upload(file, path);
        if (StringUtils.isBlank(targetFileName)) {
            return ServerResponse.createByErrorMessage("上传文件失败,请重试或联系管理员");
        }
        String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;
        Map<String, String> fileMap = Maps.newHashMap();
        fileMap.put("uri", targetFileName);
        fileMap.put("url", url);
        return ServerResponse.createBySuccess(fileMap);
    }

    @PostMapping("richtext_img_upload.do")
    public Map richtextImgUpload(@RequestParam(value = "upload_file", required = false) MultipartFile file
            , HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> resultMap = Maps.newHashMap();
        String path = request.getSession().getServletContext().getRealPath("upload");
        String targetFileName = iFileService.upload(file, path);
        if (StringUtils.isBlank(targetFileName)) {
            resultMap.put("success", false);
            resultMap.put("msg", "上传失败");
            return resultMap;
        }
        String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;
        resultMap.put("success", true);
        resultMap.put("msg", "上传成功");
        resultMap.put("file_path", url);
        response.addHeader("Access-Control-Allow-Headers", "X-File-Name");
        return resultMap;
    }
}
