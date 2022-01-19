package com.fantechs.provider.wms.inner.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerJobOrderDetDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrderDet;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerJobOrderDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.inner.service.WmsInnerShiftWorkDetService;
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
 * Created by Mr.Lei on 2021/05/06.
 */
@RestController
@Api(tags = "上架作业明细")
@RequestMapping("/wmsInnerShiftWorkDet")
@Validated
public class WmsInnerShiftWorkServiceDetController {

    @Resource
    private WmsInnerShiftWorkDetService wmsInnerShiftWorkDetService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WmsInnerJobOrderDet wmsInPutawayOrderDet) {
        return ControllerUtil.returnCRUD(wmsInnerShiftWorkDetService.save(wmsInPutawayOrderDet));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(wmsInnerShiftWorkDetService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value= WmsInnerJobOrderDet.update.class) WmsInnerJobOrderDet wmsInPutawayOrderDet) {
        return ControllerUtil.returnCRUD(wmsInnerShiftWorkDetService.update(wmsInPutawayOrderDet));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<WmsInnerJobOrderDet> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        WmsInnerJobOrderDet wmsInPutawayOrderDet = wmsInnerShiftWorkDetService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(wmsInPutawayOrderDet,StringUtils.isEmpty(wmsInPutawayOrderDet)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WmsInnerJobOrderDetDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInnerJobOrderDet searchWmsInPutawayOrderDet) {
        Page<Object> page = PageHelper.startPage(searchWmsInPutawayOrderDet.getStartPage(),searchWmsInPutawayOrderDet.getPageSize());
        List<WmsInnerJobOrderDetDto> list = wmsInnerShiftWorkDetService.findList(searchWmsInPutawayOrderDet);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchWmsInnerJobOrderDet searchWmsInPutawayOrderDet){
    List<WmsInnerJobOrderDetDto> list = wmsInnerShiftWorkDetService.findList(searchWmsInPutawayOrderDet);
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "WmsInPutawayOrderDet信息", WmsInnerJobOrderDetDto.class, "WmsInPutawayOrderDet.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

    @ApiOperation(value = "领料单发运校验",notes = "领料单发运校验")
    @PostMapping("/pickDisQty")
    public ResponseEntity pickDisQty(@ApiParam(value = "必传：",required = true)@RequestBody @Validated List<WmsInnerJobOrderDet> wmsInPutawayOrderDet) {
        return ControllerUtil.returnCRUD(wmsInnerShiftWorkDetService.pickDisQty(wmsInPutawayOrderDet));
    }
}
