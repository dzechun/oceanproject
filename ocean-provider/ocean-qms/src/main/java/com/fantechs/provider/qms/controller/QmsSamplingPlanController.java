package com.fantechs.provider.qms.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.qms.QmsSamplingPlan;
import com.fantechs.common.base.general.entity.qms.search.SearchQmsSamplingPlan;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.qms.service.QmsSamplingPlanService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2020/12/23.
 */
@RestController
@Api(tags = "抽样方案")
@RequestMapping("/qmsSamplingPlan")
@Validated
public class QmsSamplingPlanController {

    @Autowired
    private QmsSamplingPlanService qmsSamplingPlanService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated QmsSamplingPlan qmsSamplingPlan) {
        return ControllerUtil.returnCRUD(qmsSamplingPlanService.save(qmsSamplingPlan));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(qmsSamplingPlanService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=QmsSamplingPlan.update.class) QmsSamplingPlan qmsSamplingPlan) {
        return ControllerUtil.returnCRUD(qmsSamplingPlanService.update(qmsSamplingPlan));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<QmsSamplingPlan> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        QmsSamplingPlan  qmsSamplingPlan = qmsSamplingPlanService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(qmsSamplingPlan,StringUtils.isEmpty(qmsSamplingPlan)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<QmsSamplingPlan>> findList(@ApiParam(value = "查询对象")@RequestBody SearchQmsSamplingPlan searchQmsSamplingPlan) {
        Page<Object> page = PageHelper.startPage(searchQmsSamplingPlan.getStartPage(),searchQmsSamplingPlan.getPageSize());
        List<QmsSamplingPlan> list = qmsSamplingPlanService.findList(ControllerUtil.dynamicConditionByEntity(searchQmsSamplingPlan));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchQmsSamplingPlan searchQmsSamplingPlan){
    List<QmsSamplingPlan> list = qmsSamplingPlanService.findList(ControllerUtil.dynamicConditionByEntity(searchQmsSamplingPlan));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "QmsSamplingPlan信息", QmsSamplingPlan.class, "QmsSamplingPlan.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
