package com.fantechs.provider.wms.out.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.out.WmsOutShippingNoteDetDto;
import com.fantechs.common.base.general.entity.wms.out.WmsOutShippingNoteDet;
import com.fantechs.common.base.general.entity.wms.out.search.SearchWmsOutShippingNoteDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.out.service.WmsOutShippingNoteDetService;
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
 * Created by leifengzhi on 2021/01/09.
 */
@RestController
@Api(tags = "出货通知单明细控制器")
@RequestMapping("/wmsOutShippingNoteDet")
@Validated
public class WmsOutShippingNoteDetController {

    @Resource
    private WmsOutShippingNoteDetService wmsOutShippingNoteDetService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WmsOutShippingNoteDet wmsOutShippingNoteDet) {
        return ControllerUtil.returnCRUD(wmsOutShippingNoteDetService.save(wmsOutShippingNoteDet));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(wmsOutShippingNoteDetService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=WmsOutShippingNoteDet.update.class) WmsOutShippingNoteDet wmsOutShippingNoteDet) {
        return ControllerUtil.returnCRUD(wmsOutShippingNoteDetService.update(wmsOutShippingNoteDet));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<WmsOutShippingNoteDet> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        WmsOutShippingNoteDet  wmsOutShippingNoteDet = wmsOutShippingNoteDetService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(wmsOutShippingNoteDet,StringUtils.isEmpty(wmsOutShippingNoteDet)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WmsOutShippingNoteDetDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWmsOutShippingNoteDet searchWmsOutShippingNoteDet) {
        Page<Object> page = PageHelper.startPage(searchWmsOutShippingNoteDet.getStartPage(),searchWmsOutShippingNoteDet.getPageSize());
        List<WmsOutShippingNoteDetDto> list = wmsOutShippingNoteDetService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsOutShippingNoteDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

//    @ApiOperation("历史列表")
//    @PostMapping("/findHtList")
//    public ResponseEntity<List<WmsOutShippingNoteDet>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchWmsOutShippingNoteDet searchWmsOutShippingNoteDet) {
//        Page<Object> page = PageHelper.startPage(searchWmsOutShippingNoteDet.getStartPage(),searchWmsOutShippingNoteDet.getPageSize());
//        List<WmsOutShippingNoteDet> list = wmsOutShippingNoteDetService.findList(searchWmsOutShippingNoteDet);
//        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
//    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchWmsOutShippingNoteDet searchWmsOutShippingNoteDet){
    List<WmsOutShippingNoteDetDto> list = wmsOutShippingNoteDetService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsOutShippingNoteDet));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "WmsOutShippingNoteDet信息", WmsOutShippingNoteDetDto.class, "WmsOutShippingNoteDet.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
