package com.fantechs.provider.wms.inner.controller;

import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInitStockDetDto;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerInitStockDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.wms.inner.service.WmsInnerInitStockDetService;
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
@Api(tags = "初始化盘点明细")
@RequestMapping("/wmsInnerInitStockDet")
@Validated
public class WmsInnerInitStockDetController {

    @Resource
    private WmsInnerInitStockDetService wmsInnerInitStockDetService;

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WmsInnerInitStockDetDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInnerInitStockDet searchWmsInnerInitStockDet) {
        Page<Object> page = PageHelper.startPage(searchWmsInnerInitStockDet.getStartPage(),searchWmsInnerInitStockDet.getPageSize());
        List<WmsInnerInitStockDetDto> list = wmsInnerInitStockDetService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInnerInitStockDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }
}
