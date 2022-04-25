package com.fantechs.provider.base.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.basic.WanbaoBarcodeRultDataDto;
import com.fantechs.common.base.general.dto.basic.imports.WanbaoBarcodeRultDataImportDto;
import com.fantechs.common.base.general.entity.basic.WanbaoBarcodeRultData;
import com.fantechs.common.base.general.entity.basic.search.SearchWanbaoBarcodeRultData;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.WanbaoBarcodeRultDataService;
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
 * Created by leifengzhi on 2022/02/22.
 */
@RestController
@Api(tags = "万宝识别码控制器")
@RequestMapping("/wanbaoBarcodeRultData")
@Validated
public class WanbaoBarcodeRultDataController {

    @Resource
    private WanbaoBarcodeRultDataService wanbaoBarcodeRultDataService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WanbaoBarcodeRultData wanbaoBarcodeRultData) {
        return ControllerUtil.returnCRUD(wanbaoBarcodeRultDataService.save(wanbaoBarcodeRultData));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(wanbaoBarcodeRultDataService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=WanbaoBarcodeRultData.update.class) WanbaoBarcodeRultData wanbaoBarcodeRultData) {
        return ControllerUtil.returnCRUD(wanbaoBarcodeRultDataService.update(wanbaoBarcodeRultData));
    }

    @ApiOperation("同步物料时批量修改识别码")
    @PostMapping("/updateByMaterial")
    public ResponseEntity updateByMaterial(@ApiParam(value = "对象，Id必传",required = true)@RequestBody List<Long> list) {
        return ControllerUtil.returnCRUD(wanbaoBarcodeRultDataService.updateByMaterial(list));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<WanbaoBarcodeRultData> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        WanbaoBarcodeRultData  wanbaoBarcodeRultData = wanbaoBarcodeRultDataService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(wanbaoBarcodeRultData,StringUtils.isEmpty(wanbaoBarcodeRultData)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WanbaoBarcodeRultDataDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWanbaoBarcodeRultData searchWanbaoBarcodeRultData) {
        Page<Object> page = PageHelper.startPage(searchWanbaoBarcodeRultData.getStartPage(),searchWanbaoBarcodeRultData.getPageSize());
        List<WanbaoBarcodeRultDataDto> list = wanbaoBarcodeRultDataService.findList(ControllerUtil.dynamicConditionByEntity(searchWanbaoBarcodeRultData));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<WanbaoBarcodeRultDataDto>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchWanbaoBarcodeRultData searchWanbaoBarcodeRultData) {
        List<WanbaoBarcodeRultDataDto> list = wanbaoBarcodeRultDataService.findList(ControllerUtil.dynamicConditionByEntity(searchWanbaoBarcodeRultData));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchWanbaoBarcodeRultData searchWanbaoBarcodeRultData){
    List<WanbaoBarcodeRultDataDto> list = wanbaoBarcodeRultDataService.findList(ControllerUtil.dynamicConditionByEntity(searchWanbaoBarcodeRultData));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "识别码信息", "识别码信息", WanbaoBarcodeRultDataDto.class, "识别码信息.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

    @PostMapping(value = "/import")
    @ApiOperation(value = "从excel导入",notes = "从excel导入")
    public ResponseEntity importExcel(@ApiParam(value ="输入excel文件",required = true) @RequestPart(value="file") MultipartFile file){
        try {
            // 导入操作
            List<WanbaoBarcodeRultDataImportDto> baseAddressImports = EasyPoiUtils.importExcel(file, 0, 1, WanbaoBarcodeRultDataImportDto.class);
            Map<String, Object> resultMap = wanbaoBarcodeRultDataService.importExcel(baseAddressImports);
            return ControllerUtil.returnDataSuccess("操作结果集",resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.OPT20012002.getCode());
        }
    }
}
