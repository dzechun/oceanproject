package com.fantechs.provider.base.controller;

import com.fantechs.common.base.general.dto.basic.BaseBadnessCategoryDto;
import com.fantechs.common.base.general.entity.basic.BaseBadnessCategory;
import com.fantechs.common.base.general.entity.basic.history.BaseHtBadnessCategory;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseBadnessCategory;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.CustomFormUtils;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.auth.service.AuthFeignApi;
import com.fantechs.provider.base.service.BaseBadnessCategoryService;
import com.fantechs.provider.base.service.BaseHtBadnessCategoryService;
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
 *
 * Created by leifengzhi on 2021/04/02.
 */
@RestController
@Api(tags = "不良类别控制器")
@RequestMapping("/baseBadnessCategory")
@Validated
public class BaseBadnessCategoryController {

    @Resource
    private BaseBadnessCategoryService baseBadnessCategoryService;
    @Resource
    private BaseHtBadnessCategoryService baseHtBadnessCategoryService;
    @Resource
    private AuthFeignApi securityFeignApi;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated BaseBadnessCategory baseBadnessCategory) {
        return ControllerUtil.returnCRUD(baseBadnessCategoryService.save(baseBadnessCategory));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseBadnessCategoryService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=BaseBadnessCategory.update.class) BaseBadnessCategory baseBadnessCategory) {
        return ControllerUtil.returnCRUD(baseBadnessCategoryService.update(baseBadnessCategory));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseBadnessCategory> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BaseBadnessCategory  baseBadnessCategory = baseBadnessCategoryService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseBadnessCategory,StringUtils.isEmpty(baseBadnessCategory)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseBadnessCategoryDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseBadnessCategory searchBaseBadnessCategory) {
        Page<Object> page = PageHelper.startPage(searchBaseBadnessCategory.getStartPage(),searchBaseBadnessCategory.getPageSize());
        List<BaseBadnessCategoryDto> list = baseBadnessCategoryService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseBadnessCategory));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseHtBadnessCategory>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchBaseBadnessCategory searchBaseBadnessCategory) {
        Page<Object> page = PageHelper.startPage(searchBaseBadnessCategory.getStartPage(),searchBaseBadnessCategory.getPageSize());
        List<BaseHtBadnessCategory> list = baseHtBadnessCategoryService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseBadnessCategory));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchBaseBadnessCategory searchBaseBadnessCategory){
        List<BaseBadnessCategoryDto> list = baseBadnessCategoryService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseBadnessCategory));
        // 获取自定义导出参数列表
        List<Map<String, Object>> customExportParamList = BeanUtils.objectListToMapList(securityFeignApi.findCustomExportParamList(CustomFormUtils.getFromRout()).getData());
        EasyPoiUtils.customExportExcel(list, customExportParamList, "不良类别代码导出信息", "不良类别代码信息", "不良类别代码.xls", response);

    }

    @ApiOperation(value = "接口新增或更新",notes = "接口新增或更新")
    @PostMapping("/saveByApi")
    public ResponseEntity<BaseBadnessCategory> saveByApi(@ApiParam(value = "必传：routeCode、organizationId",required = true)@RequestBody @Validated BaseBadnessCategory baseBadnessCategory) {
        BaseBadnessCategory baseBadnessCategorys = baseBadnessCategoryService.saveByApi(baseBadnessCategory);
        return ControllerUtil.returnDataSuccess(baseBadnessCategorys, StringUtils.isEmpty(baseBadnessCategorys) ? 0 : 1);
    }
}
