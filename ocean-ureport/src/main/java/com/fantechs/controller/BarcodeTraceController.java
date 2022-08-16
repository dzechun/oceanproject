package com.fantechs.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.entity.BarcodeTraceModel;
import com.fantechs.entity.search.SearchBarcodeTrace;
import com.fantechs.service.BarcodeTraceService;
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
@Api(tags = "条码追踪报表")
@RequestMapping("/barcodeTrace")
@Validated
public class BarcodeTraceController {
    @Resource
    private BarcodeTraceService barcodeTraceService;

    @PostMapping("/findList")
    @ApiModelProperty("条码追踪")
    public ResponseEntity<List<BarcodeTraceModel>> findList(@RequestBody SearchBarcodeTrace searchBarcodeTrace){
        Page<Object> page = PageHelper.startPage(searchBarcodeTrace.getStartPage(), searchBarcodeTrace.getPageSize());
        return ControllerUtil.returnDataSuccess(barcodeTraceService.findList(ControllerUtil.dynamicConditionByEntity(searchBarcodeTrace)),(int)page.getTotal());
    }

    @PostMapping("/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void export(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchBarcodeTrace searchBarcodeTrace){
        List<BarcodeTraceModel> list = barcodeTraceService.findList(ControllerUtil.dynamicConditionByEntity(searchBarcodeTrace));
        try {
            // 导出操作
            EasyPoiUtils.exportExcel(list, "条码追踪", "条码追踪", BarcodeTraceModel.class, "条码追踪.xls", response);
        } catch (Exception e) {
            throw new BizErrorException(e);
        }
    }
}
