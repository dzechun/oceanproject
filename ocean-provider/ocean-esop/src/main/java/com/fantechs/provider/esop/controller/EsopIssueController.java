package com.fantechs.provider.esop.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.esop.EsopIssueDto;
import com.fantechs.common.base.general.entity.esop.EsopIssue;
import com.fantechs.common.base.general.entity.esop.history.EsopHtIssue;
import com.fantechs.common.base.general.entity.esop.search.SearchEsopIssue;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.esop.service.EsopHtIssueService;
import com.fantechs.provider.esop.service.EsopIssueService;
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
@RequestMapping("/esopIssue")
@Validated
public class EsopIssueController {

    @Resource
    private EsopIssueService esopIssueService;
    @Resource
    private EsopHtIssueService esopHtIssueService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated EsopIssue esopIssue) {
        return ControllerUtil.returnCRUD(esopIssueService.save(esopIssue));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(esopIssueService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=EsopIssue.update.class) EsopIssue esopIssue) {
        return ControllerUtil.returnCRUD(esopIssueService.update(esopIssue));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<EsopIssue> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        EsopIssue  esopIssue = esopIssueService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(esopIssue,StringUtils.isEmpty(esopIssue)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<EsopIssueDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchEsopIssue searchEsopIssue) {
        Page<Object> page = PageHelper.startPage(searchEsopIssue.getStartPage(),searchEsopIssue.getPageSize());
        List<EsopIssueDto> list = esopIssueService.findList(ControllerUtil.dynamicConditionByEntity(searchEsopIssue));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<EsopHtIssue>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchEsopIssue searchEsopIssue) {
        Page<Object> page = PageHelper.startPage(searchEsopIssue.getStartPage(),searchEsopIssue.getPageSize());
        List<EsopHtIssue> list = esopHtIssueService.findHtList(ControllerUtil.dynamicConditionByEntity(searchEsopIssue));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stresop")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchEsopIssue searchEsopIssue){
    List<EsopIssueDto> list = esopIssueService.findList(ControllerUtil.dynamicConditionByEntity(searchEsopIssue));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "问题清单", EsopIssueDto.class, "问题清单.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

    @ApiOperation(value = "批量新增",notes = "批量新增")
    @PostMapping("/batchAdd")
    public ResponseEntity batchAdd(@ApiParam(value = "必传：",required = true)@RequestBody @Validated List<EsopIssue> esopIssues) {
        return ControllerUtil.returnCRUD(esopIssueService.batchAdd(esopIssues));
    }
}
