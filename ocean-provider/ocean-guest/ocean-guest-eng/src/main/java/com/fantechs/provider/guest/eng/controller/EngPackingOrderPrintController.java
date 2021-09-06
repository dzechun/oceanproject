package com.fantechs.provider.guest.eng.controller;

import com.fantechs.common.base.general.dto.eng.EngPackingOrderPrintDto;
import com.fantechs.common.base.general.dto.eng.EngPackingOrderPrintParam;
import com.fantechs.common.base.general.entity.eng.search.SearchEngPackingOrderPrint;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.guest.eng.service.EngPackingOrderPrintService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author mr.lei
 * @Date 2021/9/4
 */
@RestController
@Api(tags = "装箱清单打印")
@RequestMapping("/engPackingOrderPrint")
@Validated
public class EngPackingOrderPrintController {
    @Resource
    private EngPackingOrderPrintService engPackingOrderPrintService;


    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<EngPackingOrderPrintDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchEngPackingOrderPrint searchEngPackingOrderPrint) {
        Page<Object> page = PageHelper.startPage(searchEngPackingOrderPrint.getStartPage(),searchEngPackingOrderPrint.getPageSize());
        List<EngPackingOrderPrintDto> list = engPackingOrderPrintService.findList(ControllerUtil.dynamicConditionByEntity(searchEngPackingOrderPrint));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("打印按钮")
    @PostMapping("/print")
    public ResponseEntity print(@RequestBody EngPackingOrderPrintParam engPackingOrderPrintParam){
        return ControllerUtil.returnCRUD(engPackingOrderPrintService.print(engPackingOrderPrintParam));
    }
}
