package com.fantechs.provider.base.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseSamplePlanDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseCurrencyImport;
import com.fantechs.common.base.general.dto.basic.imports.BaseSamplePlanImport;
import com.fantechs.common.base.general.entity.basic.BaseSamplePlan;
import com.fantechs.common.base.general.entity.basic.history.BaseHtSamplePlan;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseSamplePlan;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseHtSamplePlanService;
import com.fantechs.provider.base.service.BaseSamplePlanService;
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
 * Created by leifengzhi on 2021/04/21.
 */
@RestController
@Api(tags = "抽样方案")
@RequestMapping("/baseSamplePlan")
@Validated
@Slf4j
public class BaseSamplePlanController {

    @Resource
    private BaseSamplePlanService baseSamplePlanService;

    @Resource
    private BaseHtSamplePlanService baseHtSamplePlanService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated BaseSamplePlan baseSamplePlan) {
        return ControllerUtil.returnCRUD(baseSamplePlanService.save(baseSamplePlan));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseSamplePlanService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value= BaseSamplePlan.update.class) BaseSamplePlan baseSamplePlan) {
        return ControllerUtil.returnCRUD(baseSamplePlanService.update(baseSamplePlan));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseSamplePlan> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BaseSamplePlan baseSamplePlan = baseSamplePlanService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseSamplePlan,StringUtils.isEmpty(baseSamplePlan)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseSamplePlanDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseSamplePlan searchBaseSamplePlan) {
        Page<Object> page = PageHelper.startPage(searchBaseSamplePlan.getStartPage(), searchBaseSamplePlan.getPageSize());
        List<BaseSamplePlanDto> list = baseSamplePlanService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseSamplePlan));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseHtSamplePlan>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchBaseSamplePlan searchBaseSamplePlan) {
        Page<Object> page = PageHelper.startPage(searchBaseSamplePlan.getStartPage(), searchBaseSamplePlan.getPageSize());
        List<BaseHtSamplePlan> list = baseHtSamplePlanService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseSamplePlan));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchBaseSamplePlan searchBaseSamplePlan){
    List<BaseSamplePlanDto> list = baseSamplePlanService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseSamplePlan));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "抽样方案导出信息", "抽样方案信息", BaseSamplePlanDto.class, "抽样方案.xls", response);
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
            List<BaseSamplePlanImport> baseSamplePlanImports = EasyPoiUtils.importExcel(file,2,1, BaseSamplePlanImport.class);
            Map<String, Object> resultMap = baseSamplePlanService.importExcel(baseSamplePlanImports);
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
