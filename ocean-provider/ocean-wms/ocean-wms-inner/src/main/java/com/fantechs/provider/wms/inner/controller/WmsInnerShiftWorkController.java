package com.fantechs.provider.wms.inner.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerJobOrderDto;
import com.fantechs.common.base.general.dto.wms.inner.export.WmsInnerJobOrderExport;
import com.fantechs.common.base.general.dto.wms.inner.imports.WmsInnerJobOrderImport;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrder;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrderDet;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerJobOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.inner.service.WmsInnerShiftWorkService;
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
 * Created by Mr.Lei on 2021/05/06.
 */
@RestController
@Api(tags = "移位作业")
@RequestMapping("/wmsInnerShiftWork")
@Validated
@Slf4j
public class WmsInnerShiftWorkController {

    @Resource
    private WmsInnerShiftWorkService wmsInnerShiftWorkService;

    @ApiOperation("手动分配")
    @PostMapping("/handDistribution")
    public ResponseEntity handDistribution(@RequestBody List<WmsInnerJobOrderDet> list){
        return ControllerUtil.returnCRUD(wmsInnerShiftWorkService.handDistribution(list));
    }

    @ApiOperation("取消分配")
    @PostMapping("/cancelDistribution")
    public ResponseEntity cancelDistribution(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids){
        return ControllerUtil.returnCRUD(wmsInnerShiftWorkService.cancelDistribution(ids));
    }


    @ApiOperation("移位按条码单一确认")
    @PostMapping("/singleReceivingByBarcode")
    public ResponseEntity singleReceivingByBarcode(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WmsInnerJobOrderDet wmsInPutawayOrderDet,
                                                   @ApiParam(value = "条码ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids,
                                                    @ApiParam(value = "必传，单据类型，1-选择条码, 2-扫描条码",required = true) @RequestParam  Byte orderType){
        return ControllerUtil.returnCRUD(wmsInnerShiftWorkService.singleReceivingByBarcode(wmsInPutawayOrderDet,ids,orderType));
    }

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WmsInnerJobOrder wmsInPutawayOrder) {
        return ControllerUtil.returnCRUD(wmsInnerShiftWorkService.save(wmsInPutawayOrder));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(wmsInnerShiftWorkService.batchDelete(ids));
    }

    @ApiOperation("关闭单据")
    @PostMapping("/closeWmsInnerJobOrder")
    public ResponseEntity closeWmsInnerJobOrder(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(wmsInnerShiftWorkService.closeWmsInnerJobOrder(ids));
    }

    @ApiOperation("移位单批量删除")
    @PostMapping("/batchDeleteByShiftWork")
    public ResponseEntity batchDeleteByShiftWork(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(wmsInnerShiftWorkService.batchDeleteByShiftWork(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value= WmsInnerJobOrder.update.class) WmsInnerJobOrder wmsInPutawayOrder) {
        return ControllerUtil.returnCRUD(wmsInnerShiftWorkService.update(wmsInPutawayOrder));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<WmsInnerJobOrder> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id,
                                                   @ApiParam(value = "sourceSysOrderTypeCode",required = true)@RequestParam  @NotNull(message="id不能为空") String sourceSysOrderTypeCode) {
        WmsInnerJobOrder wmsInPutawayOrder = wmsInnerShiftWorkService.detail(id,sourceSysOrderTypeCode);
        return  ControllerUtil.returnDataSuccess(wmsInPutawayOrder,StringUtils.isEmpty(wmsInPutawayOrder)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WmsInnerJobOrderDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInnerJobOrder searchWmsInPutawayOrder) {
        Page<Object> page = PageHelper.startPage(searchWmsInPutawayOrder.getStartPage(),searchWmsInPutawayOrder.getPageSize());
        List<WmsInnerJobOrderDto> list = wmsInnerShiftWorkService.findList(searchWmsInPutawayOrder);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchWmsInnerJobOrder searchWmsInPutawayOrder,@ApiParam(value = "类型（1，上架作业 2，拣货作业）",required = true)@RequestParam  @NotNull(message="id不能为空") Byte type){
        String title = "上架作业单信息导出";
        if (type == 1) {
            searchWmsInPutawayOrder.setOrderTypeCode("IN-IWK");
        }else {
            searchWmsInPutawayOrder.setOrderTypeCode("OUT-IWK");
            title = "拣货作业单信息导出";
        }
        List<WmsInnerJobOrderExport> list = wmsInnerShiftWorkService.findExportList(ControllerUtil.dynamicConditionByEntity(searchWmsInPutawayOrder));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, title, title, WmsInnerJobOrderExport.class, title+".xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
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
                                      @RequestParam Long stockOrderId){
        try {
            // 导入操作
            List<WmsInnerJobOrderImport> wmsInnerJobOrderImports = EasyPoiUtils.importExcel(file, 0, 1, WmsInnerJobOrderImport.class);
            Map<String, Object> resultMap = wmsInnerShiftWorkService.importExcel(wmsInnerJobOrderImports);
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
}
