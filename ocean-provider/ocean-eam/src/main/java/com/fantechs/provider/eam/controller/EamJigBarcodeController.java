package com.fantechs.provider.eam.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamJigBarcodeDto;
import com.fantechs.common.base.general.entity.eam.EamJigBarcode;
import com.fantechs.common.base.general.entity.eam.history.EamHtJigBarcode;
import com.fantechs.common.base.general.entity.eam.search.SearchEamJigBarcode;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.service.EamHtJigBarcodeService;
import com.fantechs.provider.eam.service.EamJigBarcodeService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/07/28.
 */
@RestController
@Api(tags = "治具条码信息")
@RequestMapping("/eamJigBarcode")
@Validated
public class EamJigBarcodeController {

    @Resource
    private EamJigBarcodeService eamJigBarcodeService;
    @Resource
    private EamHtJigBarcodeService eamHtJigBarcodeService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated EamJigBarcode eamJigBarcode) {
        return ControllerUtil.returnCRUD(eamJigBarcodeService.save(eamJigBarcode));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(eamJigBarcodeService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=EamJigBarcode.update.class) EamJigBarcode eamJigBarcode) {
        return ControllerUtil.returnCRUD(eamJigBarcodeService.update(eamJigBarcode));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<EamJigBarcode> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        EamJigBarcode  eamJigBarcode = eamJigBarcodeService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(eamJigBarcode,StringUtils.isEmpty(eamJigBarcode)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<EamJigBarcodeDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchEamJigBarcode searchEamJigBarcode) {
        Page<Object> page = PageHelper.startPage(searchEamJigBarcode.getStartPage(),searchEamJigBarcode.getPageSize());
        List<EamJigBarcodeDto> list = eamJigBarcodeService.findList(ControllerUtil.dynamicConditionByEntity(searchEamJigBarcode));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<EamHtJigBarcode>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchEamJigBarcode searchEamJigBarcode) {
        Page<Object> page = PageHelper.startPage(searchEamJigBarcode.getStartPage(),searchEamJigBarcode.getPageSize());
        List<EamHtJigBarcode> list = eamHtJigBarcodeService.findHtList(ControllerUtil.dynamicConditionByEntity(searchEamJigBarcode));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchEamJigBarcode searchEamJigBarcode){
    List<EamJigBarcodeDto> list = eamJigBarcodeService.findList(ControllerUtil.dynamicConditionByEntity(searchEamJigBarcode));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "治具条码信息", EamJigBarcodeDto.class, "治具条码信息.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
    /*
    * 2021/08/11
    * huangshuijun
    */
    @ApiOperation("增加治具条码使用次数")
    @PostMapping("/plusCurrentUsageTime")
    public ResponseEntity plusCurrentUsageTime(@ApiParam(value = "治具条码id", required = true) @RequestParam @NotNull(message = "治具条码id")Long jigBarcodeId,
                                                  @ApiParam(value = "治具使用次数", required = true) @RequestParam @NotNull(message = "治具使用次数不能为空") Integer num) {
        return ControllerUtil.returnCRUD(eamJigBarcodeService.plusCurrentUsageTime(jigBarcodeId,num));
    }
}
