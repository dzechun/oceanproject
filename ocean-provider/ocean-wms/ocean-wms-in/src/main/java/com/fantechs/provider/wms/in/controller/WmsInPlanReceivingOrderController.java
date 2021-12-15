package com.fantechs.provider.wms.in.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.in.WmsInHtPlanReceivingOrderDto;
import com.fantechs.common.base.general.dto.wms.in.WmsInPlanReceivingOrderDto;
import com.fantechs.common.base.general.dto.wms.in.imports.WmsInPlanReceivingOrderImport;
import com.fantechs.common.base.general.entity.wms.in.WmsInPlanReceivingOrder;
import com.fantechs.common.base.general.entity.wms.in.search.SearchWmsInPlanReceivingOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.in.service.WmsInHtPlanReceivingOrderService;
import com.fantechs.provider.wms.in.service.WmsInPlanReceivingOrderService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by mr.lei on 2021/12/13.
 */
@RestController
@Api(tags = "收货计划")
@RequestMapping("/wmsInPlanReceivingOrder")
@Validated
@Slf4j
public class WmsInPlanReceivingOrderController {

    @Resource
    private WmsInPlanReceivingOrderService wmsInPlanReceivingOrderService;
    @Resource
    private WmsInHtPlanReceivingOrderService wmsInHtPlanReceivingOrderService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WmsInPlanReceivingOrder wmsInPlanReceivingOrder) {
        return ControllerUtil.returnCRUD(wmsInPlanReceivingOrderService.save(wmsInPlanReceivingOrder));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(wmsInPlanReceivingOrderService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=WmsInPlanReceivingOrder.update.class) WmsInPlanReceivingOrder wmsInPlanReceivingOrder) {
        return ControllerUtil.returnCRUD(wmsInPlanReceivingOrderService.update(wmsInPlanReceivingOrder));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<WmsInPlanReceivingOrder> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        WmsInPlanReceivingOrder  wmsInPlanReceivingOrder = wmsInPlanReceivingOrderService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(wmsInPlanReceivingOrder,StringUtils.isEmpty(wmsInPlanReceivingOrder)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WmsInPlanReceivingOrderDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInPlanReceivingOrder searchWmsInPlanReceivingOrder) {
        Page<Object> page = PageHelper.startPage(searchWmsInPlanReceivingOrder.getStartPage(),searchWmsInPlanReceivingOrder.getPageSize());
        List<WmsInPlanReceivingOrderDto> list = wmsInPlanReceivingOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInPlanReceivingOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<WmsInPlanReceivingOrderDto>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchWmsInPlanReceivingOrder searchWmsInPlanReceivingOrder) {
        List<WmsInPlanReceivingOrderDto> list = wmsInPlanReceivingOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInPlanReceivingOrder));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<WmsInHtPlanReceivingOrderDto>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInPlanReceivingOrder searchWmsInPlanReceivingOrder) {
        Page<Object> page = PageHelper.startPage(searchWmsInPlanReceivingOrder.getStartPage(),searchWmsInPlanReceivingOrder.getPageSize());
        List<WmsInHtPlanReceivingOrderDto> list = wmsInHtPlanReceivingOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInPlanReceivingOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation(value = "下推",notes = "下推")
    @PostMapping("/pushDown")
    public ResponseEntity pushDown(@ApiParam(value = "",required = true)@RequestParam  @NotBlank(message="收货计划明细ID不能为空")String ids) {
        return ControllerUtil.returnCRUD(wmsInPlanReceivingOrderService.pushDown(ids));
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchWmsInPlanReceivingOrder searchWmsInPlanReceivingOrder){
    List<WmsInPlanReceivingOrderDto> list = wmsInPlanReceivingOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInPlanReceivingOrder));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "收获计划", WmsInPlanReceivingOrderDto.class, "收获计划.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

    @PostMapping(value = "/import")
    @ApiOperation(value = "从excel导入",notes = "从excel导入")
    public ResponseEntity importExcel(@ApiParam(value ="输入excel文件",required = true) @RequestPart(value="file") MultipartFile file){
        try {
            // 导入操作
            List<WmsInPlanReceivingOrderImport> baseAddressImports = EasyPoiUtils.importExcel(file, 0, 1, WmsInPlanReceivingOrderImport.class);
            Map<String, Object> resultMap = wmsInPlanReceivingOrderService.importExcel(baseAddressImports);
            return ControllerUtil.returnDataSuccess("操作结果集",resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.OPT20012002.getCode());
        }
    }
}
