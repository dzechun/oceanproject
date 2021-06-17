package com.fantechs.provider.om.controller;

import com.fantechs.common.base.general.entity.om.OmPurchaseOrderDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.om.service.OmPurchaseOrderDetService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 *
 * Created by leifengzhi on 2021/06/17.
 */
@RestController
@Api(tags = "omPurchaseOrderDet控制器")
@RequestMapping("/omPurchaseOrderDet")
@Validated
public class OmPurchaseOrderDetController {

    @Resource
    private OmPurchaseOrderDetService omPurchaseOrderDetService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated OmPurchaseOrderDet omPurchaseOrderDet) {
        return ControllerUtil.returnCRUD(omPurchaseOrderDetService.save(omPurchaseOrderDet));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(omPurchaseOrderDetService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=OmPurchaseOrderDet.update.class) OmPurchaseOrderDet omPurchaseOrderDet) {
        return ControllerUtil.returnCRUD(omPurchaseOrderDetService.update(omPurchaseOrderDet));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<OmPurchaseOrderDet> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        OmPurchaseOrderDet  omPurchaseOrderDet = omPurchaseOrderDetService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(omPurchaseOrderDet,StringUtils.isEmpty(omPurchaseOrderDet)?0:1);
    }

/*    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<OmPurchaseOrderDetDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchOmPurchaseOrderDet searchOmPurchaseOrderDet) {
        Page<Object> page = PageHelper.startPage(searchOmPurchaseOrderDet.getStartPage(),searchOmPurchaseOrderDet.getPageSize());
        List<OmPurchaseOrderDetDto> list = omPurchaseOrderDetService.findList(ControllerUtil.dynamicConditionByEntity(searchOmPurchaseOrderDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }*/

/*    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<OmPurchaseOrderDet>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchOmPurchaseOrderDet searchOmPurchaseOrderDet) {
        Page<Object> page = PageHelper.startPage(searchOmPurchaseOrderDet.getStartPage(),searchOmPurchaseOrderDet.getPageSize());
        List<OmPurchaseOrderDet> list = omPurchaseOrderDetService.findHtList(ControllerUtil.dynamicConditionByEntity(searchOmPurchaseOrderDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }*/

/*    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchOmPurchaseOrderDet searchOmPurchaseOrderDet){
    List<OmPurchaseOrderDetDto> list = omPurchaseOrderDetService.findList(ControllerUtil.dynamicConditionByEntity(searchOmPurchaseOrderDet));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "OmPurchaseOrderDet信息", OmPurchaseOrderDetDto.class, "OmPurchaseOrderDet.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }*/
}
