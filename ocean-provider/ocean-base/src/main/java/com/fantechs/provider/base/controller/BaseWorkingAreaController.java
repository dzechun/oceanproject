package com.fantechs.provider.base.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.basic.BaseWorkingAreaDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseWorkingAreaImport;
import com.fantechs.common.base.general.entity.basic.BaseWorkingArea;
import com.fantechs.common.base.general.entity.basic.history.BaseHtWorkingArea;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseWorkingArea;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.CustomFormUtils;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.auth.service.AuthFeignApi;
import com.fantechs.provider.base.service.BaseHtWorkingAreaService;
import com.fantechs.provider.base.service.BaseWorkingAreaService;
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

@RestController
@Api(tags = "工作区信息")
@RequestMapping("/baseWorkingArea")
@Validated
@Slf4j
public class BaseWorkingAreaController {

    @Resource
    private BaseWorkingAreaService baseWorkingAreaService;
    @Resource
    private BaseHtWorkingAreaService baseHtWorkingAreaService;
    @Resource
    private AuthFeignApi securityFeignApi;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated BaseWorkingArea baseWorkingArea) {
        return ControllerUtil.returnCRUD(baseWorkingAreaService.save(baseWorkingArea));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseWorkingAreaService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=BaseWorkingArea.update.class) BaseWorkingArea baseWorkingArea) {
        return ControllerUtil.returnCRUD(baseWorkingAreaService.update(baseWorkingArea));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseWorkingArea> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BaseWorkingArea  baseWorkingArea = baseWorkingAreaService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseWorkingArea,StringUtils.isEmpty(baseWorkingArea)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseWorkingAreaDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseWorkingArea searchBaseWorkingArea) {
        Page<Object> page = PageHelper.startPage(searchBaseWorkingArea.getStartPage(),searchBaseWorkingArea.getPageSize());
        List<BaseWorkingAreaDto> list = baseWorkingAreaService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseWorkingArea));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseHtWorkingArea>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchBaseWorkingArea searchBaseWorkingArea) {
        Page<Object> page = PageHelper.startPage(searchBaseWorkingArea.getStartPage(),searchBaseWorkingArea.getPageSize());
        List<BaseHtWorkingArea> list = baseHtWorkingAreaService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseWorkingArea));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchBaseWorkingArea searchBaseWorkingArea){
    List<BaseWorkingAreaDto> list = baseWorkingAreaService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseWorkingArea));
        // 获取自定义导出参数列表
        List<Map<String, Object>> customExportParamList = BeanUtils.objectListToMapList(securityFeignApi.findCustomExportParamList(CustomFormUtils.getFromRout()).getData());
        // 自定义导出操作
        EasyPoiUtils.customExportExcel(list, customExportParamList, "工作区导出信息", "工作区信息", "工作区.xls", response);
    }

    /**
     * 从excel导入数据
     * @return
     * @throws
     */
    @PostMapping(value = "/import")
    @ApiOperation(value = "从excel导入信息",notes = "从excel导入信息")
    public ResponseEntity importExcel(@ApiParam(value ="输入excel文件",required = true)
                                      @RequestPart(value="file") MultipartFile file){
        try {
            // 导入操作
            List<BaseWorkingAreaImport> baseWorkingAreaImports = EasyPoiUtils.importExcel(file, 2, 1, BaseWorkingAreaImport.class);
            Map<String, Object> resultMap = baseWorkingAreaService.importExcel(baseWorkingAreaImports);
            return ControllerUtil.returnDataSuccess("操作结果集",resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.OPT20012002.getCode());
        }
    }
}
