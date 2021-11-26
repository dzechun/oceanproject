package com.fantechs.provider.srm.controller;

import com.fantechs.common.base.general.entity.srm.SrmInAsnOrderDetBarcode;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.srm.service.SrmInAsnOrderDetBarcodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 *
 * Created by leifengzhi on 2021/11/25.
 */
@RestController
@Api(tags = "wmsInAsnOrderDetBarcode控制器")
@RequestMapping("/wmsInAsnOrderDetBarcode")
@Validated
public class SrmInAsnOrderDetBarcodeController {

    @Resource
    private SrmInAsnOrderDetBarcodeService wmsInAsnOrderDetBarcodeService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated SrmInAsnOrderDetBarcode wmsInAsnOrderDetBarcode) {
        return ControllerUtil.returnCRUD(wmsInAsnOrderDetBarcodeService.save(wmsInAsnOrderDetBarcode));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(wmsInAsnOrderDetBarcodeService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=SrmInAsnOrderDetBarcode.update.class) SrmInAsnOrderDetBarcode wmsInAsnOrderDetBarcode) {
        return ControllerUtil.returnCRUD(wmsInAsnOrderDetBarcodeService.update(wmsInAsnOrderDetBarcode));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SrmInAsnOrderDetBarcode> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        SrmInAsnOrderDetBarcode  wmsInAsnOrderDetBarcode = wmsInAsnOrderDetBarcodeService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(wmsInAsnOrderDetBarcode,StringUtils.isEmpty(wmsInAsnOrderDetBarcode)?0:1);
    }

/*
    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SrmInAsnOrderDetBarcodeDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSrmInAsnOrderDetBarcode searchSrmInAsnOrderDetBarcode) {
        Page<Object> page = PageHelper.startPage(searchSrmInAsnOrderDetBarcode.getStartPage(),searchSrmInAsnOrderDetBarcode.getPageSize());
        List<SrmInAsnOrderDetBarcodeDto> list = wmsInAsnOrderDetBarcodeService.findList(ControllerUtil.dynamicConditionByEntity(searchSrmInAsnOrderDetBarcode));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<SrmInAsnOrderDetBarcodeDto>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchSrmInAsnOrderDetBarcode searchSrmInAsnOrderDetBarcode) {
        List<SrmInAsnOrderDetBarcodeDto> list = wmsInAsnOrderDetBarcodeService.findList(ControllerUtil.dynamicConditionByEntity(searchSrmInAsnOrderDetBarcode));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }
*/

}
