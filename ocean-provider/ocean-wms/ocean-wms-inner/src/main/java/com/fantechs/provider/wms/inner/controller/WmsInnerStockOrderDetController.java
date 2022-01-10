package com.fantechs.provider.wms.inner.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerStockOrderDetBarcodeDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerStockOrderDetDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerStockOrderDet;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerStockOrderDetBarcode;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerStockOrderDet;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerStockOrderDetBarcode;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.inner.service.WmsInnerStockOrderDetBarcodeService;
import com.fantechs.provider.wms.inner.service.WmsInnerStockOrderDetService;
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
 * Created by leifengzhi on 2021/12/28.
 */
@RestController
@Api(tags = "盘点单明细控制器")
@RequestMapping("/wmsInnerStockOrderDet")
@Validated
public class WmsInnerStockOrderDetController {

    @Resource
    private WmsInnerStockOrderDetService wmsInnerStockOrderDetService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WmsInnerStockOrderDet wmsInnerStockOrderDet) {
        return ControllerUtil.returnCRUD(wmsInnerStockOrderDetService.save(wmsInnerStockOrderDet));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(wmsInnerStockOrderDetService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=WmsInnerStockOrderDet.update.class) WmsInnerStockOrderDet wmsInnerStockOrderDet) {
        return ControllerUtil.returnCRUD(wmsInnerStockOrderDetService.update(wmsInnerStockOrderDet));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<WmsInnerStockOrderDet> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        WmsInnerStockOrderDet  wmsInnerStockOrderDet = wmsInnerStockOrderDetService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(wmsInnerStockOrderDet,StringUtils.isEmpty(wmsInnerStockOrderDet)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WmsInnerStockOrderDetDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInnerStockOrderDet searchWmsInnerStockOrderDet) {
        Page<Object> page = PageHelper.startPage(searchWmsInnerStockOrderDet.getStartPage(),searchWmsInnerStockOrderDet.getPageSize());
        List<WmsInnerStockOrderDetDto> list = wmsInnerStockOrderDetService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInnerStockOrderDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<WmsInnerStockOrderDetDto>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchWmsInnerStockOrderDet searchWmsInnerStockOrderDet) {
        List<WmsInnerStockOrderDetDto> list = wmsInnerStockOrderDetService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInnerStockOrderDet));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

}
