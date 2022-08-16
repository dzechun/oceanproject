package com.fantechs.provider.base.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.imports.BaseInspectionStandardImport;
import com.fantechs.common.base.general.dto.basic.imports.BaseWorkerImport;
import com.fantechs.common.base.general.entity.basic.BaseInspectionStandard;
import com.fantechs.common.base.general.entity.basic.history.BaseHtInspectionStandard;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseInspectionStandard;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseHtInspectionStandardService;
import com.fantechs.provider.base.service.BaseInspectionStandardService;
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
 * Created by leifengzhi on 2021/05/19.
 */
@RestController
@Api(tags = "检验标准控制器")
@RequestMapping("/baseInspectionStandard")
@Validated
@Slf4j
public class BaseInspectionStandardController {

    @Resource
    private BaseInspectionStandardService baseInspectionStandardService;
    @Resource
    private BaseHtInspectionStandardService baseHtInspectionStandardService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated BaseInspectionStandard baseInspectionStandard) {
        return ControllerUtil.returnCRUD(baseInspectionStandardService.save(baseInspectionStandard));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseInspectionStandardService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=BaseInspectionStandard.update.class) BaseInspectionStandard baseInspectionStandard) {
        return ControllerUtil.returnCRUD(baseInspectionStandardService.update(baseInspectionStandard));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseInspectionStandard> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BaseInspectionStandard  baseInspectionStandard = baseInspectionStandardService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseInspectionStandard,StringUtils.isEmpty(baseInspectionStandard)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseInspectionStandard>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseInspectionStandard searchBaseInspectionStandard) {
        Page<Object> page = PageHelper.startPage(searchBaseInspectionStandard.getStartPage(),searchBaseInspectionStandard.getPageSize());
        List<BaseInspectionStandard> list = baseInspectionStandardService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseInspectionStandard));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseHtInspectionStandard>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchBaseInspectionStandard searchBaseInspectionStandard) {
        Page<Object> page = PageHelper.startPage(searchBaseInspectionStandard.getStartPage(),searchBaseInspectionStandard.getPageSize());
        List<BaseHtInspectionStandard> list = baseHtInspectionStandardService.findHtList(ControllerUtil.dynamicConditionByEntity(searchBaseInspectionStandard));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchBaseInspectionStandard searchBaseInspectionStandard){
    List<BaseInspectionStandard> list = baseInspectionStandardService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseInspectionStandard));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "检验标准信息", BaseInspectionStandard.class, "检验标准.xls", response);
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
            List<BaseInspectionStandardImport> baseInspectionStandardImports = EasyPoiUtils.importExcel(file, 2, 1, BaseInspectionStandardImport.class);
            Map<String, Object> resultMap = baseInspectionStandardService.importExcel(baseInspectionStandardImports);
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
}
