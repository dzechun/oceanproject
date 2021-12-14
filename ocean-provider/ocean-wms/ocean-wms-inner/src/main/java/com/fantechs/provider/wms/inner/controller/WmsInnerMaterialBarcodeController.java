package com.fantechs.provider.wms.inner.controller;

import com.fantechs.common.base.general.dto.mes.sfc.LabelRuteDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerMaterialBarcodeDto;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerMaterialBarcode;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.inner.service.WmsInnerMaterialBarcodeService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/07/03.
 */
@RestController
@Api(tags = "来料打印")
@RequestMapping("/wmsInnerMaterialBarcode")
@Validated
public class WmsInnerMaterialBarcodeController {

    @Resource
    private WmsInnerMaterialBarcodeService wmsInnerMaterialBarcodeService;

    @ApiOperation(value = "批量修改",notes = "批量修改")
    @PostMapping("/batchUpdate")
    public ResponseEntity batchUpdate(@ApiParam(value = "必传：",required = true)@RequestBody @Validated @NotEmpty List<WmsInnerMaterialBarcodeDto> list) {
        return ControllerUtil.returnCRUD(wmsInnerMaterialBarcodeService.batchUpdate(list));
    }

    @ApiOperation(value = "生成条码",notes = "生成条码")
    @PostMapping("/add")
    public ResponseEntity<List<WmsInnerMaterialBarcodeDto>> add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated List<WmsInnerMaterialBarcodeDto> list,@ApiParam(value = "必传：打印类型",required = true)@RequestBody @Validated Integer type) {
        list = wmsInnerMaterialBarcodeService.add(list,type);
        return ControllerUtil.returnDataSuccess(list,StringUtils.isEmpty(list)?0:1);
    }

    @ApiOperation("规则解析及标签模版")
    @PostMapping("/findRule")
    public ResponseEntity<LabelRuteDto> findRule(@ApiParam(value = "barcodeRuleSetId",required = true)@RequestParam Long barcodeRuleSetId,@ApiParam(value = "materialId",required = true)@RequestParam Long materialId){
        LabelRuteDto labelRuteDto = wmsInnerMaterialBarcodeService.findLabelRute(barcodeRuleSetId,materialId);
        return ControllerUtil.returnDataSuccess(labelRuteDto,StringUtils.isEmpty(labelRuteDto)?0:1);
    }

    @ApiOperation("打印/补打")
    @PostMapping("/print")
    public ResponseEntity print(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids,@RequestParam int printQty){
        return ControllerUtil.returnCRUD(wmsInnerMaterialBarcodeService.print(ids,printQty));
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WmsInnerMaterialBarcodeDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInnerMaterialBarcode searchWmsInnerMaterialBarcode) {
        Page<Object> page = PageHelper.startPage(searchWmsInnerMaterialBarcode.getStartPage(),searchWmsInnerMaterialBarcode.getPageSize());
        List<WmsInnerMaterialBarcodeDto> list = wmsInnerMaterialBarcodeService.findList(searchWmsInnerMaterialBarcode);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }
}
