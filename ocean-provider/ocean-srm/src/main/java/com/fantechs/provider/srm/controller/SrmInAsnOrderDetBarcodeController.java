package com.fantechs.provider.srm.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.srm.SrmInAsnOrderDetBarcodeDto;
import com.fantechs.common.base.general.dto.srm.SrmInHtAsnOrderDetBarcodeDto;
import com.fantechs.common.base.general.dto.srm.imports.SrmInAsnOrderDetBarcodeImport;
import com.fantechs.common.base.general.entity.srm.SrmInAsnOrderDetBarcode;
import com.fantechs.common.base.general.entity.srm.search.SearchSrmInAsnOrderDetBarcode;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.srm.service.SrmInAsnOrderDetBarcodeService;
import com.fantechs.provider.srm.service.SrmInHtAsnOrderDetBarcodeService;
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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/11/25.
 */
@RestController
@Api(tags = "预收货通知单条码")
@RequestMapping("/srmInAsnOrderDetBarcode")
@Validated
@Slf4j
public class SrmInAsnOrderDetBarcodeController {

    @Resource
    private SrmInAsnOrderDetBarcodeService srmInAsnOrderDetBarcodeService;
    @Resource
    private SrmInHtAsnOrderDetBarcodeService srmInHtAsnOrderDetBarcodeService;
    
    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated SrmInAsnOrderDetBarcode wmsInAsnOrderDetBarcode) {
        return ControllerUtil.returnCRUD(srmInAsnOrderDetBarcodeService.save(wmsInAsnOrderDetBarcode));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(srmInAsnOrderDetBarcodeService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=SrmInAsnOrderDetBarcode.update.class) SrmInAsnOrderDetBarcode wmsInAsnOrderDetBarcode) {
        return ControllerUtil.returnCRUD(srmInAsnOrderDetBarcodeService.update(wmsInAsnOrderDetBarcode));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SrmInAsnOrderDetBarcode> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        SrmInAsnOrderDetBarcode  wmsInAsnOrderDetBarcode = srmInAsnOrderDetBarcodeService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(wmsInAsnOrderDetBarcode,StringUtils.isEmpty(wmsInAsnOrderDetBarcode)?0:1);
    }


    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SrmInAsnOrderDetBarcodeDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSrmInAsnOrderDetBarcode searchSrmInAsnOrderDetBarcode) {
        Page<Object> page = PageHelper.startPage(searchSrmInAsnOrderDetBarcode.getStartPage(),searchSrmInAsnOrderDetBarcode.getPageSize());
        List<SrmInAsnOrderDetBarcodeDto> list = srmInAsnOrderDetBarcodeService.findList(ControllerUtil.dynamicConditionByEntity(searchSrmInAsnOrderDetBarcode));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<SrmInAsnOrderDetBarcodeDto>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchSrmInAsnOrderDetBarcode searchSrmInAsnOrderDetBarcode) {
        List<SrmInAsnOrderDetBarcodeDto> list = srmInAsnOrderDetBarcodeService.findList(ControllerUtil.dynamicConditionByEntity(searchSrmInAsnOrderDetBarcode));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<SrmInHtAsnOrderDetBarcodeDto>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchSrmInAsnOrderDetBarcode searchSrmInAsnOrderDetBarcode) {
        Page<Object> page = PageHelper.startPage(searchSrmInAsnOrderDetBarcode.getStartPage(),searchSrmInAsnOrderDetBarcode.getPageSize());
        List<SrmInHtAsnOrderDetBarcodeDto> list = srmInHtAsnOrderDetBarcodeService.findList(ControllerUtil.dynamicConditionByEntity(searchSrmInAsnOrderDetBarcode));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }
    
    @PostMapping(value = "/import")
    @ApiOperation(value = "从excel导入",notes = "从excel导入")
    public ResponseEntity importExcel(@ApiParam(value ="输入excel文件",required = true) @RequestPart(value="file") MultipartFile file,
                                      @ApiParam(value = "装箱汇总ID",required = true)@RequestParam  @NotNull(message="装箱汇总ID不能为空") Long asnOrderId){
        try {
            // 导入操作
            List<SrmInAsnOrderDetBarcodeImport> srmInAsnOrderDetBarcodeImports = EasyPoiUtils.importExcel(file, 0, 1, SrmInAsnOrderDetBarcodeImport.class);
            Map<String, Object> resultMap = srmInAsnOrderDetBarcodeService.importExcel(srmInAsnOrderDetBarcodeImports,asnOrderId);
            return ControllerUtil.returnDataSuccess("操作结果集",resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.OPT20012002.getCode());
        }
    }

}
