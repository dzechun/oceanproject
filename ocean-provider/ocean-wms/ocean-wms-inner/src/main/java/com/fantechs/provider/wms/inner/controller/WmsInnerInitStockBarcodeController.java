package com.fantechs.provider.wms.inner.controller;

import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInitStockBarcode;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerInitStockBarcode;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.wms.inner.service.WmsInnerInitStockBarcodeService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author mr.lei
 * @Date 2021/12/1
 */
@RestController
@Api(tags = "初始化盘点明细条码")
@RequestMapping("/wmsInnerInitStockBarcode")
@Validated
public class WmsInnerInitStockBarcodeController {

    @Resource
    private WmsInnerInitStockBarcodeService wmsInnerInitStockBarcodeService;

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WmsInnerInitStockBarcode>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInnerInitStockBarcode searchWmsInnerInitStockBarcode) {
//        Page<Object> page = PageHelper.startPage(searchWmsInnerInitStockBarcode.getStartPage(),searchWmsInnerInitStockBarcode.getPageSize());
        List<WmsInnerInitStockBarcode> list = wmsInnerInitStockBarcodeService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInnerInitStockBarcode));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }
}
