package com.fantechs.provider.om.controller.sales;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.om.sales.OmSalesOrderDetDto;
import com.fantechs.common.base.general.dto.om.sales.SearchOmSalesOrderDet;
import com.fantechs.common.base.general.entity.om.sales.OmSalesOrderDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.om.service.sales.OmSalesOrderDetService;
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
 * Created by leifengzhi on 2021/04/19.
 */
@RestController
@Api(tags = "订单详情管理")
@RequestMapping("/omSalesOrderDet")
@Validated
public class OmSalesOrderDetController {

    @Resource
    private OmSalesOrderDetService omSalesOrderDetService;

//    @ApiOperation(value = "新增",notes = "新增")
//    @PostMapping("/add")
//    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated OmSalesOrderDet omSalesOrderDet) {
//        return ControllerUtil.returnCRUD(omSalesOrderDetService.save(omSalesOrderDet));
//    }
//
//    @ApiOperation("删除")
//    @PostMapping("/delete")
//    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
//        return ControllerUtil.returnCRUD(omSalesOrderDetService.batchDelete(ids));
//    }
//
//    @ApiOperation("修改")
//    @PostMapping("/update")
//    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=OmSalesOrderDet.update.class) OmSalesOrderDet omSalesOrderDet) {
//        return ControllerUtil.returnCRUD(omSalesOrderDetService.update(omSalesOrderDet));
//    }
//
//    @ApiOperation("获取详情")
//    @PostMapping("/detail")
//    public ResponseEntity<OmSalesOrderDet> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
//        OmSalesOrderDet omSalesOrderDet = omSalesOrderDetService.selectByKey(id);
//        return  ControllerUtil.returnDataSuccess(omSalesOrderDet,StringUtils.isEmpty(omSalesOrderDet)?0:1);
//    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<OmSalesOrderDetDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchOmSalesOrderDet searchOmSalesOrderDet) {
        Page<Object> page = PageHelper.startPage(searchOmSalesOrderDet.getStartPage(),searchOmSalesOrderDet.getPageSize());
        List<OmSalesOrderDetDto> list = omSalesOrderDetService.findList(ControllerUtil.dynamicConditionByEntity(searchOmSalesOrderDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<OmSalesOrderDetDto>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchOmSalesOrderDet searchOmSalesOrderDet) {
        Page<Object> page = PageHelper.startPage(searchOmSalesOrderDet.getStartPage(),searchOmSalesOrderDet.getPageSize());
        List<OmSalesOrderDetDto> list = omSalesOrderDetService.findHtList(ControllerUtil.dynamicConditionByEntity(searchOmSalesOrderDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchOmSalesOrderDet searchOmSalesOrderDet){
    List<OmSalesOrderDetDto> list = omSalesOrderDetService.findList(ControllerUtil.dynamicConditionByEntity(searchOmSalesOrderDet));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "OmSalesOrderDet信息", OmSalesOrderDetDto.class, "OmSalesOrderDet.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
