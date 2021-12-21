package com.fantechs.provider.wms.inner.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.mes.sfc.LabelRuteDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerMaterialBarcodeDto;
import com.fantechs.common.base.general.dto.wms.inner.imports.WmsInnerMaterialBarcodeImport;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerMaterialBarcode;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerMaterialBarcode;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.inner.service.WmsInnerMaterialBarcodeService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/07/03.
 */
@RestController
@Api(tags = "来料打印")
@RequestMapping("/wmsInnerMaterialBarcode")
@Validated
@Log4j
public class WmsInnerMaterialBarcodeController {

    @Resource
    private WmsInnerMaterialBarcodeService wmsInnerMaterialBarcodeService;

    @ApiOperation(value = "批量修改",notes = "批量修改")
    @PostMapping("/batchUpdate")
    public ResponseEntity batchUpdate(@ApiParam(value = "必传：",required = true)@RequestBody @Validated @NotEmpty List<WmsInnerMaterialBarcodeDto> list) {
        return ControllerUtil.returnCRUD(wmsInnerMaterialBarcodeService.batchUpdate(list));
    }

    @ApiOperation(value = "批量新增",notes = "批量新增")
    @PostMapping("/batchAdd")
    public ResponseEntity<List<WmsInnerMaterialBarcodeDto>> batchAdd(@ApiParam(value = "必传：",required = true)@RequestBody @Validated @NotEmpty List<WmsInnerMaterialBarcodeDto> list) {
        return ControllerUtil.returnDataSuccess(wmsInnerMaterialBarcodeService.batchAdd(list),StringUtils.isEmpty(list)?0:1);
    }

    @ApiOperation(value = "生成条码",notes = "生成条码")
    @PostMapping("/add")
    public ResponseEntity<List<WmsInnerMaterialBarcodeDto>> add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated List<WmsInnerMaterialBarcodeDto> list,@ApiParam(value = "必传：打印类型（1，ASN单 2，收货作业单 3，来料检验单 4，上架作业单）",required = true)@RequestParam Integer type) {
        list = wmsInnerMaterialBarcodeService.add(list,type);
        return ControllerUtil.returnDataSuccess(list,StringUtils.isEmpty(list)?0:1);
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(wmsInnerMaterialBarcodeService.batchDelete(ids));
    }

    @ApiOperation("规则解析及标签模版")
    @PostMapping("/findRule")
    public ResponseEntity<LabelRuteDto> findRule(@ApiParam(value = "barcodeRuleSetId",required = true)@RequestParam Long barcodeRuleSetId,@ApiParam(value = "materialId",required = true)@RequestParam Long materialId){
        LabelRuteDto labelRuteDto = wmsInnerMaterialBarcodeService.findLabelRute(barcodeRuleSetId,materialId);
        return ControllerUtil.returnDataSuccess(labelRuteDto,StringUtils.isEmpty(labelRuteDto)?0:1);
    }

    @ApiOperation("打印/补打")
    @PostMapping("/print")
    public ResponseEntity print(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids,@RequestParam int printQty,String printName,@RequestParam String printType,@ApiParam(value = "打印方式（0，打印 1，补打）",required = true) @RequestParam int printMode){
        return ControllerUtil.returnCRUD(wmsInnerMaterialBarcodeService.print(ids,printQty,printName,printType,printMode));
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WmsInnerMaterialBarcodeDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInnerMaterialBarcode searchWmsInnerMaterialBarcode) {
        Page<Object> page = PageHelper.startPage(searchWmsInnerMaterialBarcode.getStartPage(),searchWmsInnerMaterialBarcode.getPageSize());
        List<WmsInnerMaterialBarcodeDto> list = wmsInnerMaterialBarcodeService.findList(searchWmsInnerMaterialBarcode);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchWmsInnerMaterialBarcode searchWmsInnerMaterialBarcode){
        List<WmsInnerMaterialBarcodeDto> list = wmsInnerMaterialBarcodeService.findList(searchWmsInnerMaterialBarcode);
        try {
            // 导出操作
            EasyPoiUtils.exportExcel(list, "导出信息", "条码信息", WmsInnerMaterialBarcodeDto.class, "条码信息.xls", response);
        } catch (Exception e) {
            throw new BizErrorException(e);
        }
    }

    @PostMapping(value = "/import")
    @ApiOperation(value = "从excel导入",notes = "从excel导入")
    public ResponseEntity importExcel(@RequestBody WmsInnerMaterialBarcodeImport importDto,@ApiParam(value = "必传：打印类型（1，ASN单 2，收货作业单 3，来料检验单 4，上架作业单）",required = true)@RequestParam Integer type){
        try {
            // 导入操作
//            List<WmsInnerMaterialBarcodeImport> importList = EasyPoiUtils.importExcel(file, 2, 1, WmsInnerMaterialBarcodeImport.class);
            Map<String, Object> resultMap = wmsInnerMaterialBarcodeService.importExcel(importDto.getImportList(),importDto.getList(),type);
            return ControllerUtil.returnDataSuccess("操作结果集",resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.OPT20012002.getCode());
        }
    }

    @PostMapping(value = "/analysis")
    @ApiOperation(value = "从excel导入",notes = "从excel导入")
    public ResponseEntity<List<WmsInnerMaterialBarcodeImport>> analysis(@ApiParam(value ="输入excel文件",required = true) @RequestPart(value="file") MultipartFile file){
        try {
            // 导入操作
            List<WmsInnerMaterialBarcodeImport> importList = EasyPoiUtils.importExcel(file, 2, 1, WmsInnerMaterialBarcodeImport.class);
            return ControllerUtil.returnDataSuccess(importList,importList.size());
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.OPT20012002.getCode());
        }
    }

}
