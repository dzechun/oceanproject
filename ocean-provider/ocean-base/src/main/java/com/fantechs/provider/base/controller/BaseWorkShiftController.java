package com.fantechs.provider.base.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseWorkShiftDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseWorkShiftImport;
import com.fantechs.common.base.general.entity.basic.BaseWorkShift;
import com.fantechs.common.base.general.entity.basic.history.BaseHtWorkShift;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseWorkShift;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseHtWorkShiftService;
import com.fantechs.provider.base.service.BaseWorkShiftService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 *
 * Created by leifengzhi on 2020/12/21.
 */
@RestController
@Api(tags = "班次信息管理")
@RequestMapping("/baseWorkShift")
@Validated
@Slf4j
public class BaseWorkShiftController {

    @Autowired
    private BaseWorkShiftService baseWorkShiftService;
    @Resource
    private BaseHtWorkShiftService baseHtWorkShiftService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：workShiftCode、workShiftName",required = true)@RequestBody @Validated BaseWorkShift baseWorkShift) {
        return ControllerUtil.returnCRUD(baseWorkShiftService.save(baseWorkShift));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseWorkShiftService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=BaseWorkShift.update.class) BaseWorkShift baseWorkShift) {
        return ControllerUtil.returnCRUD(baseWorkShiftService.update(baseWorkShift));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseWorkShift> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BaseWorkShift  baseWorkShift = baseWorkShiftService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseWorkShift,StringUtils.isEmpty(baseWorkShift)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseWorkShiftDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseWorkShift searchBaseWorkShift) {
        Page<Object> page = PageHelper.startPage(searchBaseWorkShift.getStartPage(),searchBaseWorkShift.getPageSize());
        List<BaseWorkShiftDto> list = baseWorkShiftService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseWorkShift));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseHtWorkShift>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchBaseWorkShift searchBaseWorkShift) {
        Page<Object> page = PageHelper.startPage(searchBaseWorkShift.getStartPage(),searchBaseWorkShift.getPageSize());
        List<BaseHtWorkShift> list = baseHtWorkShiftService.findHtList(ControllerUtil.dynamicConditionByEntity(searchBaseWorkShift));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchBaseWorkShift searchBaseWorkShift){
    List<BaseWorkShiftDto> list = baseWorkShiftService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseWorkShift));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "BaseWorkShift信息", BaseWorkShiftDto.class, "BaseWorkShift.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

    /**
     * 从excel导入数据
     * @return
     * @throws
     */
    @PostMapping(value = "/import")
    @ApiOperation(value = "从excel导入电子标签信息",notes = "从excel导入电子标签信息")
    public ResponseEntity importExcel(@ApiParam(value ="输入excel文件",required = true)
                                      @RequestPart(value="file") MultipartFile file){
        try {
            // 导入操作
            List<BaseWorkShiftImport> baseWorkShiftImports = EasyPoiUtils.importExcel(file, 2, 1, BaseWorkShiftImport.class);
            Map<String, Object> resultMap = baseWorkShiftService.importExcel(baseWorkShiftImports);
            return ControllerUtil.returnDataSuccess("操作结果集", resultMap);
        }catch (RuntimeException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ControllerUtil.returnFail("文件格式错误", ErrorCodeEnum.OPT20012002.getCode());
        }catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.OPT20012002.getCode());
        }
    }
}
