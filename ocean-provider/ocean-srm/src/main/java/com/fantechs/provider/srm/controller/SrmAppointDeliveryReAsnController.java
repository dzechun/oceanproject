package com.fantechs.provider.srm.controller;

import com.fantechs.common.base.general.dto.srm.SrmAppointDeliveryReAsnDto;
import com.fantechs.common.base.general.dto.srm.SrmHtAppointDeliveryReAsnDto;
import com.fantechs.common.base.general.entity.srm.SrmAppointDeliveryReAsn;
import com.fantechs.common.base.general.entity.srm.search.SearchSrmAppointDeliveryReAsn;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.srm.service.SrmAppointDeliveryReAsnService;
import com.fantechs.provider.srm.service.SrmHtAppointDeliveryReAsnService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/11/24.
 */
@RestController
@Api(tags = "送货预约子表")
@RequestMapping("/srmAppointDeliveryReAsn")
@Validated
public class SrmAppointDeliveryReAsnController {

    @Resource
    private SrmAppointDeliveryReAsnService srmAppointDeliveryReAsnService;
    @Resource
    private SrmHtAppointDeliveryReAsnService srmHtAppointDeliveryReAsnService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody SrmAppointDeliveryReAsn srmAppointDeliveryReAsn) {
        return ControllerUtil.returnCRUD(srmAppointDeliveryReAsnService.save(srmAppointDeliveryReAsn));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(srmAppointDeliveryReAsnService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=SrmAppointDeliveryReAsn.update.class) SrmAppointDeliveryReAsn srmAppointDeliveryReAsn) {
        return ControllerUtil.returnCRUD(srmAppointDeliveryReAsnService.update(srmAppointDeliveryReAsn));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SrmAppointDeliveryReAsn> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        SrmAppointDeliveryReAsn  srmAppointDeliveryReAsn = srmAppointDeliveryReAsnService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(srmAppointDeliveryReAsn,StringUtils.isEmpty(srmAppointDeliveryReAsn)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SrmAppointDeliveryReAsnDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSrmAppointDeliveryReAsn searchSrmAppointDeliveryReAsn) {
        Page<Object> page = PageHelper.startPage(searchSrmAppointDeliveryReAsn.getStartPage(),searchSrmAppointDeliveryReAsn.getPageSize());
        List<SrmAppointDeliveryReAsnDto> list = srmAppointDeliveryReAsnService.findList(ControllerUtil.dynamicConditionByEntity(searchSrmAppointDeliveryReAsn));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<SrmAppointDeliveryReAsnDto>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchSrmAppointDeliveryReAsn searchSrmAppointDeliveryReAsn) {
        List<SrmAppointDeliveryReAsnDto> list = srmAppointDeliveryReAsnService.findList(ControllerUtil.dynamicConditionByEntity(searchSrmAppointDeliveryReAsn));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<SrmHtAppointDeliveryReAsnDto>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchSrmAppointDeliveryReAsn searchSrmAppointDeliveryReAsn) {
        Page<Object> page = PageHelper.startPage(searchSrmAppointDeliveryReAsn.getStartPage(),searchSrmAppointDeliveryReAsn.getPageSize());
        List<SrmHtAppointDeliveryReAsnDto> list = srmHtAppointDeliveryReAsnService.findList(ControllerUtil.dynamicConditionByEntity(searchSrmAppointDeliveryReAsn));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }
}
