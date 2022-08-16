package com.fantechs.provider.mes.sfc.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.mes.sfc.MesSfcHtReworkOrderBarcodeDto;
import com.fantechs.common.base.general.dto.mes.sfc.MesSfcReworkOrderBarcodeDto;
import com.fantechs.common.base.general.dto.mes.sfc.Search.SearchMesSfcReworkOrderBarcode;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcReworkOrderBarcode;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.mes.sfc.service.MesSfcReworkOrderBarcodeService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/06/15.
 */
@RestController
@Api(tags = "生产管理-返工单条码管理控制器")
@RequestMapping("/mesSfcReworkOrderBarcode")
@Validated
public class MesSfcReworkOrderBarcodeController {

    @Resource
    private MesSfcReworkOrderBarcodeService mesSfcReworkOrderBarcodeService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated MesSfcReworkOrderBarcode mesSfcReworkOrderBarcode) {
        return ControllerUtil.returnCRUD(mesSfcReworkOrderBarcodeService.save(mesSfcReworkOrderBarcode));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(mesSfcReworkOrderBarcodeService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=MesSfcReworkOrderBarcode.update.class) MesSfcReworkOrderBarcode mesSfcReworkOrderBarcode) {
        return ControllerUtil.returnCRUD(mesSfcReworkOrderBarcodeService.update(mesSfcReworkOrderBarcode));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<MesSfcReworkOrderBarcode> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        MesSfcReworkOrderBarcode  mesSfcReworkOrderBarcode = mesSfcReworkOrderBarcodeService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(mesSfcReworkOrderBarcode,StringUtils.isEmpty(mesSfcReworkOrderBarcode)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<MesSfcReworkOrderBarcodeDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchMesSfcReworkOrderBarcode searchMesSfcReworkOrderBarcode) {
        Page<Object> page = PageHelper.startPage(searchMesSfcReworkOrderBarcode.getStartPage(),searchMesSfcReworkOrderBarcode.getPageSize());
        List<MesSfcReworkOrderBarcodeDto> list = mesSfcReworkOrderBarcodeService.findList(ControllerUtil.dynamicConditionByEntity(searchMesSfcReworkOrderBarcode));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<MesSfcHtReworkOrderBarcodeDto>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchMesSfcReworkOrderBarcode searchMesSfcReworkOrderBarcode) {
        Page<Object> page = PageHelper.startPage(searchMesSfcReworkOrderBarcode.getStartPage(),searchMesSfcReworkOrderBarcode.getPageSize());
        List<MesSfcHtReworkOrderBarcodeDto> list = mesSfcReworkOrderBarcodeService.findHtList(ControllerUtil.dynamicConditionByEntity(searchMesSfcReworkOrderBarcode));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchMesSfcReworkOrderBarcode searchMesSfcReworkOrderBarcode){
    List<MesSfcReworkOrderBarcodeDto> list = mesSfcReworkOrderBarcodeService.findList(ControllerUtil.dynamicConditionByEntity(searchMesSfcReworkOrderBarcode));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "MesSfcReworkOrderBarcode信息", MesSfcReworkOrderBarcodeDto.class, "MesSfcReworkOrderBarcode.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
