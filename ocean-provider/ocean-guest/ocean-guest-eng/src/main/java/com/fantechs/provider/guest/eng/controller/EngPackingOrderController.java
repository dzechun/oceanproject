package com.fantechs.provider.guest.eng.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eng.EngHtPackingOrderDto;
import com.fantechs.common.base.general.dto.eng.EngPackingOrderDto;
import com.fantechs.common.base.general.dto.eng.EngPackingOrderSummaryDetDto;
import com.fantechs.common.base.general.entity.eng.EngPackingOrder;
import com.fantechs.common.base.general.entity.eng.search.SearchEngPackingOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.guest.eng.service.EngHtPackingOrderService;
import com.fantechs.provider.guest.eng.service.EngPackingOrderService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/08/27.
 */
@RestController
@Api(tags = "装箱清单基础信息")
@RequestMapping("/engPackingOrder")
@Validated
public class EngPackingOrderController {

    @Resource
    private EngPackingOrderService engPackingOrderService;
    @Resource
    private EngHtPackingOrderService engHtPackingOrderService;

    @ApiOperation(value = "登记",notes = "登记")
    @PostMapping("/register")
    public ResponseEntity register(@ApiParam(value = "必传：",required = true)@RequestBody @Validated EngPackingOrder engPackingOrder) {
        return ControllerUtil.returnCRUD(engPackingOrderService.register(engPackingOrder));
    }

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated EngPackingOrder engPackingOrder) {
        return ControllerUtil.returnCRUD(engPackingOrderService.save(engPackingOrder));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(engPackingOrderService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=EngPackingOrder.update.class) EngPackingOrder engPackingOrder) {
        return ControllerUtil.returnCRUD(engPackingOrderService.update(engPackingOrder));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<EngPackingOrder> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        EngPackingOrder  engPackingOrder = engPackingOrderService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(engPackingOrder,StringUtils.isEmpty(engPackingOrder)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<EngPackingOrderDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchEngPackingOrder searchEngPackingOrder) {
        Page<Object> page = PageHelper.startPage(searchEngPackingOrder.getStartPage(),searchEngPackingOrder.getPageSize());
        List<EngPackingOrderDto> list = engPackingOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchEngPackingOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<EngHtPackingOrderDto>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchEngPackingOrder searchEngPackingOrder) {
        Page<Object> page = PageHelper.startPage(searchEngPackingOrder.getStartPage(),searchEngPackingOrder.getPageSize());
        List<EngHtPackingOrderDto> list = engHtPackingOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchEngPackingOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }


    @ApiOperation("提交")
    @PostMapping("/submit")
    public ResponseEntity submit(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=EngPackingOrder.update.class) EngPackingOrder engPackingOrder) {
        return ControllerUtil.returnCRUD(engPackingOrderService.submit(engPackingOrder));
    }


    @ApiOperation("审核")
    @PostMapping("/censor")
    public ResponseEntity censor(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=EngPackingOrder.update.class) EngPackingOrder engPackingOrder) {
        return ControllerUtil.returnCRUD(engPackingOrderService.censor(engPackingOrder));
    }


    @ApiOperation("校验超发")
    @PostMapping("/checkQty")
    public ResponseEntity<List<EngPackingOrderSummaryDetDto>> checkQty(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=EngPackingOrder.update.class) List<EngPackingOrderDto> engPackingOrderDtos) {
        List<EngPackingOrderSummaryDetDto> list = engPackingOrderService.checkQty(engPackingOrderDtos);
        return ControllerUtil.returnDataSuccess(list,list.size());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchEngPackingOrder searchEngPackingOrder){
    List<EngPackingOrderDto> list = engPackingOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchEngPackingOrder));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "EngPackingOrder信息", EngPackingOrderDto.class, "EngPackingOrder.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
