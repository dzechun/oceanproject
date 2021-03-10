package com.fantechs.provider.wms.inner.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerTransferSlipDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerTransferSlip;
import com.fantechs.common.base.general.entity.wms.inner.history.WmsInnerHtTransferSlip;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerTransferSlip;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.inner.service.WmsInnerHtTransferSlipService;
import com.fantechs.provider.wms.inner.service.WmsInnerTransferSlipService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/03/05.
 */
@RestController
@Api(tags = "调拨单信息管理")
@RequestMapping("/wmsInnerTransferSlip")
@Validated
public class WmsInnerTransferSlipController {

    @Resource
    private WmsInnerTransferSlipService wmsInnerTransferSlipService;
    @Resource
    private WmsInnerHtTransferSlipService wmsInnerHtTransferSlipService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WmsInnerTransferSlip wmsInnerTransferSlip) {
        return ControllerUtil.returnCRUD(wmsInnerTransferSlipService.save(wmsInnerTransferSlip));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(wmsInnerTransferSlipService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=WmsInnerTransferSlip.update.class) WmsInnerTransferSlip wmsInnerTransferSlip) {
        return ControllerUtil.returnCRUD(wmsInnerTransferSlipService.update(wmsInnerTransferSlip));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<WmsInnerTransferSlip> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        WmsInnerTransferSlip  wmsInnerTransferSlip = wmsInnerTransferSlipService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(wmsInnerTransferSlip,StringUtils.isEmpty(wmsInnerTransferSlip)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WmsInnerTransferSlipDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInnerTransferSlip searchWmsInnerTransferSlip) {
        Page<Object> page = PageHelper.startPage(searchWmsInnerTransferSlip.getStartPage(),searchWmsInnerTransferSlip.getPageSize());
        List<WmsInnerTransferSlipDto> list = wmsInnerTransferSlipService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInnerTransferSlip));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<WmsInnerHtTransferSlip>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInnerTransferSlip searchWmsInnerTransferSlip) {
        Page<Object> page = PageHelper.startPage(searchWmsInnerTransferSlip.getStartPage(),searchWmsInnerTransferSlip.getPageSize());
        List<WmsInnerHtTransferSlip> list = wmsInnerHtTransferSlipService.findHtList(ControllerUtil.dynamicConditionByEntity(searchWmsInnerTransferSlip));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchWmsInnerTransferSlip searchWmsInnerTransferSlip){
    List<WmsInnerTransferSlipDto> list = wmsInnerTransferSlipService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInnerTransferSlip));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "WmsInnerTransferSlip信息", WmsInnerTransferSlipDto.class, "WmsInnerTransferSlip.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
