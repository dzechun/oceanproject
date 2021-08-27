package com.fantechs.provider.srm.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.srm.SrmHtPackingOrderDto;
import com.fantechs.common.base.general.dto.srm.SrmPackingOrderDto;
import com.fantechs.common.base.general.entity.srm.SrmPackingOrder;
import com.fantechs.common.base.general.entity.srm.history.SrmHtPackingOrder;
import com.fantechs.common.base.general.entity.srm.search.SearchSrmPackingOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.srm.service.SrmHtPackingOrderService;
import com.fantechs.provider.srm.service.SrmPackingOrderService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/08/27.
 */
@RestController
@Api(tags = "装箱清单基础信息")
@RequestMapping("/srmPackingOrder")
@Validated
public class SrmPackingOrderController {

    @Resource
    private SrmPackingOrderService srmPackingOrderService;
    @Resource
    private SrmHtPackingOrderService srmHtPackingOrderService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated SrmPackingOrder srmPackingOrder) {
        return ControllerUtil.returnCRUD(srmPackingOrderService.save(srmPackingOrder));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(srmPackingOrderService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=SrmPackingOrder.update.class) SrmPackingOrder srmPackingOrder) {
        return ControllerUtil.returnCRUD(srmPackingOrderService.update(srmPackingOrder));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SrmPackingOrder> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        SrmPackingOrder  srmPackingOrder = srmPackingOrderService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(srmPackingOrder,StringUtils.isEmpty(srmPackingOrder)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SrmPackingOrderDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSrmPackingOrder searchSrmPackingOrder) {
        Page<Object> page = PageHelper.startPage(searchSrmPackingOrder.getStartPage(),searchSrmPackingOrder.getPageSize());
        List<SrmPackingOrderDto> list = srmPackingOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchSrmPackingOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<SrmHtPackingOrderDto>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchSrmPackingOrder searchSrmPackingOrder) {
        Page<Object> page = PageHelper.startPage(searchSrmPackingOrder.getStartPage(),searchSrmPackingOrder.getPageSize());
        List<SrmHtPackingOrderDto> list = srmHtPackingOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchSrmPackingOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchSrmPackingOrder searchSrmPackingOrder){
    List<SrmPackingOrderDto> list = srmPackingOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchSrmPackingOrder));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "SrmPackingOrder信息", SrmPackingOrderDto.class, "SrmPackingOrder.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
