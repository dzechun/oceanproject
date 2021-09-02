package com.fantechs.provider.materialapi.imes.controller;

import com.fantechs.common.base.general.dto.restapi.SearchSapSupplierApi;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.materialapi.imes.service.SapSupplierApiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.ParseException;

/**
 *
 * Created by leifengzhi on 2021/05/31.
 */
@RestController
@Api(tags = "雷赛供应商信息接口")
@RequestMapping("/supplierApi")
@Validated
public class SapSupplierApiController {

    @Resource
    private SapSupplierApiService sapSupplierApiService;

    @ApiOperation(value = "请求雷赛供应商信息",notes = "请求雷赛供应商信息")
    @PostMapping("/getSupplier")
    public ResponseEntity getSupplier(@ApiParam(value = "查询对象")@RequestBody SearchSapSupplierApi searchSapSupplierApi) throws ParseException {
        return ControllerUtil.returnCRUD(sapSupplierApiService.getSupplier(searchSapSupplierApi));
    }

    @ApiOperation(value = "请求雷赛全部供应商信息",notes = "请求雷赛全部供应商信息")
    @PostMapping("/getSuppliers")
    public ResponseEntity getSuppliers() {
        return ControllerUtil.returnCRUD(sapSupplierApiService.getSuppliers());
    }
}
