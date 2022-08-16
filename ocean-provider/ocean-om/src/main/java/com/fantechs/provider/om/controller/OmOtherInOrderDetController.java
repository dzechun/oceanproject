package com.fantechs.provider.om.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.om.OmOtherInOrderDetDto;
import com.fantechs.common.base.general.entity.om.OmOtherInOrderDet;
import com.fantechs.common.base.general.entity.om.search.SearchOmOtherInOrderDet;
import com.fantechs.provider.om.service.OmOtherInOrderDetService;
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
@Api(tags = "其他入库订单明细")
@RequestMapping("/omOtherInOrderDet")
@Validated
public class OmOtherInOrderDetController {

    @Resource
    private OmOtherInOrderDetService omOtherInOrderDetService;

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<OmOtherInOrderDet> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        OmOtherInOrderDet  omOtherInOrderDet = omOtherInOrderDetService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(omOtherInOrderDet,StringUtils.isEmpty(omOtherInOrderDet)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<OmOtherInOrderDetDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchOmOtherInOrderDet searchOmOtherInOrderDet) {
        Page<Object> page = PageHelper.startPage(searchOmOtherInOrderDet.getStartPage(),searchOmOtherInOrderDet.getPageSize());
        List<OmOtherInOrderDetDto> list = omOtherInOrderDetService.findList(ControllerUtil.dynamicConditionByEntity(searchOmOtherInOrderDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<OmOtherInOrderDetDto>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchOmOtherInOrderDet searchOmOtherInOrderDet) {
        Page<Object> page = PageHelper.startPage(searchOmOtherInOrderDet.getStartPage(),searchOmOtherInOrderDet.getPageSize());
        List<OmOtherInOrderDetDto> list = omOtherInOrderDetService.findHtList(ControllerUtil.dynamicConditionByEntity(searchOmOtherInOrderDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }
}
