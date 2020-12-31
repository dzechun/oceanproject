package com.fantechs.provider.imes.basic.controller;

import com.fantechs.common.base.dto.basic.SmtMaterialCategoryDto;
import com.fantechs.common.base.entity.basic.SmtMaterialCategory;
import com.fantechs.common.base.entity.basic.history.SmtHtMaterialCategory;
import com.fantechs.common.base.entity.basic.search.SearchSmtMaterialCategory;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.basic.service.SmtHtMaterialCategoryService;
import com.fantechs.provider.imes.basic.service.SmtMaterialCategoryService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/smtMaterialCategory")
@Validated
public class SmtMaterialCategoryController {

    @Autowired
    private SmtMaterialCategoryService smtMaterialCategoryService;
    @Autowired
    private SmtHtMaterialCategoryService smtHtMaterialCategoryService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated SmtMaterialCategory smtMaterialCategory) {
        return ControllerUtil.returnCRUD(smtMaterialCategoryService.save(smtMaterialCategory));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(smtMaterialCategoryService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=SmtMaterialCategory.update.class) SmtMaterialCategory smtMaterialCategory) {
        return ControllerUtil.returnCRUD(smtMaterialCategoryService.update(smtMaterialCategory));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SmtMaterialCategory> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        SmtMaterialCategory  smtMaterialCategory = smtMaterialCategoryService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(smtMaterialCategory,StringUtils.isEmpty(smtMaterialCategory)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SmtMaterialCategoryDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSmtMaterialCategory searchSmtMaterialCategory) {
        Page<Object> page = PageHelper.startPage(searchSmtMaterialCategory.getStartPage(),searchSmtMaterialCategory.getPageSize());
        List<SmtMaterialCategoryDto> list = smtMaterialCategoryService.findList(ControllerUtil.dynamicConditionByEntity(searchSmtMaterialCategory));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<SmtHtMaterialCategory>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchSmtMaterialCategory searchSmtMaterialCategory) {
        Page<Object> page = PageHelper.startPage(searchSmtMaterialCategory.getStartPage(),searchSmtMaterialCategory.getPageSize());
        List<SmtHtMaterialCategory> list = smtHtMaterialCategoryService.findHtList(ControllerUtil.dynamicConditionByEntity(searchSmtMaterialCategory));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchSmtMaterialCategory searchSmtMaterialCategory){
    List<SmtMaterialCategoryDto> list = smtMaterialCategoryService.findList(ControllerUtil.dynamicConditionByEntity(searchSmtMaterialCategory));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "SmtMaterialCategory信息", SmtMaterialCategory.class, "SmtMaterialCategory.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
