package com.fantechs.provider.srm.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.srm.SrmPlanDeliveryOrderDetDto;
import com.fantechs.common.base.general.dto.srm.SrmPlanDeliveryOrderDto;
import com.fantechs.common.base.general.entity.srm.SrmPlanDeliveryOrderDet;
import com.fantechs.common.base.general.entity.srm.search.SearchSrmPlanDeliveryOrder;
import com.fantechs.common.base.general.entity.srm.search.SearchSrmPlanDeliveryOrderDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.srm.service.SrmPlanDeliveryOrderDetService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/11/29.
 */
@RestController
@Api(tags = "送货计划明细控制器")
@RequestMapping("/srmPlanDeliveryOrderDet")
@Validated
public class SrmPlanDeliveryOrderDetController {

    @Resource
    private SrmPlanDeliveryOrderDetService srmPlanDeliveryOrderDetService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated SrmPlanDeliveryOrderDet srmPlanDeliveryOrderDet) {
        return ControllerUtil.returnCRUD(srmPlanDeliveryOrderDetService.save(srmPlanDeliveryOrderDet));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(srmPlanDeliveryOrderDetService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=SrmPlanDeliveryOrderDet.update.class) SrmPlanDeliveryOrderDet srmPlanDeliveryOrderDet) {
        return ControllerUtil.returnCRUD(srmPlanDeliveryOrderDetService.update(srmPlanDeliveryOrderDet));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SrmPlanDeliveryOrderDet> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        SrmPlanDeliveryOrderDet  srmPlanDeliveryOrderDet = srmPlanDeliveryOrderDetService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(srmPlanDeliveryOrderDet,StringUtils.isEmpty(srmPlanDeliveryOrderDet)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SrmPlanDeliveryOrderDetDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSrmPlanDeliveryOrderDet searchSrmPlanDeliveryOrderDet) {
        Page<Object> page = PageHelper.startPage(searchSrmPlanDeliveryOrderDet.getStartPage(),searchSrmPlanDeliveryOrderDet.getPageSize());
        List<SrmPlanDeliveryOrderDetDto> list = srmPlanDeliveryOrderDetService.findList(ControllerUtil.dynamicConditionByEntity(searchSrmPlanDeliveryOrderDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("生成ASN单")
    @PostMapping("/asn")
    public ResponseEntity asn(@ApiParam(value = "数据集合")@RequestBody List<SrmPlanDeliveryOrderDetDto> list) {
        return ControllerUtil.returnCRUD(srmPlanDeliveryOrderDetService.asn(list));
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<SrmPlanDeliveryOrderDetDto>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchSrmPlanDeliveryOrderDet searchSrmPlanDeliveryOrderDet) {
        List<SrmPlanDeliveryOrderDetDto> list = srmPlanDeliveryOrderDetService.findList(ControllerUtil.dynamicConditionByEntity(searchSrmPlanDeliveryOrderDet));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchSrmPlanDeliveryOrderDet searchSrmPlanDeliveryOrderDet){

        List<SrmPlanDeliveryOrderDetDto> list = srmPlanDeliveryOrderDetService.findList(ControllerUtil.dynamicConditionByEntity(searchSrmPlanDeliveryOrderDet));
        try {
            // 导出操作
            EasyPoiUtils.exportExcel(list, "导出信息", "送货计划信息", SrmPlanDeliveryOrderDetDto.class, "送货计划信息.xls", response);
        } catch (Exception e) {
            throw new BizErrorException(e);
        }
    }

}
