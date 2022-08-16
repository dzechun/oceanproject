package com.fantechs.provider.base.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.basic.imports.BaseWorkshopSectionImport;
import com.fantechs.common.base.general.entity.basic.BaseProLine;
import com.fantechs.common.base.general.entity.basic.BaseWorkshopSection;
import com.fantechs.common.base.general.entity.basic.history.BaseHtWorkshopSection;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseWorkshopSection;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseHtWorkshopSectionService;
import com.fantechs.provider.base.service.BaseWorkshopSectionService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
 * Created by Mr.Lei on 2020/09/25.
 */
@RestController
@Api(tags = "工段信息管理")
@RequestMapping("/baseWorkshopSection")
@Validated
@Slf4j
public class BaseWorkshopSectionController {

    @Resource
    private BaseWorkshopSectionService baseWorkshopSectionService;
    @Resource
    private BaseHtWorkshopSectionService baseHtWorkshopSectionService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：sectionCode、sectionName",required = true)@RequestBody @Validated BaseWorkshopSection baseWorkshopSection) {
        return ControllerUtil.returnCRUD(baseWorkshopSectionService.save(baseWorkshopSection));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message = "ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseWorkshopSectionService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value = BaseWorkshopSection.update.class) BaseWorkshopSection baseWorkshopSection) {
        return ControllerUtil.returnCRUD(baseWorkshopSectionService.update(baseWorkshopSection));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseWorkshopSection> detail(@ApiParam(value = "ID",required = true)@RequestParam @NotNull(message = "id不能为空") Long id) {
        BaseWorkshopSection baseWorkshopSection = baseWorkshopSectionService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseWorkshopSection,StringUtils.isEmpty(baseWorkshopSection)?0:1);
    }

    @ApiOperation("根据条件查询工段信息列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseWorkshopSection>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseWorkshopSection searchBaseWorkshopSection) {
        Page<Object> page = PageHelper.startPage(searchBaseWorkshopSection.getStartPage(), searchBaseWorkshopSection.getPageSize());
        List<BaseWorkshopSection> list = baseWorkshopSectionService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseWorkshopSection));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("/工段历史记录")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseHtWorkshopSection>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchBaseWorkshopSection searchBaseWorkshopSection){
        Page<Object> page = PageHelper.startPage(searchBaseWorkshopSection.getStartPage(), searchBaseWorkshopSection.getPageSize());
        List<BaseHtWorkshopSection> list = baseHtWorkshopSectionService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseWorkshopSection));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchBaseWorkshopSection searchBaseWorkshopSection){
        List<BaseWorkshopSection> list = baseWorkshopSectionService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseWorkshopSection));
        try {
            // 导出操作
            EasyPoiUtils.exportExcel(list, "工段信息导出", "工段信息", BaseWorkshopSection.class, "工段信息.xls", response);
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
            List<BaseWorkshopSectionImport> baseWorkshopSectionImports = EasyPoiUtils.importExcel(file, 2, 1, BaseWorkshopSectionImport.class);
            Map<String, Object> resultMap = baseWorkshopSectionService.importExcel(baseWorkshopSectionImports);
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
    public ResponseEntity<BaseWorkshopSection> addOrUpdate(@ApiParam(value = "必传：productBomCode、materialId",required = true)@RequestBody @Validated BaseWorkshopSection baseWorkshopSection) {
        BaseWorkshopSection baseWorkshopSections = baseWorkshopSectionService.addOrUpdate(baseWorkshopSection);
        return ControllerUtil.returnDataSuccess(baseWorkshopSections, StringUtils.isEmpty(baseWorkshopSections) ? 0 : 1);
    }
}
