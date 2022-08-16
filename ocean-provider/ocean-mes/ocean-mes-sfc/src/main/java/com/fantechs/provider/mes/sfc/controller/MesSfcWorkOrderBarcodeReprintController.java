package com.fantechs.provider.mes.sfc.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.mes.sfc.MesSfcWorkOrderBarcodeReprintDto;
import com.fantechs.common.base.general.dto.mes.sfc.Search.SearchMesSfcWorkOrderBarcodeReprint;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcWorkOrderBarcodeReprint;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.mes.sfc.service.MesSfcWorkOrderBarcodeReprintService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
 * Created by leifengzhi on 2021/12/28.
 */
@RestController
@Api(tags = "mesSfcWorkOrderBarcodeReprint控制器")
@RequestMapping("/mesSfcWorkOrderBarcodeReprint")
@Validated
public class MesSfcWorkOrderBarcodeReprintController {

    @Resource
    private MesSfcWorkOrderBarcodeReprintService mesSfcWorkOrderBarcodeReprintService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated MesSfcWorkOrderBarcodeReprint mesSfcWorkOrderBarcodeReprint) {
        return ControllerUtil.returnCRUD(mesSfcWorkOrderBarcodeReprintService.save(mesSfcWorkOrderBarcodeReprint));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(mesSfcWorkOrderBarcodeReprintService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=MesSfcWorkOrderBarcodeReprint.update.class) MesSfcWorkOrderBarcodeReprint mesSfcWorkOrderBarcodeReprint) {
        return ControllerUtil.returnCRUD(mesSfcWorkOrderBarcodeReprintService.update(mesSfcWorkOrderBarcodeReprint));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<MesSfcWorkOrderBarcodeReprint> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        MesSfcWorkOrderBarcodeReprint  mesSfcWorkOrderBarcodeReprint = mesSfcWorkOrderBarcodeReprintService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(mesSfcWorkOrderBarcodeReprint,StringUtils.isEmpty(mesSfcWorkOrderBarcodeReprint)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<MesSfcWorkOrderBarcodeReprintDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchMesSfcWorkOrderBarcodeReprint searchMesSfcWorkOrderBarcodeReprint) {
        Page<Object> page = PageHelper.startPage(searchMesSfcWorkOrderBarcodeReprint.getStartPage(),searchMesSfcWorkOrderBarcodeReprint.getPageSize());
        List<MesSfcWorkOrderBarcodeReprintDto> list = mesSfcWorkOrderBarcodeReprintService.findList(ControllerUtil.dynamicConditionByEntity(searchMesSfcWorkOrderBarcodeReprint));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<MesSfcWorkOrderBarcodeReprintDto>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchMesSfcWorkOrderBarcodeReprint searchMesSfcWorkOrderBarcodeReprint) {
        List<MesSfcWorkOrderBarcodeReprintDto> list = mesSfcWorkOrderBarcodeReprintService.findList(ControllerUtil.dynamicConditionByEntity(searchMesSfcWorkOrderBarcodeReprint));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchMesSfcWorkOrderBarcodeReprint searchMesSfcWorkOrderBarcodeReprint){
    List<MesSfcWorkOrderBarcodeReprintDto> list = mesSfcWorkOrderBarcodeReprintService.findList(ControllerUtil.dynamicConditionByEntity(searchMesSfcWorkOrderBarcodeReprint));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "MesSfcWorkOrderBarcodeReprint信息", MesSfcWorkOrderBarcodeReprintDto.class, "MesSfcWorkOrderBarcodeReprint.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

    @PostMapping(value = "/import")
    @ApiOperation(value = "从excel导入",notes = "从excel导入")
    public ResponseEntity importExcel(@ApiParam(value ="输入excel文件",required = true) @RequestPart(value="file") MultipartFile file){
        try {
            // 导入操作
            List<MesSfcWorkOrderBarcodeReprint> baseAddressImports = EasyPoiUtils.importExcel(file, 0, 1, MesSfcWorkOrderBarcodeReprint.class);
            Map<String, Object> resultMap = mesSfcWorkOrderBarcodeReprintService.importExcel(baseAddressImports);
            return ControllerUtil.returnDataSuccess("操作结果集",resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.OPT20012002.getCode());
        }
    }
}
