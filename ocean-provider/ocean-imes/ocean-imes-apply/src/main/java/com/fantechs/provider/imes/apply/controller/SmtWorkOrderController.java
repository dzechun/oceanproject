package com.fantechs.provider.imes.apply.controller;

import com.fantechs.common.base.entity.apply.SmtWorkOrder;
import com.fantechs.common.base.entity.apply.search.SearchSmtWorkOrder;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.apply.service.SmtHtWorkOrderService;
import com.fantechs.provider.imes.apply.service.SmtWorkOrderService;
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
 * Created by wcz on 2020/10/13.
 */
@RestController
@Api(tags = "工单管理")
@RequestMapping("/smtWorkOrder")
@Validated
public class SmtWorkOrderController {

    @Autowired
    private SmtWorkOrderService smtWorkOrderService;
    @Autowired
    private SmtHtWorkOrderService smtHtWorkOrderService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated SmtWorkOrder smtWorkOrder) {
        return ControllerUtil.returnCRUD(smtWorkOrderService.save(smtWorkOrder));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(smtWorkOrderService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=SmtWorkOrder.update.class) SmtWorkOrder smtWorkOrder) {
        return ControllerUtil.returnCRUD(smtWorkOrderService.update(smtWorkOrder));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SmtWorkOrder> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        SmtWorkOrder smtWorkOrder = smtWorkOrderService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(smtWorkOrder,StringUtils.isEmpty(smtWorkOrder)?0:1);
    }

    @ApiOperation("工单列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SmtWorkOrder>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSmtWorkOrder searchSmtWorkOrder) {
        Page<Object> page = PageHelper.startPage(searchSmtWorkOrder.getStartPage(),searchSmtWorkOrder.getPageSize());
        List<SmtWorkOrder> list = smtWorkOrderService.findList(searchSmtWorkOrder);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("工单历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<SmtWorkOrder>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchSmtWorkOrder searchSmtWorkOrder) {
        Page<Object> page = PageHelper.startPage(searchSmtWorkOrder.getStartPage(),searchSmtWorkOrder.getPageSize());
        List<SmtWorkOrder> list = smtHtWorkOrderService.findList(searchSmtWorkOrder);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
                            @RequestBody(required = false) SearchSmtWorkOrder searchSmtWorkOrder){
    List<SmtWorkOrder> list = smtWorkOrderService.findList(searchSmtWorkOrder);
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出工单信息", "工单信息", SmtWorkOrder.class, "工单信息.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
