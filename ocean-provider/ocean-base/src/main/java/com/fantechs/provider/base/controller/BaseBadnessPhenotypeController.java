package com.fantechs.provider.base.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseBadnessPhenotypeDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseBadnessPhenotypeImport;
import com.fantechs.common.base.general.entity.basic.BaseBadnessPhenotype;
import com.fantechs.common.base.general.entity.basic.history.BaseHtBadnessPhenotype;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseBadnessPhenotype;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseBadnessPhenotypeService;
import com.fantechs.provider.base.service.BaseHtBadnessPhenotypeService;
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
 * Created by leifengzhi on 2021/04/07.
 */
@RestController
@Api(tags = "不良现象控制器")
@RequestMapping("/baseBadnessPhenotype")
@Validated
@Slf4j
public class BaseBadnessPhenotypeController {

    @Resource
    private BaseBadnessPhenotypeService baseBadnessPhenotypeService;

    @Resource
    private BaseHtBadnessPhenotypeService baseHtBadnessPhenotypeService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated BaseBadnessPhenotype baseBadnessPhenotype) {
        return ControllerUtil.returnCRUD(baseBadnessPhenotypeService.save(baseBadnessPhenotype));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseBadnessPhenotypeService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=BaseBadnessPhenotype.update.class) BaseBadnessPhenotype baseBadnessPhenotype) {
        return ControllerUtil.returnCRUD(baseBadnessPhenotypeService.update(baseBadnessPhenotype));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseBadnessPhenotype> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BaseBadnessPhenotype  baseBadnessPhenotype = baseBadnessPhenotypeService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseBadnessPhenotype,StringUtils.isEmpty(baseBadnessPhenotype)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseBadnessPhenotypeDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseBadnessPhenotype searchBaseBadnessPhenotype) {
        Page<Object> page = PageHelper.startPage(searchBaseBadnessPhenotype.getStartPage(),searchBaseBadnessPhenotype.getPageSize());
        List<BaseBadnessPhenotypeDto> list = baseBadnessPhenotypeService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseBadnessPhenotype));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseHtBadnessPhenotype>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchBaseBadnessPhenotype searchBaseBadnessPhenotype) {
        Page<Object> page = PageHelper.startPage(searchBaseBadnessPhenotype.getStartPage(),searchBaseBadnessPhenotype.getPageSize());
        List<BaseHtBadnessPhenotype> list = baseHtBadnessPhenotypeService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseBadnessPhenotype));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchBaseBadnessPhenotype searchBaseBadnessPhenotype){
    List<BaseBadnessPhenotypeDto> list = baseBadnessPhenotypeService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseBadnessPhenotype));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "不良现象代码导出信息", "不良现象代码信息", BaseBadnessPhenotypeDto.class, "不良现象代码.xls", response);
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
    @ApiOperation(value = "从excel导入部件组成信息",notes = "从excel导入部件组成信息")
    public ResponseEntity importExcel(@ApiParam(value ="输入excel文件",required = true)
                                      @RequestPart(value="file") MultipartFile file){
        try {
            // 导入操作
            List<BaseBadnessPhenotypeImport> baseBadnessPhenotypeImports = EasyPoiUtils.importExcel(file, 2, 1, BaseBadnessPhenotypeImport.class);
            Map<String, Object> resultMap = baseBadnessPhenotypeService.importExcel(baseBadnessPhenotypeImports);
            return ControllerUtil.returnDataSuccess("操作结果集", resultMap);
        } catch (RuntimeException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ControllerUtil.returnFail("文件格式错误", ErrorCodeEnum.OPT20012002.getCode());
        }catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.OPT20012002.getCode());
        }
    }

    @ApiOperation(value = "接口新增或更新",notes = "接口新增或更新")
    @PostMapping("/saveByApi")
    public ResponseEntity saveByApi(@ApiParam(value = "必传：routeCode、organizationId",required = true)@RequestBody @Validated List<BaseBadnessPhenotype> baseBadnessPhenotypes) {
        int i = baseBadnessPhenotypeService.saveByApi(baseBadnessPhenotypes);
        return ControllerUtil.returnCRUD(i);
    }
}
