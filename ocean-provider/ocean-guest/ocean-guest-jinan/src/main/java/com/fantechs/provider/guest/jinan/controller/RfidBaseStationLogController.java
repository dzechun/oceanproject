package com.fantechs.provider.guest.jinan.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.jinan.RfidBaseStationData;
import com.fantechs.common.base.general.entity.jinan.RfidBaseStationLog;
import com.fantechs.common.base.general.entity.jinan.search.SearchRfidBaseStationLog;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.guest.jinan.service.RfidBaseStationLogService;
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
 * Created by leifengzhi on 2021/11/29.
 */
@RestController
@Api(tags = "RFID资产管理基站日志")
@RequestMapping("/rfidBaseStationLog")
@Validated
public class RfidBaseStationLogController {

    @Resource
    private RfidBaseStationLogService rfidBaseStationLogService;

    @ApiOperation(value = "校验数据",notes = "校验数据")
    @PostMapping("/checkData")
    public ResponseEntity checkData(@ApiParam(value = "必传：",required = true)@RequestBody @Validated RfidBaseStationData rfidBaseStationData) {
        return ControllerUtil.returnCRUD(rfidBaseStationLogService.checkData(rfidBaseStationData));
    }

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated RfidBaseStationLog rfidBaseStationLog) {
        return ControllerUtil.returnCRUD(rfidBaseStationLogService.save(rfidBaseStationLog));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(rfidBaseStationLogService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=RfidBaseStationLog.update.class) RfidBaseStationLog rfidBaseStationLog) {
        return ControllerUtil.returnCRUD(rfidBaseStationLogService.update(rfidBaseStationLog));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<RfidBaseStationLog> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        RfidBaseStationLog  rfidBaseStationLog = rfidBaseStationLogService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(rfidBaseStationLog,StringUtils.isEmpty(rfidBaseStationLog)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<RfidBaseStationLog>> findList(@ApiParam(value = "查询对象")@RequestBody SearchRfidBaseStationLog searchRfidBaseStationLog) {
        Page<Object> page = PageHelper.startPage(searchRfidBaseStationLog.getStartPage(),searchRfidBaseStationLog.getPageSize());
        List<RfidBaseStationLog> list = rfidBaseStationLogService.findList(ControllerUtil.dynamicConditionByEntity(searchRfidBaseStationLog));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<RfidBaseStationLog>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchRfidBaseStationLog searchRfidBaseStationLog) {
        List<RfidBaseStationLog> list = rfidBaseStationLogService.findList(ControllerUtil.dynamicConditionByEntity(searchRfidBaseStationLog));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchRfidBaseStationLog searchRfidBaseStationLog){
    List<RfidBaseStationLog> list = rfidBaseStationLogService.findList(ControllerUtil.dynamicConditionByEntity(searchRfidBaseStationLog));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "RFID资产管理基站日志", RfidBaseStationLog.class, "RFID资产管理基站日志.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

}
