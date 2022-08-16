package com.fantechs.provider.base.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BasePlatePartsDetDto;
import com.fantechs.common.base.general.entity.basic.BasePlatePartsDet;
import com.fantechs.common.base.general.entity.basic.search.SearchBasePlatePartsDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BasePlatePartsDetService;
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


@RestController
@Api(tags = "部件组成明细")
@RequestMapping("/basePlatePartsDet")
@Validated
public class BasePlatePartsDetController {

    @Autowired
    private BasePlatePartsDetService basePlatePartsDetService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated BasePlatePartsDet basePlatePartsDet) {
        return ControllerUtil.returnCRUD(basePlatePartsDetService.save(basePlatePartsDet));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(basePlatePartsDetService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=BasePlatePartsDet.update.class) BasePlatePartsDet basePlatePartsDet) {
        return ControllerUtil.returnCRUD(basePlatePartsDetService.update(basePlatePartsDet));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BasePlatePartsDet> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BasePlatePartsDet  basePlatePartsDet = basePlatePartsDetService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(basePlatePartsDet,StringUtils.isEmpty(basePlatePartsDet)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BasePlatePartsDetDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBasePlatePartsDet searchBasePlatePartsDet) {
        Page<Object> page = PageHelper.startPage(searchBasePlatePartsDet.getStartPage(),searchBasePlatePartsDet.getPageSize());
        List<BasePlatePartsDetDto> list =  basePlatePartsDetService.findList(ControllerUtil.dynamicConditionByEntity(searchBasePlatePartsDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

//    @ApiOperation("历史列表")
//    @PostMapping("/findHtList")
//    public ResponseEntity<List<BasePlatePartsDet>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchBasePlatePartsDet searchBasePlatePartsDet) {
//        Page<Object> page = PageHelper.startPage(searchBasePlatePartsDet.getStartPage(),searchBasePlatePartsDet.getPageSize());
//        List<BasePlatePartsDet> list = basePlatePartsDetService.findHtList(ControllerUtil.dynamicConditionByEntity(searchBasePlatePartsDet));
//        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
//    }

//    @PostMapping(value = "/export")
//    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
//    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
//    @RequestBody(required = false) SearchBasePlatePartsDet searchBasePlatePartsDet){
//    List<BasePlatePartsDetDto> list = basePlatePartsDetService.findList(ControllerUtil.dynamicConditionByEntity(searchBasePlatePartsDet));
//    try {
//        // 导出操作
//        EasyPoiUtils.exportExcel(list, "导出信息", "BasePlatePartsDet信息", BasePlatePartsDet.class, "BasePlatePartsDet.xls", response);
//        } catch (Exception e) {
//        throw new BizErrorException(e);
//        }
//    }
}
