package com.fantechs.provider.wms.inner.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerStockOrderDetDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerStockOrderDto;
import com.fantechs.common.base.general.dto.wms.inner.imports.WmsInnerStockOrderImport;
import com.fantechs.common.base.general.dto.wms.out.imports.WmsOutDeliveryOrderImport;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerStockOrder;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerStockOrderDet;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerStockOrder;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerStockOrderDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.provider.wms.inner.service.WmsInnerStockOrderDetService;
import com.fantechs.provider.wms.inner.service.WmsInnerStockOrderService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * Created by Mr.Lei on 2021/05/27.
 */
@RestController
@Api(tags = "库存盘点")
@RequestMapping("/wmsInventoryVerification")
@Validated
@Slf4j
public class WmsInnerStockOrderController {

    @Resource
    private WmsInnerStockOrderService wmsInventoryVerificationService;
    @Resource
    private WmsInnerStockOrderDetService wmsInventoryVerificationDetService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WmsInnerStockOrder wmsInventoryVerification) {
        return ControllerUtil.returnCRUD(wmsInventoryVerificationService.save(wmsInventoryVerification));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(wmsInventoryVerificationService.batchDelete(ids));
    }

    @ApiOperation("修改/盘点登记接口")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value= WmsInnerStockOrder.update.class) WmsInnerStockOrder wmsInventoryVerification) {
        return ControllerUtil.returnCRUD(wmsInventoryVerificationService.update(wmsInventoryVerification));
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WmsInnerStockOrderDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInnerStockOrder searchWmsInventoryVerification) {
        Page<Object> page = PageHelper.startPage(searchWmsInventoryVerification.getStartPage(),searchWmsInventoryVerification.getPageSize());
        List<WmsInnerStockOrderDto> list = wmsInventoryVerificationService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInventoryVerification));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchWmsInnerStockOrder searchWmsInventoryVerification){
        List<WmsInnerStockOrderDto> list = wmsInventoryVerificationService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInventoryVerification));
        List<Long> longs = list.stream().map(WmsInnerStockOrderDto::getStockOrderId).collect(Collectors.toList());
        SearchWmsInnerStockOrderDet searchWmsInnerStockOrderDet = new SearchWmsInnerStockOrderDet();
        searchWmsInnerStockOrderDet.setStockIds(longs);
        List<WmsInnerStockOrderDetDto> detDtos = wmsInventoryVerificationDetService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInnerStockOrderDet));
        try {
            // 导出操作
            EasyPoiUtils.exportExcel(detDtos, "导出信息", "盘点单", WmsInnerStockOrderDetDto.class, "盘点单.xls", response);
            } catch (Exception e) {
            throw new BizErrorException(e);
            }
    }

    @ApiOperation("盘点单激活作废")
    @PostMapping("/activation")
    public ResponseEntity activation(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids,
                                     @ApiParam(value = "按钮类型 1-激活 2作废",required = true)@RequestParam Integer btnType){
        return ControllerUtil.returnCRUD(wmsInventoryVerificationService.activation(ids,btnType));
    }

    @ApiOperation("查看盘点记录/打印盘点单")
    @PostMapping("/findDetList")
    public ResponseEntity<List<WmsInnerStockOrderDetDto>> findDetList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInnerStockOrderDet searchWmsInventoryVerificationDet){
        Page<Object> page = PageHelper.startPage(searchWmsInventoryVerificationDet.getStartPage(),searchWmsInventoryVerificationDet.getPageSize());
        List<WmsInnerStockOrderDetDto> list = wmsInventoryVerificationDetService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInventoryVerificationDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("盘点确认")
    @PostMapping("/ascertained")
    public ResponseEntity ascertained(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids){
        return ControllerUtil.returnCRUD(wmsInventoryVerificationService.ascertained(ids));
    }

    @ApiOperation("差异处理")
    @PostMapping("/difference")
    public ResponseEntity difference(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids){
        return ControllerUtil.returnCRUD(wmsInventoryVerificationService.difference(ids));
    }

    /**
     * 从excel导入数据
     * @return
     * @throws
     */
    @PostMapping(value = "/import")
    @ApiOperation(value = "从excel导入信息",notes = "从excel导入信息")
    public ResponseEntity importExcel(@ApiParam(value ="输入excel文件",required = true)
                                      @RequestPart(value="file") MultipartFile file,
                                      @ApiParam(value = "盘点单Id",required = true)
                                      @RequestParam Long stockOrderId){
        try {
            // 导入操作
            List<WmsInnerStockOrderImport> wmsInnerStockOrderImports = EasyPoiUtils.importExcel(file, 0, 1, WmsInnerStockOrderImport.class);
            Map<String, Object> resultMap = wmsInventoryVerificationDetService.importExcel(stockOrderId,wmsInnerStockOrderImports);
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

    @PostMapping("/batchDeletlDet")
    @ApiOperation("批量删除明细")
    public ResponseEntity batchDeleteDet(@ApiParam(name = "ids",required = true)@RequestParam String ids){
        return ControllerUtil.returnCRUD(wmsInventoryVerificationDetService.batchDelete(ids));
    }
}
