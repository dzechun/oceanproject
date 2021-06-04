package com.fantechs.provider.qms.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.qms.QmsInspectionOrder;
import com.fantechs.common.base.general.entity.qms.QmsInspectionOrderDet;
import com.fantechs.common.base.general.entity.qms.history.QmsHtInspectionOrder;
import com.fantechs.common.base.general.entity.qms.search.SearchQmsInspectionOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.qms.service.QmsHtInspectionOrderService;
import com.fantechs.provider.qms.service.QmsInspectionOrderService;
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
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/05/25.
 */
@RestController
@Api(tags = "成品检验单")
@RequestMapping("/qmsInspectionOrder")
@Validated
public class QmsInspectionOrderController {

    @Resource
    private QmsInspectionOrderService qmsInspectionOrderService;
    @Resource
    private QmsHtInspectionOrderService qmsHtInspectionOrderService;

    @ApiOperation("PDA提交")
    @PostMapping("/PDASubmit")
    public ResponseEntity PDASubmit(@ApiParam(value = "检验单id",required = true) @RequestParam @NotNull(message="检验单id不能为空") Long inspectionOrderId) {
        return ControllerUtil.returnCRUD(qmsInspectionOrderService.writeBack(inspectionOrderId));
    }

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated QmsInspectionOrder qmsInspectionOrder) {
        return ControllerUtil.returnCRUD(qmsInspectionOrderService.save(qmsInspectionOrder));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(qmsInspectionOrderService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=QmsInspectionOrder.update.class) QmsInspectionOrder qmsInspectionOrder) {
        return ControllerUtil.returnCRUD(qmsInspectionOrderService.update(qmsInspectionOrder));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<QmsInspectionOrder> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        QmsInspectionOrder  qmsInspectionOrder = qmsInspectionOrderService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(qmsInspectionOrder,StringUtils.isEmpty(qmsInspectionOrder)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<QmsInspectionOrder>> findList(@ApiParam(value = "查询对象")@RequestBody SearchQmsInspectionOrder searchQmsInspectionOrder) {
        Page<Object> page = PageHelper.startPage(searchQmsInspectionOrder.getStartPage(),searchQmsInspectionOrder.getPageSize());
        List<QmsInspectionOrder> list = qmsInspectionOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchQmsInspectionOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<QmsHtInspectionOrder>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchQmsInspectionOrder searchQmsInspectionOrder) {
        Page<Object> page = PageHelper.startPage(searchQmsInspectionOrder.getStartPage(),searchQmsInspectionOrder.getPageSize());
        List<QmsHtInspectionOrder> list = qmsHtInspectionOrderService.findHtList(ControllerUtil.dynamicConditionByEntity(searchQmsInspectionOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchQmsInspectionOrder searchQmsInspectionOrder){
    List<QmsInspectionOrder> list = qmsInspectionOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchQmsInspectionOrder));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "成品检验单信息", QmsInspectionOrder.class, "成品检验单.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
