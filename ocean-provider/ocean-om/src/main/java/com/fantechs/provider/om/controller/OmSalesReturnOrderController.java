package com.fantechs.provider.om.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.om.OmHtSalesReturnOrderDto;
import com.fantechs.common.base.general.dto.om.OmSalesReturnOrderDto;
import com.fantechs.common.base.general.entity.om.OmSalesReturnOrder;
import com.fantechs.common.base.general.entity.om.OmSalesReturnOrderDet;
import com.fantechs.common.base.general.entity.om.search.SearchOmSalesReturnOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.om.service.OmSalesReturnOrderService;
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
 * Created by leifengzhi on 2021/06/21.
 */
@RestController
@Api(tags = "销退订单")
@RequestMapping("/omSalesReturnOrder")
@Validated
public class OmSalesReturnOrderController {

    @Resource
    private OmSalesReturnOrderService omSalesReturnOrderService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated OmSalesReturnOrder omSalesReturnOrder) {
        return ControllerUtil.returnCRUD(omSalesReturnOrderService.save(omSalesReturnOrder));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(omSalesReturnOrderService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=OmSalesReturnOrder.update.class) OmSalesReturnOrder omSalesReturnOrder) {
        return ControllerUtil.returnCRUD(omSalesReturnOrderService.update(omSalesReturnOrder));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<OmSalesReturnOrder> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        OmSalesReturnOrder  omSalesReturnOrder = omSalesReturnOrderService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(omSalesReturnOrder,StringUtils.isEmpty(omSalesReturnOrder)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<OmSalesReturnOrderDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchOmSalesReturnOrder searchOmSalesReturnOrder) {
        Page<Object> page = PageHelper.startPage(searchOmSalesReturnOrder.getStartPage(),searchOmSalesReturnOrder.getPageSize());
        List<OmSalesReturnOrderDto> list = omSalesReturnOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchOmSalesReturnOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<OmHtSalesReturnOrderDto>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchOmSalesReturnOrder searchOmSalesReturnOrder) {
        Page<Object> page = PageHelper.startPage(searchOmSalesReturnOrder.getStartPage(),searchOmSalesReturnOrder.getPageSize());
        List<OmHtSalesReturnOrderDto> list = omSalesReturnOrderService.findHtList(ControllerUtil.dynamicConditionByEntity(searchOmSalesReturnOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchOmSalesReturnOrder searchOmSalesReturnOrder){
    List<OmSalesReturnOrderDto> list = omSalesReturnOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchOmSalesReturnOrder));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "OmSalesReturnOrder信息", OmSalesReturnOrderDto.class, "OmSalesReturnOrder.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

/*    @ApiOperation("下发生成出库单")
    @PostMapping("/packageAutoOutOrder")
    public ResponseEntity packageAutoOutOrder(@RequestBody(required = true)OmSalesReturnOrder omSalesReturnOrder){
        return ControllerUtil.returnCRUD(omSalesReturnOrderService.packageAutoOutOrder(omSalesReturnOrder));
    }*/

    @PostMapping("/writeQty")
    public ResponseEntity writeQty(@RequestBody OmSalesReturnOrderDet omSalesReturnOrderDet){
        return ControllerUtil.returnCRUD(omSalesReturnOrderService.writeQty(omSalesReturnOrderDet));
    }
}
