package com.fantechs.provider.eam.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.eam.EamSparePartCategory;
import com.fantechs.common.base.general.entity.eam.history.EamHtSparePartCategory;
import com.fantechs.common.base.general.entity.eam.search.SearchEamSparePartCategory;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.service.EamSparePartCategoryService;
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
 * Created by leifengzhi on 2021/09/17.
 */
@RestController
@Api(tags = "备用件类别")
@RequestMapping("/eamSparePartCategory")
@Validated
public class EamSparePartCategoryController {

    @Resource
    private EamSparePartCategoryService eamSparePartCategoryService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated EamSparePartCategory eamSparePartCategory) {
        return ControllerUtil.returnCRUD(eamSparePartCategoryService.save(eamSparePartCategory));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(eamSparePartCategoryService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=EamSparePartCategory.update.class) EamSparePartCategory eamSparePartCategory) {
        return ControllerUtil.returnCRUD(eamSparePartCategoryService.update(eamSparePartCategory));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<EamSparePartCategory> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        EamSparePartCategory  eamSparePartCategory = eamSparePartCategoryService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(eamSparePartCategory,StringUtils.isEmpty(eamSparePartCategory)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<EamSparePartCategory>> findList(@ApiParam(value = "查询对象")@RequestBody SearchEamSparePartCategory searchEamSparePartCategory) {
        Page<Object> page = PageHelper.startPage(searchEamSparePartCategory.getStartPage(),searchEamSparePartCategory.getPageSize());
        List<EamSparePartCategory> list = eamSparePartCategoryService.findList(ControllerUtil.dynamicConditionByEntity(searchEamSparePartCategory));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<EamSparePartCategory>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchEamSparePartCategory searchEamSparePartCategory) {
        List<EamSparePartCategory> list = eamSparePartCategoryService.findList(ControllerUtil.dynamicConditionByEntity(searchEamSparePartCategory));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<EamHtSparePartCategory>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchEamSparePartCategory searchEamSparePartCategory) {
        Page<Object> page = PageHelper.startPage(searchEamSparePartCategory.getStartPage(),searchEamSparePartCategory.getPageSize());
        List<EamHtSparePartCategory> list = eamSparePartCategoryService.findHtList(ControllerUtil.dynamicConditionByEntity(searchEamSparePartCategory));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchEamSparePartCategory searchEamSparePartCategory){
    List<EamSparePartCategory> list = eamSparePartCategoryService.findList(ControllerUtil.dynamicConditionByEntity(searchEamSparePartCategory));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "备用件类别", EamSparePartCategory.class, "备用件类别.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
