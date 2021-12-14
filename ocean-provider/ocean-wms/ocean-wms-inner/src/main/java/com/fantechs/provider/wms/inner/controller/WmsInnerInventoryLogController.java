package com.fantechs.provider.wms.inner.controller;

import cn.hutool.core.map.MapUtil;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInventoryLogDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventoryLog;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrder;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrderDet;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerInventoryLog;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.inner.service.WmsInnerInventoryLogService;
import com.fantechs.provider.wms.inner.util.InBarcodeUtil;
import com.fantechs.provider.wms.inner.util.InventoryLogUtil;
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
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/07/29.
 */
@RestController
@Api(tags = "库存日志")
@RequestMapping("/wmsInnerInventoryLog")
@Validated
public class WmsInnerInventoryLogController {

    @Resource
    private WmsInnerInventoryLogService wmsInnerInventoryLogService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WmsInnerInventoryLog wmsInnerInventoryLog) {
        return ControllerUtil.returnCRUD(wmsInnerInventoryLogService.save(wmsInnerInventoryLog));
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WmsInnerInventoryLogDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInnerInventoryLog searchWmsInnerInventoryLog) {
        Page<Object> page = PageHelper.startPage(searchWmsInnerInventoryLog.getStartPage(),searchWmsInnerInventoryLog.getPageSize());
        List<WmsInnerInventoryLogDto> list = wmsInnerInventoryLogService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInnerInventoryLog));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchWmsInnerInventoryLog searchWmsInnerInventoryLog){
    List<WmsInnerInventoryLogDto> list = wmsInnerInventoryLogService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInnerInventoryLog));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "WmsInnerInventoryLog信息", WmsInnerInventoryLogDto.class, "WmsInnerInventoryLog.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
