package com.fantechs.provider.imes.basic.controller;

import com.fantechs.common.base.dto.basic.SmtProcessCategoryDto;
import com.fantechs.common.base.entity.basic.SmtProcessCategory;
import com.fantechs.common.base.entity.basic.history.SmtHtProcessCategory;
import com.fantechs.common.base.entity.basic.search.SearchSmtProcessCategory;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.basic.service.SmtHtProcessCategoryService;
import com.fantechs.provider.imes.basic.service.SmtProcessCategoryService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/10/15.
 */
@RestController
@Api(tags = "工序类别管理")
@RequestMapping("/smtProcessCategory")
@Validated
public class SmtProcessCategoryController {

    @Autowired
    private SmtProcessCategoryService smtProcessCategoryService;
    @Autowired
    private SmtHtProcessCategoryService smtHtProcessCategoryService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：processCategoryCode、processCategoryName",required = true)@RequestBody @Validated SmtProcessCategory smtProcessCategory) {
        return ControllerUtil.returnCRUD(smtProcessCategoryService.save(smtProcessCategory));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(smtProcessCategoryService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=SmtProcessCategory.update.class) SmtProcessCategory smtProcessCategory) {
        return ControllerUtil.returnCRUD(smtProcessCategoryService.update(smtProcessCategory));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SmtProcessCategory> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        SmtProcessCategory  smtProcessCategory = smtProcessCategoryService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(smtProcessCategory,StringUtils.isEmpty(smtProcessCategory)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SmtProcessCategoryDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSmtProcessCategory searchSmtProcessCategory) {
        Page<Object> page = PageHelper.startPage(searchSmtProcessCategory.getStartPage(),searchSmtProcessCategory.getPageSize());
        List<SmtProcessCategoryDto> list = smtProcessCategoryService.findList(ControllerUtil.dynamicConditionByEntity(searchSmtProcessCategory));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<SmtHtProcessCategory>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchSmtProcessCategory searchSmtProcessCategory) {
        Page<Object> page = PageHelper.startPage(searchSmtProcessCategory.getStartPage(),searchSmtProcessCategory.getPageSize());

        Map<String,Object> map = ControllerUtil.dynamicConditionByEntity(searchSmtProcessCategory);
        List<SmtHtProcessCategory> list = smtHtProcessCategoryService.findHtList(ControllerUtil.dynamicConditionByEntity(searchSmtProcessCategory));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchSmtProcessCategory searchSmtProcessCategory){
    List<SmtProcessCategoryDto> list = smtProcessCategoryService.findList(ControllerUtil.dynamicConditionByEntity(searchSmtProcessCategory));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "SmtProcessCategory信息", SmtProcessCategory.class, "SmtProcessCategory.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
