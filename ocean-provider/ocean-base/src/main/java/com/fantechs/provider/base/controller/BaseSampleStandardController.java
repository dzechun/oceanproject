package com.fantechs.provider.base.controller;

import com.fantechs.common.base.general.dto.basic.BaseSampleStandardDto;
import com.fantechs.common.base.general.entity.basic.BaseSampleStandard;
import com.fantechs.common.base.general.entity.basic.history.BaseHtSampleStandard;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseSampleStandard;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.CustomFormUtils;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.auth.service.AuthFeignApi;
import com.fantechs.provider.base.service.BaseHtSampleStandardService;
import com.fantechs.provider.base.service.BaseSampleStandardService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * Created by leifengzhi on 2021/04/06.
 */
@RestController
@Api(tags = "抽样标准信息管理")
@RequestMapping("/baseSampleStandard")
@Validated
public class BaseSampleStandardController {

    @Resource
    private BaseSampleStandardService baseSampleStandardService;
    @Resource
    private BaseHtSampleStandardService baseHtSampleStandardService;
    @Resource
    private AuthFeignApi securityFeignApi;

    @ApiOperation(value = "新增", notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：", required = true) @RequestBody @Validated BaseSampleStandard baseSampleStandard) {
        return ControllerUtil.returnCRUD(baseSampleStandardService.save(baseSampleStandard));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔", required = true) @RequestParam @NotBlank(message = "ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseSampleStandardService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传", required = true) @RequestBody @Validated(value = BaseSampleStandard.update.class) BaseSampleStandard baseSampleStandard) {
        return ControllerUtil.returnCRUD(baseSampleStandardService.update(baseSampleStandard));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseSampleStandard> detail(@ApiParam(value = "ID", required = true) @RequestParam @NotNull(message = "id不能为空") Long id) {
        BaseSampleStandard baseSampleStandard = baseSampleStandardService.selectByKey(id);
        return ControllerUtil.returnDataSuccess(baseSampleStandard, StringUtils.isEmpty(baseSampleStandard) ? 0 : 1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseSampleStandardDto>> findList(@ApiParam(value = "查询对象") @RequestBody SearchBaseSampleStandard searchBaseSampleStandard) {
        Page<Object> page = PageHelper.startPage(searchBaseSampleStandard.getStartPage(), searchBaseSampleStandard.getPageSize());
        List<BaseSampleStandardDto> list = baseSampleStandardService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseSampleStandard));
        return ControllerUtil.returnDataSuccess(list, (int) page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseHtSampleStandard>> findHtList(@ApiParam(value = "查询对象") @RequestBody SearchBaseSampleStandard searchBaseSampleStandard) {
        Page<Object> page = PageHelper.startPage(searchBaseSampleStandard.getStartPage(), searchBaseSampleStandard.getPageSize());
        List<BaseHtSampleStandard> list = baseHtSampleStandardService.findHtList(ControllerUtil.dynamicConditionByEntity(searchBaseSampleStandard));
        return ControllerUtil.returnDataSuccess(list, (int) page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel", notes = "导出excel", produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchBaseSampleStandard searchBaseSampleStandard) {
        List<BaseSampleStandardDto> list = baseSampleStandardService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseSampleStandard));
        // 获取自定义导出参数列表
        List<Map<String, Object>> customExportParamList = BeanUtils.objectListToMapList(securityFeignApi.findCustomExportParamList(CustomFormUtils.getFromRout()).getData());
        // 自定义导出操作
        EasyPoiUtils.customExportExcel(list, customExportParamList, "导出信息", "BaseSampleStandard信息", "BaseSampleStandard.xls", response);
    }
}
