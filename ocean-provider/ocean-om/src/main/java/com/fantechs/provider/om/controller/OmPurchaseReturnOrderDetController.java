package com.fantechs.provider.om.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.om.OmPurchaseReturnOrderDetDto;
import com.fantechs.common.base.general.entity.om.OmHtPurchaseReturnOrderDet;
import com.fantechs.common.base.general.entity.om.OmPurchaseReturnOrderDet;
import com.fantechs.common.base.general.entity.om.search.SearchOmPurchaseReturnOrderDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.om.service.OmPurchaseReturnOrderDetService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/12/20.
 */
@RestController
@Api(tags = "采退订单明细")
@RequestMapping("/omPurchaseReturnOrderDet")
@Validated
@Slf4j
public class OmPurchaseReturnOrderDetController {

    @Resource
    private OmPurchaseReturnOrderDetService omPurchaseReturnOrderDetService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated OmPurchaseReturnOrderDet omPurchaseReturnOrderDet) {
        return ControllerUtil.returnCRUD(omPurchaseReturnOrderDetService.save(omPurchaseReturnOrderDet));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(omPurchaseReturnOrderDetService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=OmPurchaseReturnOrderDet.update.class) OmPurchaseReturnOrderDet omPurchaseReturnOrderDet) {
        return ControllerUtil.returnCRUD(omPurchaseReturnOrderDetService.update(omPurchaseReturnOrderDet));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<OmPurchaseReturnOrderDet> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        OmPurchaseReturnOrderDet  omPurchaseReturnOrderDet = omPurchaseReturnOrderDetService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(omPurchaseReturnOrderDet,StringUtils.isEmpty(omPurchaseReturnOrderDet)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<OmPurchaseReturnOrderDetDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchOmPurchaseReturnOrderDet searchOmPurchaseReturnOrderDet) {
        Page<Object> page = PageHelper.startPage(searchOmPurchaseReturnOrderDet.getStartPage(),searchOmPurchaseReturnOrderDet.getPageSize());
        List<OmPurchaseReturnOrderDetDto> list = omPurchaseReturnOrderDetService.findList(ControllerUtil.dynamicConditionByEntity(searchOmPurchaseReturnOrderDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<OmPurchaseReturnOrderDetDto>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchOmPurchaseReturnOrderDet searchOmPurchaseReturnOrderDet) {
        List<OmPurchaseReturnOrderDetDto> list = omPurchaseReturnOrderDetService.findList(ControllerUtil.dynamicConditionByEntity(searchOmPurchaseReturnOrderDet));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<OmHtPurchaseReturnOrderDet>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchOmPurchaseReturnOrderDet searchOmPurchaseReturnOrderDet) {
        Page<Object> page = PageHelper.startPage(searchOmPurchaseReturnOrderDet.getStartPage(),searchOmPurchaseReturnOrderDet.getPageSize());
        List<OmHtPurchaseReturnOrderDet> list = omPurchaseReturnOrderDetService.findHtList(ControllerUtil.dynamicConditionByEntity(searchOmPurchaseReturnOrderDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchOmPurchaseReturnOrderDet searchOmPurchaseReturnOrderDet){
    List<OmPurchaseReturnOrderDetDto> list = omPurchaseReturnOrderDetService.findList(ControllerUtil.dynamicConditionByEntity(searchOmPurchaseReturnOrderDet));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "采退订单明细", OmPurchaseReturnOrderDetDto.class, "采退订单明细.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
