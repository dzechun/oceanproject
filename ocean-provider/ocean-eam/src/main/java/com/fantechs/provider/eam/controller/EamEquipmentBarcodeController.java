package com.fantechs.provider.eam.controller;

import com.fantechs.common.base.general.entity.eam.EamEquipmentBarcode;
import com.fantechs.common.base.general.entity.eam.search.SearchEamEquipmentBarcode;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.service.EamEquipmentBarcodeService;
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
 * Created by leifengzhi on 2021/08/21.
 */
@RestController
@Api(tags = "设备条码信息")
@RequestMapping("/eamEquipmentBarcode")
@Validated
public class EamEquipmentBarcodeController {

    @Resource
    private EamEquipmentBarcodeService eamEquipmentBarcodeService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated EamEquipmentBarcode eamEquipmentBarcode) {
        return ControllerUtil.returnCRUD(eamEquipmentBarcodeService.save(eamEquipmentBarcode));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(eamEquipmentBarcodeService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=EamEquipmentBarcode.update.class) EamEquipmentBarcode eamEquipmentBarcode) {
        return ControllerUtil.returnCRUD(eamEquipmentBarcodeService.update(eamEquipmentBarcode));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<EamEquipmentBarcode> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        EamEquipmentBarcode  eamEquipmentBarcode = eamEquipmentBarcodeService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(eamEquipmentBarcode,StringUtils.isEmpty(eamEquipmentBarcode)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<EamEquipmentBarcode>> findList(@ApiParam(value = "查询对象")@RequestBody SearchEamEquipmentBarcode searchEamEquipmentBarcode) {
        Page<Object> page = PageHelper.startPage(searchEamEquipmentBarcode.getStartPage(),searchEamEquipmentBarcode.getPageSize());
        List<EamEquipmentBarcode> list = eamEquipmentBarcodeService.findList(ControllerUtil.dynamicConditionByEntity(searchEamEquipmentBarcode));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    /*
     * 2021/08/25
     * huangshuijun
     */
    @ApiOperation("增加设备条码使用次数")
    @PostMapping("/plusCurrentUsageTime")
    public ResponseEntity plusCurrentUsageTime(@ApiParam(value = "设备条码id", required = true) @RequestParam @NotNull(message = "设备条码id")Long equipmentBarCodeId,
                                               @ApiParam(value = "设备使用次数", required = true) @RequestParam @NotNull(message = "设备使用次数不能为空") Integer num) {
        return ControllerUtil.returnCRUD(eamEquipmentBarcodeService.plusCurrentUsageTime(equipmentBarCodeId,num));
    }

    @ApiOperation(value = "发送设备预警信息",notes = "发送设备预警信息")
    @PostMapping("/equipmentWarning")
    public ResponseEntity equipmentWarning() {
        return ControllerUtil.returnCRUD(eamEquipmentBarcodeService.equipmentWarning());
    }

    @ApiOperation(value = "将设备状态4改成5",notes = "将设备状态4改成5")
    @GetMapping("/updateEquipmentStatus")
    public ResponseEntity updateEquipmentStatus() {
        return ControllerUtil.returnCRUD(eamEquipmentBarcodeService.updateEquipmentStatus());
    }

    @ApiOperation(value = "将数分钟内没有更新过的设备改成待生产",notes = "将数分钟内没有更新过的设备改成待生产")
    @GetMapping("/updateEquipmentStatusByLongTime")
    public ResponseEntity updateEquipmentStatusByLongTime() {
        return ControllerUtil.returnCRUD(eamEquipmentBarcodeService.updateEquipmentStatusByLongTime());
    }


}
