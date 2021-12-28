package com.fantechs.provider.wms.out.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.out.WmsOutPlanStockListOrderDetDto;
import com.fantechs.common.base.general.entity.wms.out.WmsOutPlanStockListOrderDet;
import com.fantechs.common.base.general.entity.wms.out.search.SearchWmsOutPlanStockListOrderDet;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.out.service.WmsOutPlanStockListOrderDetService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/12/22.
 */
@RestController
@Api(tags = "备料计划明细控制器")
@RequestMapping("/wmsOutPlanStockListOrderDet")
@Validated
public class WmsOutPlanStockListOrderDetController {

    @Resource
    private WmsOutPlanStockListOrderDetService wmsOutPlanStockListOrderDetService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WmsOutPlanStockListOrderDet wmsOutPlanStockListOrderDet) {
        return ControllerUtil.returnCRUD(wmsOutPlanStockListOrderDetService.save(wmsOutPlanStockListOrderDet));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(wmsOutPlanStockListOrderDetService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=WmsOutPlanStockListOrderDet.update.class) WmsOutPlanStockListOrderDet wmsOutPlanStockListOrderDet) {
        return ControllerUtil.returnCRUD(wmsOutPlanStockListOrderDetService.update(wmsOutPlanStockListOrderDet));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<WmsOutPlanStockListOrderDet> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        WmsOutPlanStockListOrderDet  wmsOutPlanStockListOrderDet = wmsOutPlanStockListOrderDetService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(wmsOutPlanStockListOrderDet,StringUtils.isEmpty(wmsOutPlanStockListOrderDet)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WmsOutPlanStockListOrderDetDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWmsOutPlanStockListOrderDet searchWmsOutPlanStockListOrderDet) {
        Page<Object> page = PageHelper.startPage(searchWmsOutPlanStockListOrderDet.getStartPage(),searchWmsOutPlanStockListOrderDet.getPageSize());
        List<WmsOutPlanStockListOrderDetDto> list = wmsOutPlanStockListOrderDetService.findList(searchWmsOutPlanStockListOrderDet);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<WmsOutPlanStockListOrderDetDto>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchWmsOutPlanStockListOrderDet searchWmsOutPlanStockListOrderDet) {
        List<WmsOutPlanStockListOrderDetDto> list = wmsOutPlanStockListOrderDetService.findList(searchWmsOutPlanStockListOrderDet);
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchWmsOutPlanStockListOrderDet searchWmsOutPlanStockListOrderDet){
    List<WmsOutPlanStockListOrderDetDto> list = wmsOutPlanStockListOrderDetService.findList(searchWmsOutPlanStockListOrderDet);
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "WmsOutPlanStockListOrderDet信息", WmsOutPlanStockListOrderDetDto.class, "WmsOutPlanStockListOrderDet.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

}
