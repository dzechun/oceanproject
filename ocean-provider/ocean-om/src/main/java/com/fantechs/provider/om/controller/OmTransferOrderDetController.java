package com.fantechs.provider.om.controller;

import com.fantechs.common.base.general.dto.om.OmTransferOrderDetDto;
import com.fantechs.common.base.general.entity.om.OmTransferOrderDet;
import com.fantechs.common.base.general.entity.om.search.SearchOmTransferOrderDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.om.service.OmTransferOrderDetService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by Mr.Lei on 2021/06/15.
 */
@RestController
@Api(tags = "调拨订单明细")
@RequestMapping("/omTransferOrderDet")
@Validated
public class OmTransferOrderDetController {

    @Resource
    private OmTransferOrderDetService omTransferOrderDetService;

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<OmTransferOrderDet> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        OmTransferOrderDet  omTransferOrderDet = omTransferOrderDetService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(omTransferOrderDet,StringUtils.isEmpty(omTransferOrderDet)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<OmTransferOrderDetDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchOmTransferOrderDet searchOmTransferOrderDet) {
        Page<Object> page = PageHelper.startPage(searchOmTransferOrderDet.getStartPage(),searchOmTransferOrderDet.getPageSize());
        List<OmTransferOrderDetDto> list = omTransferOrderDetService.findList(ControllerUtil.dynamicConditionByEntity(searchOmTransferOrderDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value= OmTransferOrderDet.update.class) OmTransferOrderDet omTransferOrderDet) {
        return ControllerUtil.returnCRUD(omTransferOrderDetService.update(omTransferOrderDet));
    }
}
