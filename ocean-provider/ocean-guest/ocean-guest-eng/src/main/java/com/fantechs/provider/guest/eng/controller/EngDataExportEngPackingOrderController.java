package com.fantechs.provider.guest.eng.controller;

import com.fantechs.common.base.general.dto.restapi.EngDataExportEngPackingOrderDto;
import com.fantechs.common.base.general.entity.eng.search.SearchEngContractQtyOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.guest.eng.service.EngDataExportEngPackingOrderService;
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
 *
 * Created by leifengzhi on 2021/09/01.
 */
@RestController
@Api(tags = "装箱清单确认回写")
@RequestMapping("/EngDataExportEngPackingOrder")
@Validated
public class EngDataExportEngPackingOrderController {

    @Resource
    private EngDataExportEngPackingOrderService engDataExportEngPackingOrderService;

    @ApiOperation("列表")
    @PostMapping("/findExportData")
    public ResponseEntity<List<EngDataExportEngPackingOrderDto>> findExportData(@ApiParam(value = "查询对象")@RequestBody SearchEngContractQtyOrder searchEngContractQtyOrder) {
        searchEngContractQtyOrder.setPageSize(1000);
        Page<Object> page = PageHelper.startPage(searchEngContractQtyOrder.getStartPage(),searchEngContractQtyOrder.getPageSize());
        List<EngDataExportEngPackingOrderDto> list = engDataExportEngPackingOrderService.findExportData(ControllerUtil.dynamicConditionByEntity(searchEngContractQtyOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表")
    @PostMapping("/writePackingLists")
    public ResponseEntity<String> writePackingLists() {
        String result = engDataExportEngPackingOrderService.writePackingLists();
        return ControllerUtil.returnDataSuccess(result,1);
    }


}
