package com.fantechs.provider.om.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.om.OmHtSalesReturnOrderDetDto;
import com.fantechs.common.base.general.dto.om.OmSalesReturnOrderDetDto;
import com.fantechs.common.base.general.entity.om.OmHtSalesReturnOrderDet;
import com.fantechs.common.base.general.entity.om.OmSalesReturnOrderDet;
import com.fantechs.common.base.general.entity.om.search.SearchOmSalesReturnOrderDet;
import com.fantechs.provider.om.service.OmSalesReturnOrderDetService;
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
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/06/21.
 */
@RestController
@Api(tags = "销退订单明细")
@RequestMapping("/omSalesReturnOrderDet")
@Validated
public class OmSalesReturnOrderDetController {

    @Resource
    private OmSalesReturnOrderDetService omSalesReturnOrderDetService;

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<OmSalesReturnOrderDet> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        OmSalesReturnOrderDet  omSalesReturnOrderDet = omSalesReturnOrderDetService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(omSalesReturnOrderDet,StringUtils.isEmpty(omSalesReturnOrderDet)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<OmSalesReturnOrderDetDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchOmSalesReturnOrderDet searchOmSalesReturnOrderDet) {
        Page<Object> page = PageHelper.startPage(searchOmSalesReturnOrderDet.getStartPage(),searchOmSalesReturnOrderDet.getPageSize());
        List<OmSalesReturnOrderDetDto> list = omSalesReturnOrderDetService.findList(ControllerUtil.dynamicConditionByEntity(searchOmSalesReturnOrderDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<OmHtSalesReturnOrderDetDto>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchOmSalesReturnOrderDet searchOmSalesReturnOrderDet) {
        Page<Object> page = PageHelper.startPage(searchOmSalesReturnOrderDet.getStartPage(),searchOmSalesReturnOrderDet.getPageSize());
        List<OmHtSalesReturnOrderDetDto> list = omSalesReturnOrderDetService.findHtList(ControllerUtil.dynamicConditionByEntity(searchOmSalesReturnOrderDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }
}
