package com.fantechs.provider.srm.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.srm.SrmInAsnOrderDto;
import com.fantechs.common.base.general.entity.srm.SrmInAsnOrder;
import com.fantechs.common.base.general.entity.srm.search.SearchSrmInAsnOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.srm.service.SrmInAsnOrderService;
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
 * Created by leifengzhi on 2021/11/25.
 */
@RestController
@Api(tags = "wmsInAsnOrder控制器")
@RequestMapping("/wmsInAsnOrder")
@Validated
@Slf4j
public class SrmInAsnOrderController {

    @Resource
    private SrmInAsnOrderService srmInAsnOrderService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated SrmInAsnOrder wmsInAsnOrder) {
        return ControllerUtil.returnCRUD(srmInAsnOrderService.save(wmsInAsnOrder));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(srmInAsnOrderService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=SrmInAsnOrder.update.class) SrmInAsnOrder wmsInAsnOrder) {
        return ControllerUtil.returnCRUD(srmInAsnOrderService.update(wmsInAsnOrder));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SrmInAsnOrder> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        SrmInAsnOrder  wmsInAsnOrder = srmInAsnOrderService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(wmsInAsnOrder,StringUtils.isEmpty(wmsInAsnOrder)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SrmInAsnOrderDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSrmInAsnOrder searchSrmInAsnOrder) {
        Page<Object> page = PageHelper.startPage(searchSrmInAsnOrder.getStartPage(),searchSrmInAsnOrder.getPageSize());
        List<SrmInAsnOrderDto> list = srmInAsnOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchSrmInAsnOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<SrmInAsnOrderDto>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchSrmInAsnOrder searchSrmInAsnOrder) {
        List<SrmInAsnOrderDto> list = srmInAsnOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchSrmInAsnOrder));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

/*    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<SrmInAsnOrder>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchSrmInAsnOrder searchSrmInAsnOrder) {
        Page<Object> page = PageHelper.startPage(searchSrmInAsnOrder.getStartPage(),searchSrmInAsnOrder.getPageSize());
        List<SrmInAsnOrder> list = srmInAsnOrderService.findHtList(ControllerUtil.dynamicConditionByEntity(searchSrmInAsnOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }*/

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchSrmInAsnOrder searchSrmInAsnOrder){
    List<SrmInAsnOrderDto> list = srmInAsnOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchSrmInAsnOrder));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "SrmInAsnOrder信息", SrmInAsnOrderDto.class, "SrmInAsnOrder.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }


}
