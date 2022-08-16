package com.fantechs.provider.base.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseBatchRulesDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseBatchRulesImport;
import com.fantechs.common.base.general.entity.basic.BaseBatchRules;
import com.fantechs.common.base.general.entity.basic.history.BaseHtBatchRules;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseBatchRules;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseBatchRulesService;
import com.fantechs.provider.base.service.BaseHtBatchRulesService;
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
 * Created by Mr.Lei on 2021/04/25.
 */
@RestController
@Api(tags = "批次规则")
@RequestMapping("/baseBatchRules")
@Validated
@Slf4j
public class BaseBatchRulesController {

    @Resource
    private BaseBatchRulesService baseBatchRulesService;
    @Resource
    private BaseHtBatchRulesService baseHtBatchRulesService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated BaseBatchRules baseBatchRules) {
        return ControllerUtil.returnCRUD(baseBatchRulesService.save(baseBatchRules));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseBatchRulesService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=BaseBatchRules.update.class) BaseBatchRules baseBatchRules) {
        return ControllerUtil.returnCRUD(baseBatchRulesService.update(baseBatchRules));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseBatchRules> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BaseBatchRules  baseBatchRules = baseBatchRulesService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseBatchRules,StringUtils.isEmpty(baseBatchRules)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseBatchRulesDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseBatchRules searchBaseBatchRules) {
        Page<Object> page = PageHelper.startPage(searchBaseBatchRules.getStartPage(),searchBaseBatchRules.getPageSize());
        List<BaseBatchRulesDto> list = baseBatchRulesService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseBatchRules));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseHtBatchRules>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchBaseBatchRules searchBaseBatchRules) {
        Page<Object> page = PageHelper.startPage(searchBaseBatchRules.getStartPage(),searchBaseBatchRules.getPageSize());
        List<BaseHtBatchRules> list = baseHtBatchRulesService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseBatchRules));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchBaseBatchRules searchBaseBatchRules){
    List<BaseBatchRulesDto> list = baseBatchRulesService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseBatchRules));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "BaseBatchRules信息", BaseBatchRulesDto.class, "BaseBatchRules.xls", response);
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
    @ApiOperation(value = "从excel导入信息",notes = "从excel导入信息")
    public ResponseEntity importExcel(@ApiParam(value ="输入excel文件",required = true)
                                      @RequestPart(value="file") MultipartFile file){
        try {
            // 导入操作
            List<BaseBatchRulesImport> baseBatchRulesImports = EasyPoiUtils.importExcel(file,2,1, BaseBatchRulesImport.class);
            Map<String, Object> resultMap = baseBatchRulesService.importExcel(baseBatchRulesImports);
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
