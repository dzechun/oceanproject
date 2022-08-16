package com.fantechs.provider.base.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.basic.BaseProcessCategoryDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseProcessCategoryImport;
import com.fantechs.common.base.general.entity.basic.BaseProcessCategory;
import com.fantechs.common.base.general.entity.basic.history.BaseHtProcessCategory;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProcessCategory;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseHtProcessCategoryService;
import com.fantechs.provider.base.service.BaseProcessCategoryService;
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
import java.util.NoSuchElementException;

/**
 *
 * Created by leifengzhi on 2020/10/15.
 */
@RestController
@Api(tags = "工序类别信息管理")
@RequestMapping("/baseProcessCategory")
@Validated
@Slf4j
public class BaseProcessCategoryController {

    @Resource
    private BaseProcessCategoryService baseProcessCategoryService;
    @Resource
    private BaseHtProcessCategoryService baseHtProcessCategoryService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：processCategoryCode、processCategoryName",required = true)@RequestBody @Validated BaseProcessCategory baseProcessCategory) {
        return ControllerUtil.returnCRUD(baseProcessCategoryService.save(baseProcessCategory));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseProcessCategoryService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value= BaseProcessCategory.update.class) BaseProcessCategory baseProcessCategory) {
        return ControllerUtil.returnCRUD(baseProcessCategoryService.update(baseProcessCategory));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseProcessCategory> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BaseProcessCategory baseProcessCategory = baseProcessCategoryService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseProcessCategory,StringUtils.isEmpty(baseProcessCategory)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseProcessCategoryDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseProcessCategory searchBaseProcessCategory) {
        Page<Object> page = PageHelper.startPage(searchBaseProcessCategory.getStartPage(), searchBaseProcessCategory.getPageSize());
        List<BaseProcessCategoryDto> list = baseProcessCategoryService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseProcessCategory));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseHtProcessCategory>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchBaseProcessCategory searchBaseProcessCategory) {
        Page<Object> page = PageHelper.startPage(searchBaseProcessCategory.getStartPage(), searchBaseProcessCategory.getPageSize());

        Map<String,Object> map = ControllerUtil.dynamicConditionByEntity(searchBaseProcessCategory);
        List<BaseHtProcessCategory> list = baseHtProcessCategoryService.findHtList(ControllerUtil.dynamicConditionByEntity(searchBaseProcessCategory));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchBaseProcessCategory searchBaseProcessCategory){
    List<BaseProcessCategoryDto> list = baseProcessCategoryService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseProcessCategory));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "工序类别信息", "SmtProcessCategory信息", BaseProcessCategoryDto.class, "SmtProcessCategory.xls", response);
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
    @ApiOperation(value = "从excel导入电子标签信息",notes = "从excel导入电子标签信息")
    public ResponseEntity importExcel(@ApiParam(value ="输入excel文件",required = true)
                                      @RequestPart(value="file") MultipartFile file){
        try {
            // 导入操作
            List<BaseProcessCategoryImport> baseProcessCategoryImportList = EasyPoiUtils.importExcel(file,2,1, BaseProcessCategoryImport.class);
            Map<String, Object> resultMap = baseProcessCategoryService.importExcel(baseProcessCategoryImportList);
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

    @ApiOperation(value = "接口新增或更新",notes = "接口新增或更新")
    @PostMapping("/addOrUpdate")
    public ResponseEntity<BaseProcessCategory> addOrUpdate(@ApiParam(value = "必传：productBomCode、materialId",required = true)@RequestBody @Validated BaseProcessCategory baseProcessCategory) {
        BaseProcessCategory baseProcessCategorys = baseProcessCategoryService.addOrUpdate(baseProcessCategory);
        return ControllerUtil.returnDataSuccess(baseProcessCategorys, StringUtils.isEmpty(baseProcessCategorys) ? 0 : 1);
    }
}
