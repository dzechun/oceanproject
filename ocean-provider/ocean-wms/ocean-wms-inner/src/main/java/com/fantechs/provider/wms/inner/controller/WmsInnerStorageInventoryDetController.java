package com.fantechs.provider.wms.inner.controller;

import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerStorageInventoryDetDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerStorageInventoryDet;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerStorageInventoryDet;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.inner.service.WmsInnerStorageInventoryDetService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2020/12/04.
 */
@RestController
@Api(tags = "储位库存明细")
@RequestMapping("/wmsInnerStorageInventoryDet")
@Validated
public class WmsInnerStorageInventoryDetController {

    @Autowired
    private WmsInnerStorageInventoryDetService wmsInnerStorageInventoryDetService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    @Transactional
    @LcnTransaction
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WmsInnerStorageInventoryDet smtStorageInventoryDet) {
        return ControllerUtil.returnCRUD(wmsInnerStorageInventoryDetService.save(smtStorageInventoryDet));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(wmsInnerStorageInventoryDetService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    @Transactional
    @LcnTransaction
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value= WmsInnerStorageInventoryDet.update.class) WmsInnerStorageInventoryDet smtStorageInventoryDet) {
        return ControllerUtil.returnCRUD(wmsInnerStorageInventoryDetService.update(smtStorageInventoryDet));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<WmsInnerStorageInventoryDet> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        WmsInnerStorageInventoryDet smtStorageInventoryDet = wmsInnerStorageInventoryDetService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(smtStorageInventoryDet,StringUtils.isEmpty(smtStorageInventoryDet)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WmsInnerStorageInventoryDetDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInnerStorageInventoryDet searchWmsInnerStorageInventoryDet) {
        Page<Object> page = PageHelper.startPage(searchWmsInnerStorageInventoryDet.getStartPage(), searchWmsInnerStorageInventoryDet.getPageSize());
        List<WmsInnerStorageInventoryDetDto> list = wmsInnerStorageInventoryDetService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInnerStorageInventoryDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchWmsInnerStorageInventoryDet searchWmsInnerStorageInventoryDet){
    List<WmsInnerStorageInventoryDetDto> list = wmsInnerStorageInventoryDetService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInnerStorageInventoryDet));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "SmtStorageInventoryDet信息", WmsInnerStorageInventoryDet.class, "SmtStorageInventoryDet.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
