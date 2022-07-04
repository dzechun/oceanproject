package com.fantechs.provider.wms.out.controller;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.out.WmsOutDeliveryOrderDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutTransferDeliveryOrderDto;
import com.fantechs.common.base.general.dto.wms.out.imports.WmsOutDeliveryOrderImport;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDeliveryOrder;
import com.fantechs.common.base.general.entity.wms.out.history.WmsOutHtDeliveryOrder;
import com.fantechs.common.base.general.entity.wms.out.search.SearchWmsOutDeliveryOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.out.service.WmsOutDeliveryOrderService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.seata.spring.annotation.GlobalTransactional;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/05/07.
 */
@RestController
@Api(tags = "销售出库控制器")
@RequestMapping("/wmsOutDeliveryOrder")
@Validated
@Slf4j
public class WmsOutDeliveryOrderController {

    @Resource
    private WmsOutDeliveryOrderService wmsOutDeliveryOrderService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WmsOutDeliveryOrder wmsOutDeliveryOrder) {
        return ControllerUtil.returnCRUD(wmsOutDeliveryOrderService.save(wmsOutDeliveryOrder));
    }

    @ApiOperation(value = "批量新增",notes = "批量新增")
    @PostMapping("/addList")
    @Transactional(rollbackFor = Exception.class)
    @GlobalTransactional
    public ResponseEntity addList(@ApiParam(value = "销售出库单信息集合") @RequestBody List<WmsOutDeliveryOrder> outDeliveryOrders){
        return ControllerUtil.returnCRUD(wmsOutDeliveryOrderService.batchSave(outDeliveryOrders));
    }

    @ApiOperation(value = "创建作业单",notes = "创建作业单")
    @PostMapping("/createJobOrder")
    public ResponseEntity createJobOrder(@ApiParam(value = "ID",required = true)@RequestParam @NotNull(message="id不能为空") Long id,@RequestParam(required = false) Long platformId) {
        return ControllerUtil.returnCRUD(wmsOutDeliveryOrderService.createJobOrder(id,platformId));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(wmsOutDeliveryOrderService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=WmsOutDeliveryOrder.update.class) WmsOutDeliveryOrder wmsOutDeliveryOrder) {
        return ControllerUtil.returnCRUD(wmsOutDeliveryOrderService.update(wmsOutDeliveryOrder));
    }

    @ApiOperation("修改审核状态")
    @PostMapping("/updateStatus")
    public ResponseEntity updateStatus(@ApiParam(value = "对象ID列表",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids){
        String[] split = ids.split(",");
        List<Long> list = new ArrayList<>(split.length);
        for (String item : split){
            list.add(Long.decode(item));
        }
        return ControllerUtil.returnCRUD(wmsOutDeliveryOrderService.updateStatus(list));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<WmsOutDeliveryOrder> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        WmsOutDeliveryOrder  wmsOutDeliveryOrder = wmsOutDeliveryOrderService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(wmsOutDeliveryOrder,StringUtils.isEmpty(wmsOutDeliveryOrder)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WmsOutDeliveryOrderDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWmsOutDeliveryOrder searchWmsOutDeliveryOrder) {
        Page<Object> page = PageHelper.startPage(searchWmsOutDeliveryOrder.getStartPage(),searchWmsOutDeliveryOrder.getPageSize());
        List<WmsOutDeliveryOrderDto> list = wmsOutDeliveryOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsOutDeliveryOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("调拨出库单列表")
    @PostMapping("/transferFindList")
    public ResponseEntity<List<WmsOutTransferDeliveryOrderDto>> transferFindList(@ApiParam(value = "查询对象")@RequestBody SearchWmsOutDeliveryOrder searchWmsOutDeliveryOrder) {
        Page<Object> page = PageHelper.startPage(searchWmsOutDeliveryOrder.getStartPage(),searchWmsOutDeliveryOrder.getPageSize());
        List<WmsOutTransferDeliveryOrderDto> list = wmsOutDeliveryOrderService.transferFindList(ControllerUtil.dynamicConditionByEntity(searchWmsOutDeliveryOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("调拨出库单历史列表")
    @PostMapping("/transferFindHtList")
    public ResponseEntity<List<WmsOutTransferDeliveryOrderDto>> transferFindHtList(@ApiParam(value = "查询对象")@RequestBody SearchWmsOutDeliveryOrder searchWmsOutDeliveryOrder) {
        Page<Object> page = PageHelper.startPage(searchWmsOutDeliveryOrder.getStartPage(),searchWmsOutDeliveryOrder.getPageSize());
        List<WmsOutTransferDeliveryOrderDto> list = wmsOutDeliveryOrderService.transferFindHtList(ControllerUtil.dynamicConditionByEntity(searchWmsOutDeliveryOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<WmsOutHtDeliveryOrder>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchWmsOutDeliveryOrder searchWmsOutDeliveryOrder) {
        Page<Object> page = PageHelper.startPage(searchWmsOutDeliveryOrder.getStartPage(),searchWmsOutDeliveryOrder.getPageSize());
        List<WmsOutHtDeliveryOrder> list = wmsOutDeliveryOrderService.findHtList(ControllerUtil.dynamicConditionByEntity(searchWmsOutDeliveryOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchWmsOutDeliveryOrder searchWmsOutDeliveryOrder){
    List<WmsOutDeliveryOrderDto> list = wmsOutDeliveryOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsOutDeliveryOrder));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "销售出库单信息", WmsOutDeliveryOrderDto.class, "销售出库单信息.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

    @PostMapping(value = "/transferExport")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void transferExportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchWmsOutDeliveryOrder searchWmsOutDeliveryOrder){
        List<WmsOutTransferDeliveryOrderDto> list = wmsOutDeliveryOrderService.transferFindList(ControllerUtil.dynamicConditionByEntity(searchWmsOutDeliveryOrder));
        try {
            // 导出操作
            EasyPoiUtils.exportExcel(list, "导出信息", "调拨出库单信息", WmsOutTransferDeliveryOrderDto.class, "调拨出库单信息.xls", response);
        } catch (Exception e) {
            throw new BizErrorException(e);
        }
    }

    @ApiOperation("发运反写出库单状态")
    @PostMapping("/forwardingStatus")
    public ResponseEntity forwardingStatus(@RequestParam Long deliverOrderId,@RequestParam Byte orderStatus){
        return ControllerUtil.returnCRUD(wmsOutDeliveryOrderService.forwardingStatus(deliverOrderId,orderStatus));
    }

    @ApiOperation("api增加领料出库单信息")
    @PostMapping("/saveByApi")
    public ResponseEntity saveByApi(@ApiParam(value = "对象，deliveryOrderCode必传",required = true)@RequestBody @Validated WmsOutDeliveryOrder wmsOutDeliveryOrder) {
        return ControllerUtil.returnCRUD(wmsOutDeliveryOrderService.saveByApi(wmsOutDeliveryOrder));
    }

    /**
     * 从excel导入数据
     * @return
     * @throws
     */
    @PostMapping(value = "/import")
    @ApiOperation(value = "从excel导入信息",notes = "从excel导入信息")
    public ResponseEntity importExcel(@ApiParam(value ="输入excel文件",required = true)
                                      @RequestPart(value="file") MultipartFile file){
        try {
            // 导入操作
            List<WmsOutDeliveryOrderImport> wmsOutDeliveryOrderImports = EasyPoiUtils.importExcel(file, 0, 1, WmsOutDeliveryOrderImport.class);
            Map<String, Object> resultMap = wmsOutDeliveryOrderService.importExcel(wmsOutDeliveryOrderImports);
            return ControllerUtil.returnDataSuccess("操作结果集", resultMap);
        }catch (RuntimeException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ControllerUtil.returnFail("文件格式错误", ErrorCodeEnum.OPT20012002.getCode());
        }catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.OPT20012002.getCode());
        }
    }

    @ApiOperation(value = "领料出库单封单反写")
    @PostMapping("/sealOrder")
    public ResponseEntity sealOrder(@RequestParam Long deliveryOrderId){
        return ControllerUtil.returnCRUD(wmsOutDeliveryOrderService.sealOrder(deliveryOrderId));
    }

    @ApiOperation(value = "领料出库单封单回写接口")
    @PostMapping("/overIssue")
    public ResponseEntity overIssue(@RequestParam Long deliveryOrderId){
        return ControllerUtil.returnCRUD(wmsOutDeliveryOrderService.overIssue(deliveryOrderId));
    }
}
