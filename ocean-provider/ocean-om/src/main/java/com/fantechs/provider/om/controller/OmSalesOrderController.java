package com.fantechs.provider.om.controller;

import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.om.OmHtSalesOrderDto;
import com.fantechs.common.base.general.dto.om.OmSalesOrderDetDto;
import com.fantechs.common.base.general.dto.om.OmSalesOrderDto;
import com.fantechs.common.base.general.dto.om.SearchOmSalesOrderDto;
import com.fantechs.common.base.general.entity.om.OmSalesOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.om.service.OmSalesOrderService;
import com.fantechs.provider.om.service.ht.OmHtSalesOrderService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/04/19.
 */
@RestController
@Api(tags = "销售订单管理")
@RequestMapping("/omSalesOrder")
@Validated
public class OmSalesOrderController {

    @Resource
    private OmSalesOrderService omSalesOrderService;
    @Resource
    private OmHtSalesOrderService omHtSalesOrderService;

    @ApiOperation(value = "下推",notes = "下推")
    @PostMapping("/pushDown")
    public ResponseEntity pushDown(@ApiParam(value = "必传：",required = true)@RequestBody @Validated @NotEmpty List<OmSalesOrderDetDto> omSalesOrderDetDtoList) {
        return ControllerUtil.returnCRUD(omSalesOrderService.pushDown(omSalesOrderDetDtoList));
    }

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated OmSalesOrderDto omSalesOrderDto) {
        return ControllerUtil.returnCRUD(omSalesOrderService.save(omSalesOrderDto));
    }

    @ApiOperation(value = "批量新增",notes = "批量新增")
    @PostMapping("/addList")
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public ResponseEntity addList(@ApiParam(value = "销售订单信息集合") @RequestBody List<OmSalesOrder> salesOrders){
        return ControllerUtil.returnCRUD(omSalesOrderService.batchSave(salesOrders));
    }

    @ApiOperation(value = "下发仓库",notes = "下发仓库")
    @PostMapping("/issueWarehouse")
    public ResponseEntity issueWarehouse(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        return ControllerUtil.returnCRUD(omSalesOrderService.issueWarehouse(id));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(omSalesOrderService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=OmSalesOrder.update.class) OmSalesOrderDto omSalesOrderDto) {
        return ControllerUtil.returnCRUD(omSalesOrderService.update(omSalesOrderDto));
    }

    @ApiOperation("批量修改")
    @PostMapping("/batchUpdate")
    public ResponseEntity batchUpdate(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=OmSalesOrder.update.class) List<OmSalesOrder> salesOrders) {
        return ControllerUtil.returnCRUD(omSalesOrderService.batchUpdate(salesOrders));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<OmSalesOrderDto> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        Map<String, Object> map = new HashMap<>();
        map.put("salesOrderId", id);
        List<OmSalesOrderDto> omSalesOrderDtoList = omSalesOrderService.findList(map);
        return  ControllerUtil.returnDataSuccess(omSalesOrderDtoList.get(0),StringUtils.isEmpty(omSalesOrderDtoList.get(0))?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<OmSalesOrderDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchOmSalesOrderDto searchOmSalesOrderDto) {
        Page<Object> page = PageHelper.startPage(searchOmSalesOrderDto.getStartPage(),searchOmSalesOrderDto.getPageSize());
        List<OmSalesOrderDto> list = omSalesOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchOmSalesOrderDto));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表")
    @PostMapping("/findAll")
    public ResponseEntity<List<OmSalesOrderDto>> findAll() {
        List<OmSalesOrderDto> list = omSalesOrderService.findAll();
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<OmHtSalesOrderDto>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchOmSalesOrderDto searchOmSalesOrder) {
        Page<Object> page = PageHelper.startPage(searchOmSalesOrder.getStartPage(),searchOmSalesOrder.getPageSize());
        List<OmHtSalesOrderDto> list = omHtSalesOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchOmSalesOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchOmSalesOrderDto searchOmSalesOrder){
    List<OmSalesOrderDto> list = omSalesOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchOmSalesOrder));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "销售订单信息", "OmSalesOrder信息", OmSalesOrderDto.class, "OmSalesOrder.xls", response);
        } catch (Exception e) {
//            e.printStackTrace();
            throw new BizErrorException(e);
        }
    }
}
