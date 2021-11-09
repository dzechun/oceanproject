package com.fantechs.provider.guest.eng.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.eng.EngLogisticsRecord;
import com.fantechs.common.base.general.entity.eng.search.SearchEngLogisticsRecord;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.guest.eng.service.EngLogisticsRecordService;
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
 * Created by leifengzhi on 2021/11/03.
 */
@RestController
@Api(tags = "物流跟踪记录")
@RequestMapping("/engLogisticsRecord")
@Validated
@Slf4j
public class EngLogisticsRecordController {

    @Resource
    private EngLogisticsRecordService engLogisticsRecordService;

    @ApiOperation(value = "获取未读信息数",notes = "获取未读信息数")
    @PostMapping("/getUnReadCount")
    public ResponseEntity getUnReadCount() {
        return ControllerUtil.returnDataSuccess(engLogisticsRecordService.getUnReadCount(),1);
    }

    @ApiOperation(value = "批量新增",notes = "批量新增")
    @PostMapping("/batchSave")
    public ResponseEntity batchSave(@ApiParam(value = "必传：",required = true)@RequestBody @Validated List<EngLogisticsRecord> list) {
        return ControllerUtil.returnCRUD(engLogisticsRecordService.batchSave(list));
    }

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated EngLogisticsRecord engLogisticsRecord) {
        return ControllerUtil.returnCRUD(engLogisticsRecordService.save(engLogisticsRecord));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(engLogisticsRecordService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=EngLogisticsRecord.update.class) EngLogisticsRecord engLogisticsRecord) {
        return ControllerUtil.returnCRUD(engLogisticsRecordService.update(engLogisticsRecord));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<EngLogisticsRecord> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        EngLogisticsRecord  engLogisticsRecord = engLogisticsRecordService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(engLogisticsRecord,StringUtils.isEmpty(engLogisticsRecord)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<EngLogisticsRecord>> findList(@ApiParam(value = "查询对象")@RequestBody SearchEngLogisticsRecord searchEngLogisticsRecord) {
        Page<Object> page = PageHelper.startPage(searchEngLogisticsRecord.getStartPage(),searchEngLogisticsRecord.getPageSize());
        List<EngLogisticsRecord> list = engLogisticsRecordService.findList(ControllerUtil.dynamicConditionByEntity(searchEngLogisticsRecord));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<EngLogisticsRecord>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchEngLogisticsRecord searchEngLogisticsRecord) {
        List<EngLogisticsRecord> list = engLogisticsRecordService.findList(ControllerUtil.dynamicConditionByEntity(searchEngLogisticsRecord));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchEngLogisticsRecord searchEngLogisticsRecord){
    List<EngLogisticsRecord> list = engLogisticsRecordService.findList(ControllerUtil.dynamicConditionByEntity(searchEngLogisticsRecord));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "EngLogisticsRecord信息", EngLogisticsRecord.class, "EngLogisticsRecord.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

}
