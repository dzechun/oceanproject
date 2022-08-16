package com.fantechs.provider.base.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseLabelCategoryDto;
import com.fantechs.common.base.general.entity.basic.BaseLabelCategory;
import com.fantechs.common.base.general.entity.basic.history.BaseHtLabelCategory;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseLabelCategory;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseHtLabelCategoryService;
import com.fantechs.provider.base.service.BaseLabelCategoryService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
* @author Mr.Lei
* @create 2020/12/17.
*/
@RestController
@Api(tags = "标签类别")
@RequestMapping("/baseLabelCategory")
@Validated
public class BaseLabelCategoryController {

    @Resource
    private BaseLabelCategoryService baseLabelCategoryService;
    @Resource
    private BaseHtLabelCategoryService baseHtLabelCategoryService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated BaseLabelCategory baseLabelCategory) {
        return ControllerUtil.returnCRUD(baseLabelCategoryService.save(baseLabelCategory));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseLabelCategoryService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value= BaseLabelCategory.update.class) BaseLabelCategory baseLabelCategory) {
        return ControllerUtil.returnCRUD(baseLabelCategoryService.update(baseLabelCategory));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseLabelCategory> detail(@ApiParam(value = "ID",required = true) @RequestParam(value = "id") Long id) {
        BaseLabelCategory baseLabelCategory = baseLabelCategoryService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseLabelCategory,StringUtils.isEmpty(baseLabelCategory)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseLabelCategoryDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseLabelCategory searchBaseLabelCategory) {
        Page<Object> page = PageHelper.startPage(searchBaseLabelCategory.getStartPage(), searchBaseLabelCategory.getPageSize());
        List<BaseLabelCategoryDto> list = baseLabelCategoryService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseLabelCategory));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("标签类别列表")
    @PostMapping("/findListByIds")
    public ResponseEntity<List<BaseLabelCategory>> findListByIds(@ApiParam(value = "查询对象")@RequestBody List<Long> ids) {
        List<BaseLabelCategory> baseLabelCategoryList = baseLabelCategoryService.findListByIDs(ids);
        return ControllerUtil.returnDataSuccess(baseLabelCategoryList, baseLabelCategoryList.size());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseHtLabelCategory>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchBaseLabelCategory searchBaseLabelCategory) {
        Page<Object> page = PageHelper.startPage(searchBaseLabelCategory.getStartPage(), searchBaseLabelCategory.getPageSize());
        List<BaseHtLabelCategory> list = baseHtLabelCategoryService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseLabelCategory));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchBaseLabelCategory searchBaseLabelCategory){
    List<BaseLabelCategoryDto> list = baseLabelCategoryService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseLabelCategory));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "标签类别信息", BaseLabelCategoryDto.class, "标签类别.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
