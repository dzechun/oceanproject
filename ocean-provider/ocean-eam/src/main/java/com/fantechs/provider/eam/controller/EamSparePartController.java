package com.fantechs.provider.eam.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamSparePartDto;
import com.fantechs.common.base.general.entity.eam.EamSparePart;
import com.fantechs.common.base.general.entity.eam.history.EamHtSparePart;
import com.fantechs.common.base.general.entity.eam.search.SearchEamSparePart;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.service.EamSparePartService;
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
@Api(tags = "备用件")
@RequestMapping("/eamSparePart")
@Validated
public class EamSparePartController {

    @Resource
    private EamSparePartService eamSparePartService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated EamSparePart eamSparePart) {
        return ControllerUtil.returnCRUD(eamSparePartService.save(eamSparePart));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(eamSparePartService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=EamSparePart.update.class) EamSparePart eamSparePart) {
        return ControllerUtil.returnCRUD(eamSparePartService.update(eamSparePart));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<EamSparePart> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        EamSparePart  eamSparePart = eamSparePartService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(eamSparePart,StringUtils.isEmpty(eamSparePart)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<EamSparePartDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchEamSparePart searchEamSparePart) {
        Page<Object> page = PageHelper.startPage(searchEamSparePart.getStartPage(),searchEamSparePart.getPageSize());
        List<EamSparePartDto> list = eamSparePartService.findList(ControllerUtil.dynamicConditionByEntity(searchEamSparePart));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<EamSparePartDto>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchEamSparePart searchEamSparePart) {
        List<EamSparePartDto> list = eamSparePartService.findList(ControllerUtil.dynamicConditionByEntity(searchEamSparePart));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<EamHtSparePart>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchEamSparePart searchEamSparePart) {
        Page<Object> page = PageHelper.startPage(searchEamSparePart.getStartPage(),searchEamSparePart.getPageSize());
        List<EamHtSparePart> list = eamSparePartService.findHtList(ControllerUtil.dynamicConditionByEntity(searchEamSparePart));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchEamSparePart searchEamSparePart){
    List<EamSparePartDto> list = eamSparePartService.findList(ControllerUtil.dynamicConditionByEntity(searchEamSparePart));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "备用件", EamSparePartDto.class, "备用件.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
