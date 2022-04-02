package com.fantechs.provider.guest.wanbao.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.wanbao.QmsInspectionOrderDetSampleBeforeRecheck;
import com.fantechs.common.base.general.entity.wanbao.search.SearchQmsInspectionOrderDetSampleBeforeRecheck;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.guest.wanbao.service.QmsInspectionOrderDetSampleBeforeRecheckService;
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
 * Created by leifengzhi on 2022/04/01.
 */
@RestController
@Api(tags = "复检前检验单明细样本")
@RequestMapping("/qmsInspectionOrderDetSampleBeforeRecheck")
@Validated
public class QmsInspectionOrderDetSampleBeforeRecheckController {

    @Resource
    private QmsInspectionOrderDetSampleBeforeRecheckService qmsInspectionOrderDetSampleBeforeRecheckService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated QmsInspectionOrderDetSampleBeforeRecheck qmsInspectionOrderDetSampleBeforeRecheck) {
        return ControllerUtil.returnCRUD(qmsInspectionOrderDetSampleBeforeRecheckService.save(qmsInspectionOrderDetSampleBeforeRecheck));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(qmsInspectionOrderDetSampleBeforeRecheckService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=QmsInspectionOrderDetSampleBeforeRecheck.update.class) QmsInspectionOrderDetSampleBeforeRecheck qmsInspectionOrderDetSampleBeforeRecheck) {
        return ControllerUtil.returnCRUD(qmsInspectionOrderDetSampleBeforeRecheckService.update(qmsInspectionOrderDetSampleBeforeRecheck));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<QmsInspectionOrderDetSampleBeforeRecheck> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        QmsInspectionOrderDetSampleBeforeRecheck  qmsInspectionOrderDetSampleBeforeRecheck = qmsInspectionOrderDetSampleBeforeRecheckService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(qmsInspectionOrderDetSampleBeforeRecheck,StringUtils.isEmpty(qmsInspectionOrderDetSampleBeforeRecheck)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<QmsInspectionOrderDetSampleBeforeRecheck>> findList(@ApiParam(value = "查询对象")@RequestBody SearchQmsInspectionOrderDetSampleBeforeRecheck searchQmsInspectionOrderDetSampleBeforeRecheck) {
        Page<Object> page = PageHelper.startPage(searchQmsInspectionOrderDetSampleBeforeRecheck.getStartPage(),searchQmsInspectionOrderDetSampleBeforeRecheck.getPageSize());
        List<QmsInspectionOrderDetSampleBeforeRecheck> list = qmsInspectionOrderDetSampleBeforeRecheckService.findList(ControllerUtil.dynamicConditionByEntity(searchQmsInspectionOrderDetSampleBeforeRecheck));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<QmsInspectionOrderDetSampleBeforeRecheck>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchQmsInspectionOrderDetSampleBeforeRecheck searchQmsInspectionOrderDetSampleBeforeRecheck) {
        List<QmsInspectionOrderDetSampleBeforeRecheck> list = qmsInspectionOrderDetSampleBeforeRecheckService.findList(ControllerUtil.dynamicConditionByEntity(searchQmsInspectionOrderDetSampleBeforeRecheck));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }


    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchQmsInspectionOrderDetSampleBeforeRecheck searchQmsInspectionOrderDetSampleBeforeRecheck){
    List<QmsInspectionOrderDetSampleBeforeRecheck> list = qmsInspectionOrderDetSampleBeforeRecheckService.findList(ControllerUtil.dynamicConditionByEntity(searchQmsInspectionOrderDetSampleBeforeRecheck));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "QmsInspectionOrderDetSampleBeforeRecheck信息", QmsInspectionOrderDetSampleBeforeRecheck.class, "QmsInspectionOrderDetSampleBeforeRecheck.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
