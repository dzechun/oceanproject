package com.fantechs.provider.guest.eng.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eng.EngContractQtyOrderAndPurOrderDto;
import com.fantechs.common.base.general.entity.eng.search.SearchEngContractQtyOrderAndPurOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.provider.guest.eng.service.EngContractQtyOrderAndPurOrderService;
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
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/09/01.
 */
@RestController
@Api(tags = "新合同量单信息")
@RequestMapping("/engContractQtyOrderAndPurOrder")
@Validated
public class EngContractQtyOrderAndPurOrderController {

    @Resource
    private EngContractQtyOrderAndPurOrderService engContractQtyOrderAndPurOrderService;

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<EngContractQtyOrderAndPurOrderDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchEngContractQtyOrderAndPurOrder searchEngContractQtyOrderAndPurOrder) {
        Page<Object> page = PageHelper.startPage(searchEngContractQtyOrderAndPurOrder.getStartPage(),searchEngContractQtyOrderAndPurOrder.getPageSize());
        List<EngContractQtyOrderAndPurOrderDto> list = engContractQtyOrderAndPurOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchEngContractQtyOrderAndPurOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }


    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchEngContractQtyOrderAndPurOrder searchEngContractQtyOrderAndPurOrder){
    List<EngContractQtyOrderAndPurOrderDto> list = engContractQtyOrderAndPurOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchEngContractQtyOrderAndPurOrder));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "EngContractQtyOrderAndPurOrder信息", EngContractQtyOrderAndPurOrderDto.class, "EngContractQtyOrderAndPurOrder.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

}
