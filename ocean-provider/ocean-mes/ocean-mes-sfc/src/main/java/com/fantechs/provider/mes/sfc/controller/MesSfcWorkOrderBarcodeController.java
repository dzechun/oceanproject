package com.fantechs.provider.mes.sfc.controller;

import com.fantechs.common.base.general.dto.mes.sfc.LabelRuteDto;
import com.fantechs.common.base.general.dto.mes.sfc.MesSfcWorkOrderBarcodeDto;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcWorkOrderBarcode;
import com.fantechs.common.base.general.entity.mes.sfc.SearchMesSfcWorkOrderBarcode;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.mes.sfc.service.MesSfcWorkOrderBarcodeService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 *  条码打印接口
 * Created by Mr.lei on 2021/04/07.
 */
@RestController
@Api(tags = "条码打印")
@RequestMapping("/mesSfcWorkOrderBarcode")
@Validated
public class MesSfcWorkOrderBarcodeController {

    @Resource
    private MesSfcWorkOrderBarcodeService mesSfcWorkOrderBarcodeService;

    @ApiOperation("生成条码")
    @PostMapping("/add")
    public ResponseEntity<List<MesSfcWorkOrderBarcode>> add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated MesSfcWorkOrderBarcode mesSfcWorkOrderBarcode) {
        List<MesSfcWorkOrderBarcode> mesSfcWorkOrderBarcodes = mesSfcWorkOrderBarcodeService.add(mesSfcWorkOrderBarcode);
        return ControllerUtil.returnDataSuccess(mesSfcWorkOrderBarcodes,StringUtils.isEmpty(mesSfcWorkOrderBarcodes)?0:1);
    }

    @ApiOperation("补打列表")
    @PostMapping("/findList")
    public ResponseEntity<List<MesSfcWorkOrderBarcodeDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchMesSfcWorkOrderBarcode searchMesSfcWorkOrderBarcode) {
        Page<Object> page = PageHelper.startPage(searchMesSfcWorkOrderBarcode.getStartPage(),searchMesSfcWorkOrderBarcode.getPageSize());
        List<MesSfcWorkOrderBarcodeDto> list = mesSfcWorkOrderBarcodeService.findList(searchMesSfcWorkOrderBarcode);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("打印/补打")
    @PostMapping("/print")
    public ResponseEntity print(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids,
                                @ApiParam(value = "打印类型（1，打印，2，补打）",required = true)@RequestParam Byte printType,@RequestParam String printName,
                                @RequestParam String userCode,@RequestParam String password){
        return ControllerUtil.returnCRUD(mesSfcWorkOrderBarcodeService.print(ids,printType,printName,userCode,password));
    }

    @ApiOperation("规则解析及标签模版")
    @PostMapping("/findRule")
    public ResponseEntity<LabelRuteDto> findRule(@ApiParam(value = "Id",required = true)@RequestParam Long Id, @ApiParam("条码类别（1.工序流转卡、2.工单条码、3.客户条码、4-销售订单条码）")@RequestParam Byte barcodeType){
        LabelRuteDto labelRuteDto = mesSfcWorkOrderBarcodeService.findLabelRute(Id,barcodeType);
        return ControllerUtil.returnDataSuccess(labelRuteDto,StringUtils.isEmpty(labelRuteDto)?0:1);
    }

    @ApiOperation("其他打印接口")
    @PostMapping("/restPrint")
    public ResponseEntity restPrint(@ApiParam(value = "Id",required = true)@RequestParam Long Id,@ApiParam("条码类别（1.工序流转卡、2.工单条码、3.客户条码、4-销售订单条码）")@RequestParam Byte barcodeType){
        return ControllerUtil.returnSuccess();
    }

    @ApiOperation("标签版本校验下载")
    @GetMapping("/checkoutLabel")
    public ResponseEntity checkoutLabel(@ApiParam(value = "模版名称",required = true)@RequestParam String labelName, HttpServletRequest request, HttpServletResponse response){
        mesSfcWorkOrderBarcodeService.checkOutLabel(labelName,request,response);
        return ControllerUtil.returnSuccess();
    }

    @ApiOperation("条码查询")
    @PostMapping("/findBarcode")
    public ResponseEntity<MesSfcWorkOrderBarcode> findBarcode(@RequestParam String barcode){
        MesSfcWorkOrderBarcode mesSfcWorkOrderBarcode = mesSfcWorkOrderBarcodeService.findBarcode(barcode);
        return ControllerUtil.returnDataSuccess(mesSfcWorkOrderBarcode,StringUtils.isEmpty(mesSfcWorkOrderBarcode)?0:1);
    }

    @ApiOperation("万宝-销售订单明细生成客户条码")
    @PostMapping("/wanbaoAddCustomerBarcode")
    public ResponseEntity<List<String>> wanbaoAddCustomerBarcode(@ApiParam(value = "销售订单明细ID",required = true) @RequestParam Long salesOrderDetId,
                                                                 @ApiParam(value = "固定值",required = true) @RequestParam String fixedValue,
                                                                 @ApiParam(value = "初始值",required = true) @RequestParam String initialValue,
                                                                 @ApiParam(value = "最终值",required = true) @RequestParam String finalValue){
        List<String> list = mesSfcWorkOrderBarcodeService.wanbaoAddCustomerBarcode(salesOrderDetId, fixedValue, initialValue, finalValue);
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @ApiOperation("万宝-销售订单明细删除客户条码")
    @PostMapping("/wanbaoDeleteCustomerBarcode")
    public ResponseEntity<List<String>> wanbaoDeleteCustomerBarcode(@ApiParam(value = "销售订单明细ID",required = true) @RequestParam Long salesOrderDetId,
                                                                 @ApiParam(value = "固定值",required = true) @RequestParam String fixedValue){
        return ControllerUtil.returnCRUD(mesSfcWorkOrderBarcodeService.wanbaoDeleteCustomerBarcode(salesOrderDetId, fixedValue));
    }

    @ApiOperation("万宝-按销售订单明细查询客户条码")
    @PostMapping("/wanbaoFindCustomerBarcode")
    public ResponseEntity<List<MesSfcWorkOrderBarcode>> wanbaoFindCustomerBarcode(@ApiParam(value = "销售订单明细ID",required = true) @RequestParam Long salesOrderDetId){
        List<MesSfcWorkOrderBarcode> barcodes = mesSfcWorkOrderBarcodeService.wanbaoFindCustomerBarcode(salesOrderDetId);
        return ControllerUtil.returnDataSuccess(barcodes, barcodes.size());
    }

}
