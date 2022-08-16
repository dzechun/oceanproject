package com.fantechs.provider.base.controller;

import com.fantechs.common.base.general.dto.basic.BaseMaterialCategoryDto;
import com.fantechs.common.base.general.entity.basic.BaseMaterialCategory;
import com.fantechs.common.base.general.entity.basic.history.BaseHtMaterialCategory;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterialCategory;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseHtMaterialCategoryService;
import com.fantechs.provider.base.service.BaseMaterialCategoryService;
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

/**
 *
 * Created by leifengzhi on 2020/12/31.
 */
@RestController
@Api(tags = "物料类别")
@RequestMapping("/baseMaterialCategory")
@Validated
public class BaseMaterialCategoryController {

    @Resource
    private BaseMaterialCategoryService baseMaterialCategoryService;
    @Resource
    private BaseHtMaterialCategoryService baseHtMaterialCategoryService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated BaseMaterialCategory baseMaterialCategory) {
        return ControllerUtil.returnCRUD(baseMaterialCategoryService.save(baseMaterialCategory));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseMaterialCategoryService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value= BaseMaterialCategory.update.class) BaseMaterialCategory baseMaterialCategory) {
        return ControllerUtil.returnCRUD(baseMaterialCategoryService.update(baseMaterialCategory));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseMaterialCategory> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BaseMaterialCategory baseMaterialCategory = baseMaterialCategoryService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseMaterialCategory,StringUtils.isEmpty(baseMaterialCategory)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseMaterialCategoryDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseMaterialCategory searchBaseMaterialCategory) {
        Page<Object> page = PageHelper.startPage(searchBaseMaterialCategory.getStartPage(), searchBaseMaterialCategory.getPageSize());
        List<BaseMaterialCategoryDto> list = baseMaterialCategoryService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseMaterialCategory));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseHtMaterialCategory>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchBaseMaterialCategory searchBaseMaterialCategory) {
        Page<Object> page = PageHelper.startPage(searchBaseMaterialCategory.getStartPage(), searchBaseMaterialCategory.getPageSize());
        List<BaseHtMaterialCategory> list = baseHtMaterialCategoryService.findHtList(ControllerUtil.dynamicConditionByEntity(searchBaseMaterialCategory));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchBaseMaterialCategory searchBaseMaterialCategory){
    List<BaseMaterialCategoryDto> list = baseMaterialCategoryService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseMaterialCategory));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "物料类别", BaseMaterialCategory.class, "物料类别.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
