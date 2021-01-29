package com.fantechs.provider.imes.basic.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.basic.SmtFactoryDto;
import com.fantechs.common.base.entity.basic.SmtWorkshopSection;
import com.fantechs.common.base.entity.basic.history.SmtHtWorkshopSection;
import com.fantechs.common.base.entity.basic.search.SearchSmtWorkshopSection;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.basic.service.SmtHtWorkshopSectionService;
import com.fantechs.provider.imes.basic.service.SmtWorkshopSectionService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by Mr.Lei on 2020/09/25.
 */
@RestController
@Api(tags = "工段信息管理")
@RequestMapping("/workshopSection")
@Validated
@Slf4j
public class SmtWorkshopSectionController {

    @Autowired
    private SmtWorkshopSectionService smtWorkshopSectionService;
    @Autowired
    private SmtHtWorkshopSectionService smtHtWorkshopSectionService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：sectionCode、sectionName",required = true)@RequestBody @Validated SmtWorkshopSection smtWorkshopSection) {
        return ControllerUtil.returnCRUD(smtWorkshopSectionService.save(smtWorkshopSection));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message = "ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(smtWorkshopSectionService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value = SmtWorkshopSection.update.class) SmtWorkshopSection smtWorkshopSection) {
        return ControllerUtil.returnCRUD(smtWorkshopSectionService.update(smtWorkshopSection));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SmtWorkshopSection> detail(@ApiParam(value = "ID",required = true)@RequestParam @NotNull(message = "id不能为空") Long id) {
        SmtWorkshopSection smtWorkshopSection = smtWorkshopSectionService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(smtWorkshopSection,StringUtils.isEmpty(smtWorkshopSection)?0:1);
    }

    @ApiOperation("根据条件查询工段信息列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SmtWorkshopSection>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSmtWorkshopSection searchSmtWorkshopSection) {
        Page<Object> page = PageHelper.startPage(searchSmtWorkshopSection.getStartPage(), searchSmtWorkshopSection.getPageSize());
        List<SmtWorkshopSection> list = smtWorkshopSectionService.findList(searchSmtWorkshopSection);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("/工段历史记录")
    @PostMapping("/findHtList")
    public ResponseEntity<List<SmtHtWorkshopSection>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchSmtWorkshopSection searchSmtWorkshopSection){
        Page<Object> page = PageHelper.startPage(searchSmtWorkshopSection.getStartPage(), searchSmtWorkshopSection.getPageSize());
        List<SmtHtWorkshopSection> list = smtHtWorkshopSectionService.findList(searchSmtWorkshopSection);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchSmtWorkshopSection searchSmtWorkshopSection){
        List<SmtWorkshopSection> list = smtWorkshopSectionService.findList(searchSmtWorkshopSection);
        try {
            // 导出操作
            EasyPoiUtils.exportExcel(list, "工段信息导出", "工段信息", SmtWorkshopSection.class, "工段信息.xls", response);
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
            List<SmtWorkshopSection> smtWorkshopSections = EasyPoiUtils.importExcel(file, SmtWorkshopSection.class);
            Map<String, Object> resultMap = smtWorkshopSectionService.importExcel(smtWorkshopSections);
            return ControllerUtil.returnDataSuccess("操作结果集",resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.OPT20012002.getCode());
        }
    }
}
