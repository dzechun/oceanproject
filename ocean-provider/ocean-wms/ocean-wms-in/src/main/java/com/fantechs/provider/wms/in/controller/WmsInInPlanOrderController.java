package com.fantechs.provider.wms.in.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.in.WmsInHtInPlanOrderDto;
import com.fantechs.common.base.general.dto.wms.in.WmsInInPlanOrderDto;
import com.fantechs.common.base.general.dto.wms.in.imports.WmsInInPlanOrderImport;
import com.fantechs.common.base.general.entity.wms.in.WmsInInPlanOrder;
import com.fantechs.common.base.general.entity.wms.in.search.SearchWmsInInPlanOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.in.service.WmsInHtInPlanOrderService;
import com.fantechs.provider.wms.in.service.WmsInInPlanOrderService;
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
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/12/08.
 */
@RestController
@Api(tags = "入库计划")
@RequestMapping("/wmsInInPlanOrder")
@Validated
@Slf4j
public class WmsInInPlanOrderController {

    @Resource
    private WmsInInPlanOrderService wmsInInPlanOrderService;
    @Resource
    private WmsInHtInPlanOrderService wmsInHtInPlanOrderService;


    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：warehouseId",required = true)@RequestBody @Validated WmsInInPlanOrderDto wmsInInPlanOrderDto) {
        return ControllerUtil.returnCRUD(wmsInInPlanOrderService.save(wmsInInPlanOrderDto));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(wmsInInPlanOrderService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=WmsInInPlanOrder.update.class) WmsInInPlanOrderDto wmsInInPlanOrderDto) {
        return ControllerUtil.returnCRUD(wmsInInPlanOrderService.update(wmsInInPlanOrderDto));
    }

    @ApiOperation("关闭订单")
    @PostMapping("/close")
    public ResponseEntity close(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(wmsInInPlanOrderService.close(ids));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<WmsInInPlanOrder> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        WmsInInPlanOrder  wmsInInPlanOrder = wmsInInPlanOrderService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(wmsInInPlanOrder,StringUtils.isEmpty(wmsInInPlanOrder)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WmsInInPlanOrderDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInInPlanOrder searchWmsInInPlanOrder) {
        Page<Object> page = PageHelper.startPage(searchWmsInInPlanOrder.getStartPage(),searchWmsInInPlanOrder.getPageSize());
        List<WmsInInPlanOrderDto> list = wmsInInPlanOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInInPlanOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<WmsInInPlanOrderDto>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchWmsInInPlanOrder searchWmsInInPlanOrder) {
        List<WmsInInPlanOrderDto> list = wmsInInPlanOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInInPlanOrder));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<WmsInHtInPlanOrderDto>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInInPlanOrder searchWmsInInPlanOrder) {
        Page<Object> page = PageHelper.startPage(searchWmsInInPlanOrder.getStartPage(),searchWmsInInPlanOrder.getPageSize());
        List<WmsInHtInPlanOrderDto> list = wmsInHtInPlanOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInInPlanOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation(value = "下推",notes = "下推")
    @PostMapping("/pushDown")
    public ResponseEntity pushDown(@ApiParam(value = "来料检验单ID列表，多个逗号分隔",required = true)@RequestParam  @NotBlank(message="来料检验单ID不能为空") String ids) {
        return ControllerUtil.returnCRUD(wmsInInPlanOrderService.pushDown(ids));
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchWmsInInPlanOrder searchWmsInInPlanOrder){
    List<WmsInInPlanOrderDto> list = wmsInInPlanOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInInPlanOrder));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "WmsInInPlanOrder信息", WmsInInPlanOrderDto.class, "WmsInInPlanOrder.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

    @PostMapping(value = "/import")
    @ApiOperation(value = "从excel导入",notes = "从excel导入")
    public ResponseEntity importExcel(@ApiParam(value ="输入excel文件",required = true) @RequestPart(value="file") MultipartFile file){
        try {
            // 导入操作
            List<WmsInInPlanOrderImport> wmsInInPlanOrderImports = EasyPoiUtils.importExcel(file, 0, 1, WmsInInPlanOrderImport.class);
            Map<String, Object> resultMap = wmsInInPlanOrderService.importExcel(wmsInInPlanOrderImports);
            return ControllerUtil.returnDataSuccess("操作结果集",resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.OPT20012002.getCode());
        }
    }

    @ApiOperation("更新入库计划上架数量")
    @PostMapping("/updatePutawayQty")
    public ResponseEntity updatePutawayQty(@ApiParam(value = "必传明细ID",required = true)@RequestParam Long inPlanOrderDetId, @ApiParam(value = "必传上架数量",required = true)@RequestParam BigDecimal putawayQty) {
        return ControllerUtil.returnCRUD(wmsInInPlanOrderService.updatePutawayQty(inPlanOrderDetId,putawayQty));
    }

}
