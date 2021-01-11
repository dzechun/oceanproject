package com.fantechs.provider.wms.in.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.in.WmsInFinishedProductDetDto;
import com.fantechs.common.base.general.entity.wms.in.WmsInFinishedProductDet;
import com.fantechs.common.base.general.entity.wms.in.search.SearchWmsInFinishedProductDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.in.service.WmsInFinishedProductDetService;
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
 * Created by leifengzhi on 2021/01/07.
 */
@RestController
@Api(tags = "成品入库明细控制器")
@RequestMapping("/wmsInFinishedProductDet")
@Validated
public class WmsInFinishedProductDetController {

    @Resource
    private WmsInFinishedProductDetService wmsInFinishedProductDetService;

//    @ApiOperation(value = "新增",notes = "新增")
//    @PostMapping("/add")
//    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WmsInFinishedProductDet wmsInFinishedProductDet) {
//        return ControllerUtil.returnCRUD(wmsInFinishedProductDetService.save(wmsInFinishedProductDet));
//    }

//    @ApiOperation("删除")
//    @PostMapping("/delete")
//    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
//        return ControllerUtil.returnCRUD(wmsInFinishedProductDetService.batchDelete(ids));
//    }

//    @ApiOperation("修改")
//    @PostMapping("/update")
//    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=WmsInFinishedProductDet.update.class) WmsInFinishedProductDet wmsInFinishedProductDet) {
//        return ControllerUtil.returnCRUD(wmsInFinishedProductDetService.update(wmsInFinishedProductDet));
//    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<WmsInFinishedProductDet> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        WmsInFinishedProductDet  wmsInFinishedProductDet = wmsInFinishedProductDetService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(wmsInFinishedProductDet,StringUtils.isEmpty(wmsInFinishedProductDet)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WmsInFinishedProductDetDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInFinishedProductDet searchWmsInFinishedProductDet) {
        Page<Object> page = PageHelper.startPage(searchWmsInFinishedProductDet.getStartPage(),searchWmsInFinishedProductDet.getPageSize());
        List<WmsInFinishedProductDetDto> list = wmsInFinishedProductDetService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInFinishedProductDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    /*@ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<WmsInFinishedProductDet>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInFinishedProductDet searchWmsInFinishedProductDet) {
        Page<Object> page = PageHelper.startPage(searchWmsInFinishedProductDet.getStartPage(),searchWmsInFinishedProductDet.getPageSize());
        List<WmsInFinishedProductDet> list = wmsInFinishedProductDetService.findList(searchWmsInFinishedProductDet);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }*/

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchWmsInFinishedProductDet searchWmsInFinishedProductDet){
    List<WmsInFinishedProductDetDto> list = wmsInFinishedProductDetService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInFinishedProductDet));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "WmsInFinishedProductDet信息", WmsInFinishedProductDetDto.class, "WmsInFinishedProductDet.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
