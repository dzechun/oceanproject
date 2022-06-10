package com.fantechs.provider.base.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.basic.BaseLabelVariableDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseLabelVariableImport;
import com.fantechs.common.base.general.entity.basic.BaseLabelVariable;
import com.fantechs.common.base.general.entity.basic.history.BaseHtLabelVariable;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseLabelVariable;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.CustomFormUtils;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.auth.service.AuthFeignApi;
import com.fantechs.provider.base.service.BaseLabelVariableService;
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
 * Created by leifengzhi on 2021/09/16.
 */
@RestController
@Api(tags = "标签变量信息")
@RequestMapping("/baseLabelVariable")
@Validated
@Slf4j
public class BaseLabelVariableController {

    @Resource
    private BaseLabelVariableService baseLabelVariableService;
    @Resource
    private AuthFeignApi securityFeignApi;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated BaseLabelVariable baseLabelVariable) {
        return ControllerUtil.returnCRUD(baseLabelVariableService.save(baseLabelVariable));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseLabelVariableService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=BaseLabelVariable.update.class) BaseLabelVariable baseLabelVariable) {
        return ControllerUtil.returnCRUD(baseLabelVariableService.update(baseLabelVariable));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseLabelVariable> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BaseLabelVariable  baseLabelVariable = baseLabelVariableService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseLabelVariable,StringUtils.isEmpty(baseLabelVariable)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseLabelVariableDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseLabelVariable searchBaseLabelVariable) {
        Page<Object> page = PageHelper.startPage(searchBaseLabelVariable.getStartPage(),searchBaseLabelVariable.getPageSize());
        List<BaseLabelVariableDto> list = baseLabelVariableService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseLabelVariable));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<BaseLabelVariableDto>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchBaseLabelVariable searchBaseLabelVariable) {
        List<BaseLabelVariableDto> list = baseLabelVariableService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseLabelVariable));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseHtLabelVariable>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchBaseLabelVariable searchBaseLabelVariable) {
        Page<Object> page = PageHelper.startPage(searchBaseLabelVariable.getStartPage(),searchBaseLabelVariable.getPageSize());
        List<BaseHtLabelVariable> list = baseLabelVariableService.findHtList(ControllerUtil.dynamicConditionByEntity(searchBaseLabelVariable));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchBaseLabelVariable searchBaseLabelVariable){
        List<BaseLabelVariableDto> list = baseLabelVariableService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseLabelVariable));
        // 获取自定义导出参数列表
        List<Map<String, Object>> customExportParamList = BeanUtils.objectListToMapList(securityFeignApi.findCustomExportParamList(CustomFormUtils.getFromRout()).getData());
        // 自定义导出操作
        EasyPoiUtils.customExportExcel(list, customExportParamList, "导出信息", "标签变量信息", "标签变量信息.xls", response);
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
            List<BaseLabelVariableImport> baseLabelVariableImports = EasyPoiUtils.importExcel(file, 2, 1, BaseLabelVariableImport.class);
            Map<String, Object> resultMap = baseLabelVariableService.importExcel(baseLabelVariableImports);
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
