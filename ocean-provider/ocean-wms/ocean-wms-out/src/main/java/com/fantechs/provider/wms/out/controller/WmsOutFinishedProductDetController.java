package com.fantechs.provider.wms.out.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.out.WmsOutFinishedProductDetDto;
import com.fantechs.common.base.general.entity.wms.out.WmsOutFinishedProductDet;
import com.fantechs.common.base.general.entity.wms.out.history.WmsOutHtFinishedProductDet;
import com.fantechs.common.base.general.entity.wms.out.search.SearchWmsOutFinishedProductDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;

import com.fantechs.provider.wms.out.service.WmsOutFinishedProductDetService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2020/12/23.
 */
@RestController
@Api(tags = "成品出库明细控制器")
@RequestMapping("/wmsOutFinishedProductDet")
@Validated
public class WmsOutFinishedProductDetController {

    @Resource
    private WmsOutFinishedProductDetService wmsOutFinishedProductDetService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WmsOutFinishedProductDet wmsOutFinishedProductDet) {
        return ControllerUtil.returnCRUD(wmsOutFinishedProductDetService.save(wmsOutFinishedProductDet));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(wmsOutFinishedProductDetService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=WmsOutFinishedProductDet.update.class) WmsOutFinishedProductDet wmsOutFinishedProductDet) {
        return ControllerUtil.returnCRUD(wmsOutFinishedProductDetService.update(wmsOutFinishedProductDet));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<WmsOutFinishedProductDet> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        WmsOutFinishedProductDet  wmsOutFinishedProductDet = wmsOutFinishedProductDetService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(wmsOutFinishedProductDet,StringUtils.isEmpty(wmsOutFinishedProductDet)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WmsOutFinishedProductDetDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWmsOutFinishedProductDet searchWmsOutFinishedProductDet) {
        Page<Object> page = PageHelper.startPage(searchWmsOutFinishedProductDet.getStartPage(),searchWmsOutFinishedProductDet.getPageSize());
        List<WmsOutFinishedProductDetDto> list = wmsOutFinishedProductDetService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsOutFinishedProductDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<WmsOutHtFinishedProductDet>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchWmsOutFinishedProductDet searchWmsOutFinishedProductDet) {
        Page<Object> page = PageHelper.startPage(searchWmsOutFinishedProductDet.getStartPage(),searchWmsOutFinishedProductDet.getPageSize());
        List<WmsOutHtFinishedProductDet> list = wmsOutFinishedProductDetService.findHTList(ControllerUtil.dynamicConditionByEntity(searchWmsOutFinishedProductDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchWmsOutFinishedProductDet searchWmsOutFinishedProductDet){
    List<WmsOutFinishedProductDetDto> list = wmsOutFinishedProductDetService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsOutFinishedProductDet));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "WmsOutFinishedProductDet信息", WmsOutFinishedProductDetDto.class, "WmsOutFinishedProductDet.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
