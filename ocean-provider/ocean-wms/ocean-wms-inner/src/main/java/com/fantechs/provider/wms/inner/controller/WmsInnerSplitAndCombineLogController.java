package com.fantechs.provider.wms.inner.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerSplitAndCombineLog;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerSplitAndCombineLog;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.inner.service.WmsInnerSplitAndCombineLogService;
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
 * Created by leifengzhi on 2021/12/27.
 */
@RestController
@Api(tags = "分合包箱栈板日志")
@RequestMapping("/wmsInnerSplitAndCombineLog")
@Validated
public class WmsInnerSplitAndCombineLogController {

    @Resource
    private WmsInnerSplitAndCombineLogService wmsInnerSplitAndCombineLogService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WmsInnerSplitAndCombineLog wmsInnerSplitAndCombineLog) {
        return ControllerUtil.returnCRUD(wmsInnerSplitAndCombineLogService.save(wmsInnerSplitAndCombineLog));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(wmsInnerSplitAndCombineLogService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=WmsInnerSplitAndCombineLog.update.class) WmsInnerSplitAndCombineLog wmsInnerSplitAndCombineLog) {
        return ControllerUtil.returnCRUD(wmsInnerSplitAndCombineLogService.update(wmsInnerSplitAndCombineLog));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<WmsInnerSplitAndCombineLog> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        WmsInnerSplitAndCombineLog  wmsInnerSplitAndCombineLog = wmsInnerSplitAndCombineLogService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(wmsInnerSplitAndCombineLog,StringUtils.isEmpty(wmsInnerSplitAndCombineLog)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WmsInnerSplitAndCombineLog>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInnerSplitAndCombineLog searchWmsInnerSplitAndCombineLog) {
        Page<Object> page = PageHelper.startPage(searchWmsInnerSplitAndCombineLog.getStartPage(),searchWmsInnerSplitAndCombineLog.getPageSize());
        List<WmsInnerSplitAndCombineLog> list = wmsInnerSplitAndCombineLogService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInnerSplitAndCombineLog));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }



    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchWmsInnerSplitAndCombineLog searchWmsInnerSplitAndCombineLog){
    List<WmsInnerSplitAndCombineLog> list = wmsInnerSplitAndCombineLogService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInnerSplitAndCombineLog));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "WmsInnerSplitAndCombineLog信息", WmsInnerSplitAndCombineLog.class, "WmsInnerSplitAndCombineLog.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

}
