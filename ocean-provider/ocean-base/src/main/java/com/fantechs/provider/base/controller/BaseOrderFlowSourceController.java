package com.fantechs.provider.base.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.imports.BaseOrderFlowSourceImport;
import com.fantechs.common.base.general.entity.basic.BaseOrderFlowSource;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseOrderFlowSource;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseOrderFlowSourceService;
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
 * Created by leifengzhi on 2022/02/15.
 */
@RestController
@Api(tags = "单据流数据源")
@RequestMapping("/baseOrderFlowSource")
@Validated
@Slf4j
public class BaseOrderFlowSourceController {

    @Resource
    private BaseOrderFlowSourceService baseOrderFlowSourceService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated BaseOrderFlowSource baseOrderFlowSource) {
        return ControllerUtil.returnCRUD(baseOrderFlowSourceService.save(baseOrderFlowSource));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseOrderFlowSourceService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=BaseOrderFlowSource.update.class) BaseOrderFlowSource baseOrderFlowSource) {
        return ControllerUtil.returnCRUD(baseOrderFlowSourceService.update(baseOrderFlowSource));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseOrderFlowSource> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BaseOrderFlowSource  baseOrderFlowSource = baseOrderFlowSourceService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseOrderFlowSource,StringUtils.isEmpty(baseOrderFlowSource)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseOrderFlowSource>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseOrderFlowSource searchBaseOrderFlowSource) {
        Page<Object> page = PageHelper.startPage(searchBaseOrderFlowSource.getStartPage(),searchBaseOrderFlowSource.getPageSize());
        List<BaseOrderFlowSource> list = baseOrderFlowSourceService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseOrderFlowSource));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<BaseOrderFlowSource>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchBaseOrderFlowSource searchBaseOrderFlowSource) {
        List<BaseOrderFlowSource> list = baseOrderFlowSourceService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseOrderFlowSource));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchBaseOrderFlowSource searchBaseOrderFlowSource){
    List<BaseOrderFlowSource> list = baseOrderFlowSourceService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseOrderFlowSource));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "BaseOrderFlowSource信息", BaseOrderFlowSource.class, "BaseOrderFlowSource.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

    @PostMapping(value = "/import")
    @ApiOperation(value = "从excel导入",notes = "从excel导入")
    public ResponseEntity importExcel(@ApiParam(value ="输入excel文件",required = true) @RequestPart(value="file") MultipartFile file){
        try {
            // 导入操作
            List<BaseOrderFlowSourceImport> baseOrderFlowSourceImports = EasyPoiUtils.importExcel(file, 2, 1, BaseOrderFlowSourceImport.class);
            Map<String, Object> resultMap = baseOrderFlowSourceService.importExcel(baseOrderFlowSourceImports);
            return ControllerUtil.returnDataSuccess("操作结果集",resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.OPT20012002.getCode());
        }
    }
}
