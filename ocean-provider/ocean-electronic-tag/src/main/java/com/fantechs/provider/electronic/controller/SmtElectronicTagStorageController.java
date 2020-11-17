package com.fantechs.provider.electronic.controller;

import com.fantechs.common.base.dto.SmtElectronicTagStorageDto;
import com.fantechs.common.base.entity.SmtElectronicTagStorage;
import com.fantechs.common.base.entity.history.SmtHtElectronicTagStorage;
import com.fantechs.common.base.entity.search.SearchSmtElectronicTagStorage;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.electronic.mapper.SmtHtElectronicTagStorageMapper;
import com.fantechs.provider.electronic.service.SmtElectronicTagStorageService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2020/11/17.
 */
@RestController
@Api(tags = "电子标签绑定储位")
@RequestMapping("/smtElectronicTagStorage")
@Validated
public class SmtElectronicTagStorageController {

    @Autowired
    private SmtElectronicTagStorageService smtElectronicTagStorageService;
    @Resource
    private SmtHtElectronicTagStorageMapper smtHtElectronicTagStorageMapper;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：storageId、electronicTagControllerId、electronicTagId",required = true)@RequestBody @Validated SmtElectronicTagStorage smtElectronicTagStorage) {
        return ControllerUtil.returnCRUD(smtElectronicTagStorageService.save(smtElectronicTagStorage));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(smtElectronicTagStorageService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=SmtElectronicTagStorage.update.class) SmtElectronicTagStorage smtElectronicTagStorage) {
        return ControllerUtil.returnCRUD(smtElectronicTagStorageService.update(smtElectronicTagStorage));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SmtElectronicTagStorage> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        SmtElectronicTagStorage  smtElectronicTagStorage = smtElectronicTagStorageService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(smtElectronicTagStorage,StringUtils.isEmpty(smtElectronicTagStorage)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SmtElectronicTagStorageDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSmtElectronicTagStorage searchSmtElectronicTagStorage) {
        Page<Object> page = PageHelper.startPage(searchSmtElectronicTagStorage.getStartPage(),searchSmtElectronicTagStorage.getPageSize());
        List<SmtElectronicTagStorageDto> list = smtElectronicTagStorageService.findList(ControllerUtil.dynamicConditionByEntity(searchSmtElectronicTagStorage));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<SmtHtElectronicTagStorage>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchSmtElectronicTagStorage searchSmtElectronicTagStorage) {
        Page<Object> page = PageHelper.startPage(searchSmtElectronicTagStorage.getStartPage(),searchSmtElectronicTagStorage.getPageSize());
        List<SmtHtElectronicTagStorage> list = smtHtElectronicTagStorageMapper.findHtList(ControllerUtil.dynamicConditionByEntity(searchSmtElectronicTagStorage));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchSmtElectronicTagStorage searchSmtElectronicTagStorage){
    List<SmtElectronicTagStorageDto> list = smtElectronicTagStorageService.findList(ControllerUtil.dynamicConditionByEntity(searchSmtElectronicTagStorage));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "SmtElectronicTagStorage信息", SmtElectronicTagStorageDto.class, "SmtElectronicTagStorage.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
