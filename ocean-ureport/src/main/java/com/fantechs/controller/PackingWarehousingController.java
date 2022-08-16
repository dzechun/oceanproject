package com.fantechs.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.entity.BarcodeTraceModel;
import com.fantechs.entity.PackingWarehousingModel;
import com.fantechs.entity.search.SearchPackingWarehousingModel;
import com.fantechs.service.PackingWarehousingService;
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
 * @Date 2021/10/27
 */
@RestController
@Api(tags = "装箱入库报表")
@RequestMapping("/PackingWarehousing")
@Validated
public class PackingWarehousingController {
    @Resource
    private PackingWarehousingService packingWarehousingService;

    @PostMapping("/findList")
    @ApiModelProperty("条码追踪")
    public ResponseEntity<List<PackingWarehousingModel>> findList(@RequestBody(required = false) SearchPackingWarehousingModel searchPackingWarehousingModel){
        Page<Object> page = PageHelper.startPage(searchPackingWarehousingModel.getStartPage(), searchPackingWarehousingModel.getPageSize());
        return ControllerUtil.returnDataSuccess(packingWarehousingService.findList(ControllerUtil.dynamicConditionByEntity(searchPackingWarehousingModel)),(int)page.getTotal());
    }

    @PostMapping("/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void export(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchPackingWarehousingModel searchPackingWarehousingModel){
        List<PackingWarehousingModel> list = packingWarehousingService.findList(ControllerUtil.dynamicConditionByEntity(searchPackingWarehousingModel));
        try {
            // 导出操作
            EasyPoiUtils.exportExcel(list, "装箱入库报表", "装箱入库报表", PackingWarehousingModel.class, "装箱入库报表.xls", response);
        } catch (Exception e) {
            throw new BizErrorException(e);
        }
    }
}
