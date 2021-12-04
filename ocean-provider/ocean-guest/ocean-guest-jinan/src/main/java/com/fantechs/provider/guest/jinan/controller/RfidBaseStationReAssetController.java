package com.fantechs.provider.guest.jinan.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.jinan.RfidBaseStationReAsset;
import com.fantechs.common.base.general.entity.jinan.search.SearchRfidBaseStationReAsset;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.guest.jinan.service.RfidBaseStationReAssetService;
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
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/11/30.
 */
@RestController
@Api(tags = "RFID基站绑定资产")
@RequestMapping("/rfidBaseStationReAsset")
@Validated
@Slf4j
public class RfidBaseStationReAssetController {

    @Resource
    private RfidBaseStationReAssetService rfidBaseStationReAssetService;

    @ApiOperation(value = "批量新增",notes = "批量新增")
    @PostMapping("/batchAdd")
    public ResponseEntity batchAdd(@ApiParam(value = "必传：",required = true)@RequestBody @Validated @NotEmpty(message="列表不能为空")List<RfidBaseStationReAsset> list) {
        return ControllerUtil.returnCRUD(rfidBaseStationReAssetService.batchAdd(list));
    }

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated RfidBaseStationReAsset rfidBaseStationReAsset) {
        return ControllerUtil.returnCRUD(rfidBaseStationReAssetService.save(rfidBaseStationReAsset));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(rfidBaseStationReAssetService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=RfidBaseStationReAsset.update.class) RfidBaseStationReAsset rfidBaseStationReAsset) {
        return ControllerUtil.returnCRUD(rfidBaseStationReAssetService.update(rfidBaseStationReAsset));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<RfidBaseStationReAsset> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        RfidBaseStationReAsset  rfidBaseStationReAsset = rfidBaseStationReAssetService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(rfidBaseStationReAsset,StringUtils.isEmpty(rfidBaseStationReAsset)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<RfidBaseStationReAsset>> findList(@ApiParam(value = "查询对象")@RequestBody SearchRfidBaseStationReAsset searchRfidBaseStationReAsset) {
        Page<Object> page = PageHelper.startPage(searchRfidBaseStationReAsset.getStartPage(),searchRfidBaseStationReAsset.getPageSize());
        List<RfidBaseStationReAsset> list = rfidBaseStationReAssetService.findList(ControllerUtil.dynamicConditionByEntity(searchRfidBaseStationReAsset));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<RfidBaseStationReAsset>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchRfidBaseStationReAsset searchRfidBaseStationReAsset) {
        List<RfidBaseStationReAsset> list = rfidBaseStationReAssetService.findList(ControllerUtil.dynamicConditionByEntity(searchRfidBaseStationReAsset));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }


    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchRfidBaseStationReAsset searchRfidBaseStationReAsset){
    List<RfidBaseStationReAsset> list = rfidBaseStationReAssetService.findList(ControllerUtil.dynamicConditionByEntity(searchRfidBaseStationReAsset));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "RfidBaseStationReAsset信息", RfidBaseStationReAsset.class, "RfidBaseStationReAsset.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
