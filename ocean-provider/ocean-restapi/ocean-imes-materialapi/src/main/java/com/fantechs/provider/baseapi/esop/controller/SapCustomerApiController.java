package com.fantechs.provider.baseapi.esop.controller;

import com.fantechs.common.base.general.dto.restapi.SearchSapSupplierApi;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.baseapi.esop.service.SapCustomerApiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 *
 * Created by leifengzhi on 2021/05/31.
 */
@RestController
@Api(tags = "雷赛客户信息接口")
@RequestMapping("/customerApi")
@Validated
public class SapCustomerApiController {

    @Resource
    private SapCustomerApiService sapCustomerApiService;

    @ApiOperation(value = "请求雷赛客户信息",notes = "请求雷赛客户信息")
    @PostMapping("/getCustomer")
    public ResponseEntity getSupplier(@ApiParam(value = "查询对象")@RequestBody SearchSapSupplierApi searchSapSupplierApi) {
        return ControllerUtil.returnCRUD(sapCustomerApiService.getCustomer(searchSapSupplierApi));
    }
}
