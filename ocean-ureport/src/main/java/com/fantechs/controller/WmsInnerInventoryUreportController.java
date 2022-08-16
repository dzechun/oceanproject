package com.fantechs.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.dto.PrintModelDto;
import com.fantechs.entity.WmsInnerInventoryModel;
import com.fantechs.entity.search.SearchWmsInnerInventory;
import com.fantechs.service.WmsInnerInventoryUreportService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Author mr.lei
 * @Date 2021/7/29
 */
@RestController
@Api(tags = "库存汇总")
@RequestMapping("/wmsInnerInventoryUreport")
@Validated
public class WmsInnerInventoryUreportController {
    @Resource
    private WmsInnerInventoryUreportService wmsInnerInventoryUreportService;

    @PostMapping("/findList")
    @ApiModelProperty("库存汇总")
    public ResponseEntity<List<WmsInnerInventoryModel>> findList(@RequestBody SearchWmsInnerInventory searchWmsInnerInventory){
        Page<Object> page = PageHelper.startPage(searchWmsInnerInventory.getStartPage(), searchWmsInnerInventory.getPageSize());
        return ControllerUtil.returnDataSuccess(wmsInnerInventoryUreportService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInnerInventory)),(int)page.getTotal());
    }

    @PostMapping("/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void export(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchWmsInnerInventory searchWmsInnerInventory){
        List<WmsInnerInventoryModel> list = wmsInnerInventoryUreportService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInnerInventory));
        try {
            // 导出操作
            EasyPoiUtils.exportExcel(list, "库存汇总", "库存汇总", WmsInnerInventoryModel.class, "库存汇总.xls", response);
        } catch (Exception e) {
            throw new BizErrorException(e);
        }
    }

    @PostMapping("/printMaterialCode")
    @ApiOperation("材料编码打印")
    public ResponseEntity printMaterialCode(@RequestBody List<PrintModelDto> list){
        return ControllerUtil.returnCRUD(wmsInnerInventoryUreportService.PrintMaterialCode(list));
    }
}
