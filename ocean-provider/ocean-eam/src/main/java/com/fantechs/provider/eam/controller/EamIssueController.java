package com.fantechs.provider.eam.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamIssueDto;
import com.fantechs.common.base.general.entity.eam.EamIssue;
import com.fantechs.common.base.general.entity.eam.history.EamHtIssue;
import com.fantechs.common.base.general.entity.eam.search.SearchEamIssue;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.service.EamHtIssueService;
import com.fantechs.provider.eam.service.EamIssueService;
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
 * Created by leifengzhi on 2021/07/07.
 */
@RestController
@Api(tags = "问题清单")
@RequestMapping("/eamIssue")
@Validated
public class EamIssueController {

    @Resource
    private EamIssueService eamIssueService;
    @Resource
    private EamHtIssueService eamHtIssueService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated EamIssue eamIssue) {
        return ControllerUtil.returnCRUD(eamIssueService.save(eamIssue));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(eamIssueService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=EamIssue.update.class) EamIssue eamIssue) {
        return ControllerUtil.returnCRUD(eamIssueService.update(eamIssue));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<EamIssue> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        EamIssue  eamIssue = eamIssueService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(eamIssue,StringUtils.isEmpty(eamIssue)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<EamIssueDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchEamIssue searchEamIssue) {
        Page<Object> page = PageHelper.startPage(searchEamIssue.getStartPage(),searchEamIssue.getPageSize());
        List<EamIssueDto> list = eamIssueService.findList(ControllerUtil.dynamicConditionByEntity(searchEamIssue));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<EamHtIssue>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchEamIssue searchEamIssue) {
        Page<Object> page = PageHelper.startPage(searchEamIssue.getStartPage(),searchEamIssue.getPageSize());
        List<EamHtIssue> list = eamHtIssueService.findHtList(ControllerUtil.dynamicConditionByEntity(searchEamIssue));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchEamIssue searchEamIssue){
    List<EamIssueDto> list = eamIssueService.findList(ControllerUtil.dynamicConditionByEntity(searchEamIssue));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "问题清单", EamIssueDto.class, "问题清单.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
