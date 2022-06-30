package com.fantechs.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.dto.SearchWarehousingDto;
import com.fantechs.dto.WarehousingDetailDto;
import com.fantechs.dto.WarehousingListDto;
import com.fantechs.dto.WarehousingSummaryDto;
import com.fantechs.entity.OmInStorage;
import com.fantechs.entity.ProductionInStorage;
import com.fantechs.entity.ProductionInStorageDet;
import com.fantechs.entity.search.SearchOmInStorage;
import com.fantechs.entity.search.SearchProductionInStorage;
import com.fantechs.service.ProductionInStorageService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
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
 * @Date 2021/9/24
 */
@RestController
@Api(tags = "生产入库")
@RequestMapping("/productionInStorage")
@Validated
public class ProductionInStorageController {
    @Resource
    private ProductionInStorageService productionInStorageService;

    @ApiOperation("生产汇总")
    @PostMapping("/findProList")
    public ResponseEntity<List<ProductionInStorage>> findProList(@ApiParam(value = "查询对象")@RequestBody SearchProductionInStorage searchProductionInStorage) {
        Page<Object> page = PageHelper.startPage(searchProductionInStorage.getStartPage(),searchProductionInStorage.getPageSize());
        List<ProductionInStorage> list = productionInStorageService.findProList(ControllerUtil.dynamicConditionByEntity(searchProductionInStorage));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/findProListExport")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void findProListExport(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchProductionInStorage searchProductionInStorage){
        List<ProductionInStorage> list = productionInStorageService.findProList(ControllerUtil.dynamicConditionByEntity(searchProductionInStorage));
        try {
            // 导出操作
            EasyPoiUtils.exportExcel(list, "生产汇总导出信息", "生产汇总信息", ProductionInStorage.class, "生产汇总.xls", response);
        } catch (Exception e) {
            throw new BizErrorException(e);
        }
    }

    @ApiOperation("生产库存明细")
    @PostMapping("/findProDetList")
    public ResponseEntity<List<ProductionInStorageDet>> findProDetList(@ApiParam(value = "查询对象")@RequestBody SearchProductionInStorage searchProductionInStorage) {
        Page<Object> page = PageHelper.startPage(searchProductionInStorage.getStartPage(),searchProductionInStorage.getPageSize());
        List<ProductionInStorageDet> list = productionInStorageService.findProDetList(ControllerUtil.dynamicConditionByEntity(searchProductionInStorage));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }


    @PostMapping(value = "/findProDetListExport")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void findProDetListExport(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchProductionInStorage searchProductionInStorage){
        List<ProductionInStorageDet> list = productionInStorageService.findProDetList(ControllerUtil.dynamicConditionByEntity(searchProductionInStorage));
        try {
            // 导出操作
            EasyPoiUtils.exportExcel(list, "生产库存明细导出信息", "生产库存明细信息", ProductionInStorageDet.class, "生产库存明细.xls", response);
        } catch (Exception e) {
            throw new BizErrorException(e);
        }
    }

    @ApiOperation("生产入库明细一览表")
    @PostMapping("/findWarehousingList")
    public ResponseEntity<List<WarehousingListDto>> findWarehousingDetList(@ApiParam(value = "查询对象")@RequestBody SearchWarehousingDto searchWarehousingDto) {
        Page<Object> page = PageHelper.startPage(searchWarehousingDto.getStartPage(),searchWarehousingDto.getPageSize());
        List<WarehousingListDto> list = productionInStorageService.findWarehousingList(ControllerUtil.dynamicConditionByEntity(searchWarehousingDto));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/findWarehousingListExport")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void findWarehousingListExport(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchWarehousingDto searchWarehousingDto){
        List<WarehousingListDto> list = productionInStorageService.findWarehousingList(ControllerUtil.dynamicConditionByEntity(searchWarehousingDto));
        try {
            // 导出操作
            EasyPoiUtils.exportExcel(list, "生产入库明细一览表导出信息", "生产入库明细一览表", ProductionInStorageDet.class, "生产入库明细一览表.xls", response);
        } catch (Exception e) {
            throw new BizErrorException(e);
        }
    }

    @ApiOperation("生产入库明细表")
    @PostMapping("/findWarehousingDetail")
    public ResponseEntity<List<WarehousingDetailDto>> findWarehousingDetail(@ApiParam(value = "查询对象")@RequestBody SearchWarehousingDto searchWarehousingDto) {
        Page<Object> page = PageHelper.startPage(searchWarehousingDto.getStartPage(),searchWarehousingDto.getPageSize());
        List<WarehousingDetailDto> list = productionInStorageService.findWarehousingDetail(ControllerUtil.dynamicConditionByEntity(searchWarehousingDto));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/findWarehousingDetailExport")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void findWarehousingDetailExport(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchWarehousingDto searchWarehousingDto){
        List<WarehousingDetailDto> list = productionInStorageService.findWarehousingDetail(ControllerUtil.dynamicConditionByEntity(searchWarehousingDto));
        try {
            // 导出操作
            EasyPoiUtils.exportExcel(list, "生产入库汇总表导出信息", "生产入库汇总表", ProductionInStorageDet.class, "生产入库汇总表.xls", response);
        } catch (Exception e) {
            throw new BizErrorException(e);
        }
    }

    @ApiOperation("生产入库汇总表")
    @PostMapping("/findWarehousingSummary")
    public ResponseEntity<List<WarehousingSummaryDto>> findWarehousingSummary(@ApiParam(value = "查询对象")@RequestBody SearchWarehousingDto searchWarehousingDto) {
        Page<Object> page = PageHelper.startPage(searchWarehousingDto.getStartPage(),searchWarehousingDto.getPageSize());
        List<WarehousingSummaryDto> list = productionInStorageService.findWarehousingSummary(ControllerUtil.dynamicConditionByEntity(searchWarehousingDto));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/findWarehousingSummaryExport")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void findWarehousingSummaryExport(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchWarehousingDto searchWarehousingDto){
        List<WarehousingSummaryDto> list = productionInStorageService.findWarehousingSummary(ControllerUtil.dynamicConditionByEntity(searchWarehousingDto));
        try {
            // 导出操作
            EasyPoiUtils.exportExcel(list, "生产入库汇总表导出信息", "生产入库汇总表", ProductionInStorageDet.class, "生产入库汇总表.xls", response);
        } catch (Exception e) {
            throw new BizErrorException(e);
        }
    }

    @ApiOperation("销售库存明细")
    @PostMapping("/findOmList")
    public ResponseEntity<List<OmInStorage>> findOmList(@ApiParam(value = "查询对象")@RequestBody SearchOmInStorage searchOmInStorage) {
        Page<Object> page = PageHelper.startPage(searchOmInStorage.getStartPage(),searchOmInStorage.getPageSize());
        List<OmInStorage> list = productionInStorageService.findOmList(ControllerUtil.dynamicConditionByEntity(searchOmInStorage));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/findOmListExport")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void findOmListExport(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchOmInStorage searchOmInStorage){
        List<OmInStorage> list = productionInStorageService.findOmList(ControllerUtil.dynamicConditionByEntity(searchOmInStorage));
        try {
            // 导出操作
            EasyPoiUtils.exportExcel(list, "销售库存明细导出信息", "销售库存明细信息", OmInStorage.class, "销售库存明细.xls", response);
        } catch (Exception e) {
            throw new BizErrorException(e);
        }
    }
}
