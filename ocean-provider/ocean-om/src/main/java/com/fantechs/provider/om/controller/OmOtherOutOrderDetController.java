package com.fantechs.provider.om.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.om.OmOtherOutOrderDetDto;
import com.fantechs.common.base.general.entity.om.OmOtherOutOrderDet;
import com.fantechs.common.base.general.entity.om.search.SearchOmOtherOutOrderDet;
import com.fantechs.provider.om.service.OmOtherOutOrderDetService;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
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
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * Created by Mr.Lei on 2021/06/23.
 */
@RestController
@Api(tags = "其他出库订单明细")
@RequestMapping("/omOtherOutOrderDet")
@Validated
public class OmOtherOutOrderDetController {

    @Resource
    private OmOtherOutOrderDetService omOtherOutOrderDetService;

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<OmOtherOutOrderDet> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        OmOtherOutOrderDet  omOtherOutOrderDet = omOtherOutOrderDetService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(omOtherOutOrderDet,StringUtils.isEmpty(omOtherOutOrderDet)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<OmOtherOutOrderDetDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchOmOtherOutOrderDet searchOmOtherOutOrderDet) {
        Page<Object> page = PageHelper.startPage(searchOmOtherOutOrderDet.getStartPage(),searchOmOtherOutOrderDet.getPageSize());
        List<OmOtherOutOrderDetDto> list = omOtherOutOrderDetService.findList(ControllerUtil.dynamicConditionByEntity(searchOmOtherOutOrderDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

//    @ApiOperation("历史列表")
//    @PostMapping("/findHtList")
//    public ResponseEntity<List<OmOtherOutOrderDet>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchOmOtherOutOrderDet searchOmOtherOutOrderDet) {
//        Page<Object> page = PageHelper.startPage(searchOmOtherOutOrderDet.getStartPage(),searchOmOtherOutOrderDet.getPageSize());
//        List<OmOtherOutOrderDet> list = omOtherOutOrderDetService.findHtList(ControllerUtil.dynamicConditionByEntity(searchOmOtherOutOrderDet));
//        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
//    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchOmOtherOutOrderDet searchOmOtherOutOrderDet){
    List<OmOtherOutOrderDetDto> list = omOtherOutOrderDetService.findList(ControllerUtil.dynamicConditionByEntity(searchOmOtherOutOrderDet));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "OmOtherOutOrderDet信息", OmOtherOutOrderDetDto.class, "OmOtherOutOrderDet.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

    @ApiOperation(value = "更新其他出库订单下推数量",notes = "更新其他出库订单下推数量")
    @PostMapping("/updatePutDownQty")
    public ResponseEntity updatePutDownQty(@ApiParam(value = "必传明细ID",required = true)@RequestParam Long detId, @ApiParam(value = "必传上架数量",required = true)@RequestParam BigDecimal putawayQty) {
        return ControllerUtil.returnCRUD(omOtherOutOrderDetService.updatePutDownQty(detId,putawayQty));
    }
}
