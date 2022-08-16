package com.fantechs.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.entity.PackingOutboundModel;
import com.fantechs.entity.search.SearchPackingOutbound;
import com.fantechs.service.PackingOutboundService;
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
@Api(tags = "领料报表")
@RequestMapping("/PackingOutbound")
@Validated
public class PackingOutboundController {
    @Resource
    private PackingOutboundService packingOutboundService;

    @PostMapping("/findList")
    @ApiModelProperty("条码追踪")
    public ResponseEntity<List<PackingOutboundModel>> findList(@RequestBody(required = false) SearchPackingOutbound searchPackingOutbound){
        Page<Object> page = PageHelper.startPage(searchPackingOutbound.getStartPage(), searchPackingOutbound.getPageSize());
        return ControllerUtil.returnDataSuccess(packingOutboundService.findList(ControllerUtil.dynamicConditionByEntity(searchPackingOutbound)),(int)page.getTotal());
    }

    @PostMapping("/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void export(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchPackingOutbound searchPackingOutbound){
        List<PackingOutboundModel> list = packingOutboundService.findList(ControllerUtil.dynamicConditionByEntity(searchPackingOutbound));
        try {
            // 导出操作
            EasyPoiUtils.exportExcel(list, "领料出库报表", "领料出库报表", PackingOutboundModel.class, "领料出库报表.xls", response);
        } catch (Exception e) {
            throw new BizErrorException(e);
        }
    }
}
