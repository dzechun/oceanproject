package com.fantechs.provider.mes.sfc.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.mes.sfc.DoReworkOrderDto;
import com.fantechs.common.base.general.dto.mes.sfc.GenerateReworkOrderCodeDto;
import com.fantechs.common.base.general.dto.mes.sfc.MesSfcHtReworkOrderDto;
import com.fantechs.common.base.general.dto.mes.sfc.MesSfcReworkOrderDto;
import com.fantechs.common.base.general.dto.mes.sfc.Search.SearchMesSfcBarcodeProcess;
import com.fantechs.common.base.general.dto.mes.sfc.Search.SearchMesSfcReworkOrder;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcReworkOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.mes.sfc.service.MesSfcReworkOrderService;
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
 * Created by leifengzhi on 2021/06/15.
 */
@RestController
@Api(tags = "生产管理-返工单管理控制器")
@RequestMapping("/mesSfcReworkOrder")
@Validated
public class MesSfcReworkOrderController {

    @Resource
    private MesSfcReworkOrderService mesSfcReworkOrderService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated DoReworkOrderDto doReworkOrderDto) throws Exception {
        return ControllerUtil.returnCRUD(mesSfcReworkOrderService.save(doReworkOrderDto));
    }


    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(mesSfcReworkOrderService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=MesSfcReworkOrder.update.class) MesSfcReworkOrder mesSfcReworkOrder) {
        return ControllerUtil.returnCRUD(mesSfcReworkOrderService.update(mesSfcReworkOrder));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<MesSfcReworkOrder> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        MesSfcReworkOrder  mesSfcReworkOrder = mesSfcReworkOrderService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(mesSfcReworkOrder,StringUtils.isEmpty(mesSfcReworkOrder)?0:1);
    }

    @ApiOperation("获取返工单号")
    @PostMapping("/generateReworkOrderCode")
    public ResponseEntity<GenerateReworkOrderCodeDto> generateReworkOrderCode(@ApiParam(value = "查询对象")@RequestBody SearchMesSfcBarcodeProcess searchMesSfcBarcodeProcess){
        GenerateReworkOrderCodeDto reworkOrderCodeDto = mesSfcReworkOrderService.generateReworkOrderCode(searchMesSfcBarcodeProcess);
        return ControllerUtil.returnDataSuccess(reworkOrderCodeDto, 1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<MesSfcReworkOrderDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchMesSfcReworkOrder searchMesSfcReworkOrder) {
        Page<Object> page = PageHelper.startPage(searchMesSfcReworkOrder.getStartPage(),searchMesSfcReworkOrder.getPageSize());
        List<MesSfcReworkOrderDto> list = mesSfcReworkOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchMesSfcReworkOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<MesSfcHtReworkOrderDto>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchMesSfcReworkOrder searchMesSfcReworkOrder) {
        Page<Object> page = PageHelper.startPage(searchMesSfcReworkOrder.getStartPage(),searchMesSfcReworkOrder.getPageSize());
        List<MesSfcHtReworkOrderDto> list = mesSfcReworkOrderService.findHtList(ControllerUtil.dynamicConditionByEntity(searchMesSfcReworkOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchMesSfcReworkOrder searchMesSfcReworkOrder){
    List<MesSfcReworkOrderDto> list = mesSfcReworkOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchMesSfcReworkOrder));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "MesSfcReworkOrder信息", MesSfcReworkOrderDto.class, "MesSfcReworkOrder.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
