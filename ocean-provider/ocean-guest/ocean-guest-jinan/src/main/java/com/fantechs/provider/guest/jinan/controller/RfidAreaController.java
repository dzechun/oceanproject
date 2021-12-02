package com.fantechs.provider.guest.jinan.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.jinan.Import.RfidAreaImport;
import com.fantechs.common.base.general.entity.jinan.RfidArea;
import com.fantechs.common.base.general.entity.jinan.history.RfidHtArea;
import com.fantechs.common.base.general.entity.jinan.search.SearchRfidArea;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.guest.jinan.service.RfidAreaService;
import com.fantechs.provider.guest.jinan.service.RfidHtAreaService;
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
 * Created by leifengzhi on 2021/11/29.
 */
@RestController
@Api(tags = "RFID区域管理")
@RequestMapping("/rfidArea")
@Validated
@Slf4j
public class RfidAreaController {

    @Resource
    private RfidAreaService rfidAreaService;
    @Resource
    private RfidHtAreaService rfidHtAreaService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated(value=RfidArea.add.class) RfidArea rfidArea) {
        return ControllerUtil.returnCRUD(rfidAreaService.save(rfidArea));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(rfidAreaService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=RfidArea.update.class) RfidArea rfidArea) {
        return ControllerUtil.returnCRUD(rfidAreaService.update(rfidArea));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<RfidArea> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        RfidArea  rfidArea = rfidAreaService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(rfidArea,StringUtils.isEmpty(rfidArea)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<RfidArea>> findList(@ApiParam(value = "查询对象")@RequestBody SearchRfidArea searchRfidArea) {
        Page<Object> page = PageHelper.startPage(searchRfidArea.getStartPage(),searchRfidArea.getPageSize());
        List<RfidArea> list = rfidAreaService.findList(ControllerUtil.dynamicConditionByEntity(searchRfidArea));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<RfidArea>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchRfidArea searchRfidArea) {
        List<RfidArea> list = rfidAreaService.findList(ControllerUtil.dynamicConditionByEntity(searchRfidArea));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<RfidHtArea>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchRfidArea searchRfidArea) {
        Page<Object> page = PageHelper.startPage(searchRfidArea.getStartPage(),searchRfidArea.getPageSize());
        List<RfidHtArea> list = rfidHtAreaService.findHtList(ControllerUtil.dynamicConditionByEntity(searchRfidArea));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchRfidArea searchRfidArea){
    List<RfidArea> list = rfidAreaService.findList(ControllerUtil.dynamicConditionByEntity(searchRfidArea));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "RFID区域", RfidArea.class, "RFID区域.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

    @PostMapping(value = "/import")
    @ApiOperation(value = "从excel导入",notes = "从excel导入")
    public ResponseEntity importExcel(@ApiParam(value ="输入excel文件",required = true) @RequestPart(value="file") MultipartFile file){
        try {
            // 导入操作
            List<RfidAreaImport> rfidAreaImports = EasyPoiUtils.importExcel(file, 2, 1, RfidAreaImport.class);
            Map<String, Object> resultMap = rfidAreaService.importExcel(rfidAreaImports);
            return ControllerUtil.returnDataSuccess("操作结果集",resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.OPT20012002.getCode());
        }
    }
}
