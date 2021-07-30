package com.fantechs.provider.eam.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamJigRequisitionDto;
import com.fantechs.common.base.general.entity.eam.EamJigRequisition;
import com.fantechs.common.base.general.entity.eam.history.EamHtJigRequisition;
import com.fantechs.common.base.general.entity.eam.search.SearchEamJigRequisition;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.service.EamHtJigRequisitionService;
import com.fantechs.provider.eam.service.EamJigRequisitionService;
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
 * Created by leifengzhi on 2021/07/30.
 */
@RestController
@Api(tags = "治具领用")
@RequestMapping("/eamJigRequisition")
@Validated
public class EamJigRequisitionController {

    @Resource
    private EamJigRequisitionService eamJigRequisitionService;
    @Resource
    private EamHtJigRequisitionService eamHtJigRequisitionService;


    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated EamJigRequisition eamJigRequisition) {
        return ControllerUtil.returnCRUD(eamJigRequisitionService.save(eamJigRequisition));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(eamJigRequisitionService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=EamJigRequisition.update.class) EamJigRequisition eamJigRequisition) {
        return ControllerUtil.returnCRUD(eamJigRequisitionService.update(eamJigRequisition));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<EamJigRequisition> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        EamJigRequisition  eamJigRequisition = eamJigRequisitionService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(eamJigRequisition,StringUtils.isEmpty(eamJigRequisition)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<EamJigRequisitionDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchEamJigRequisition searchEamJigRequisition) {
        Page<Object> page = PageHelper.startPage(searchEamJigRequisition.getStartPage(),searchEamJigRequisition.getPageSize());
        List<EamJigRequisitionDto> list = eamJigRequisitionService.findList(ControllerUtil.dynamicConditionByEntity(searchEamJigRequisition));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<EamHtJigRequisition>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchEamJigRequisition searchEamJigRequisition) {
        Page<Object> page = PageHelper.startPage(searchEamJigRequisition.getStartPage(),searchEamJigRequisition.getPageSize());
        List<EamHtJigRequisition> list = eamHtJigRequisitionService.findHtList(ControllerUtil.dynamicConditionByEntity(searchEamJigRequisition));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchEamJigRequisition searchEamJigRequisition){
    List<EamJigRequisitionDto> list = eamJigRequisitionService.findList(ControllerUtil.dynamicConditionByEntity(searchEamJigRequisition));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "治具领用", EamJigRequisitionDto.class, "治具领用.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
