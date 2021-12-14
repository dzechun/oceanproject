package com.fantechs.provider.wms.in.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.in.WmsInReceivingOrderDto;
import com.fantechs.common.base.general.entity.wms.in.WmsInReceivingOrder;
import com.fantechs.common.base.general.entity.wms.in.search.SearchWmsInReceivingOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.in.service.WmsInReceivingOrderService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by mr.lei on 2021/12/13.
 */
@RestController
@Api(tags = "收货作业")
@RequestMapping("/wmsInReceivingOrder")
@Validated
@Slf4j
public class WmsInReceivingOrderController {

    @Resource
    private WmsInReceivingOrderService wmsInReceivingOrderService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WmsInReceivingOrder wmsInReceivingOrder) {
        return ControllerUtil.returnCRUD(wmsInReceivingOrderService.save(wmsInReceivingOrder));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(wmsInReceivingOrderService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=WmsInReceivingOrder.update.class) WmsInReceivingOrder wmsInReceivingOrder) {
        return ControllerUtil.returnCRUD(wmsInReceivingOrderService.update(wmsInReceivingOrder));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<WmsInReceivingOrder> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        WmsInReceivingOrder  wmsInReceivingOrder = wmsInReceivingOrderService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(wmsInReceivingOrder,StringUtils.isEmpty(wmsInReceivingOrder)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WmsInReceivingOrderDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInReceivingOrder searchWmsInReceivingOrder) {
        Page<Object> page = PageHelper.startPage(searchWmsInReceivingOrder.getStartPage(),searchWmsInReceivingOrder.getPageSize());
        List<WmsInReceivingOrderDto> list = wmsInReceivingOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInReceivingOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<WmsInReceivingOrderDto>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchWmsInReceivingOrder searchWmsInReceivingOrder) {
        List<WmsInReceivingOrderDto> list = wmsInReceivingOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInReceivingOrder));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

//    @ApiOperation("历史列表")
//    @PostMapping("/findHtList")
//    public ResponseEntity<List<WmsInReceivingOrder>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInReceivingOrder searchWmsInReceivingOrder) {
//        Page<Object> page = PageHelper.startPage(searchWmsInReceivingOrder.getStartPage(),searchWmsInReceivingOrder.getPageSize());
//        List<WmsInReceivingOrder> list = wmsInReceivingOrderService.findHtList(ControllerUtil.dynamicConditionByEntity(searchWmsInReceivingOrder));
//        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
//    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchWmsInReceivingOrder searchWmsInReceivingOrder){
    List<WmsInReceivingOrderDto> list = wmsInReceivingOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInReceivingOrder));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "WmsInReceivingOrder信息", WmsInReceivingOrderDto.class, "WmsInReceivingOrder.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

    @PostMapping(value = "/import")
    @ApiOperation(value = "从excel导入",notes = "从excel导入")
    public ResponseEntity importExcel(@ApiParam(value ="输入excel文件",required = true) @RequestPart(value="file") MultipartFile file){
        try {
            // 导入操作
            List<WmsInReceivingOrder> baseAddressImports = EasyPoiUtils.importExcel(file, 0, 1, WmsInReceivingOrder.class);
            Map<String, Object> resultMap = wmsInReceivingOrderService.importExcel(baseAddressImports);
            return ControllerUtil.returnDataSuccess("操作结果集",resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.OPT20012002.getCode());
        }
    }
}
