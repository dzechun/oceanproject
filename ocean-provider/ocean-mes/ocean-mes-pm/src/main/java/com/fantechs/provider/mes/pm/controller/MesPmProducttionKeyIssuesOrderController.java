package com.fantechs.provider.mes.pm.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.mes.pm.MesPmProducttionKeyIssuesOrder;
import com.fantechs.common.base.general.entity.mes.pm.history.MesPmHtProducttionKeyIssuesOrder;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmProducttionKeyIssuesOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.mes.pm.service.MesPmHtProducttionKeyIssuesOrderService;
import com.fantechs.provider.mes.pm.service.MesPmProducttionKeyIssuesOrderService;
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
 * Created by leifengzhi on 2021/06/11.
 */
@RestController
@Api(tags = "产前关键事项确认")
@RequestMapping("/mesPmProducttionKeyIssuesOrder")
@Validated
public class MesPmProducttionKeyIssuesOrderController {

    @Resource
    private MesPmProducttionKeyIssuesOrderService mesPmProducttionKeyIssuesOrderService;
    @Resource
    private MesPmHtProducttionKeyIssuesOrderService mesPmHtProducttionKeyIssuesOrderService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated MesPmProducttionKeyIssuesOrder mesPmProducttionKeyIssuesOrder) {
        return ControllerUtil.returnCRUD(mesPmProducttionKeyIssuesOrderService.save(mesPmProducttionKeyIssuesOrder));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(mesPmProducttionKeyIssuesOrderService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=MesPmProducttionKeyIssuesOrder.update.class) MesPmProducttionKeyIssuesOrder mesPmProducttionKeyIssuesOrder) {
        return ControllerUtil.returnCRUD(mesPmProducttionKeyIssuesOrderService.update(mesPmProducttionKeyIssuesOrder));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<MesPmProducttionKeyIssuesOrder> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        MesPmProducttionKeyIssuesOrder  mesPmProducttionKeyIssuesOrder = mesPmProducttionKeyIssuesOrderService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(mesPmProducttionKeyIssuesOrder,StringUtils.isEmpty(mesPmProducttionKeyIssuesOrder)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<MesPmProducttionKeyIssuesOrder>> findList(@ApiParam(value = "查询对象")@RequestBody SearchMesPmProducttionKeyIssuesOrder searchMesPmProducttionKeyIssuesOrder) {
        Page<Object> page = PageHelper.startPage(searchMesPmProducttionKeyIssuesOrder.getStartPage(),searchMesPmProducttionKeyIssuesOrder.getPageSize());
        List<MesPmProducttionKeyIssuesOrder> list = mesPmProducttionKeyIssuesOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchMesPmProducttionKeyIssuesOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("PDA查询产前关键事项")
    @PostMapping("/PDAFindOne")
    public ResponseEntity<MesPmProducttionKeyIssuesOrder> PDAFindOne(@ApiParam(value = "工单号",required = true)@RequestParam  @NotBlank(message="工单号不能为空") String workOrderCode) {
        MesPmProducttionKeyIssuesOrder mesPmProducttionKeyIssuesOrder = mesPmProducttionKeyIssuesOrderService.PDAFindOne(workOrderCode);
        return ControllerUtil.returnDataSuccess(mesPmProducttionKeyIssuesOrder,StringUtils.isEmpty(mesPmProducttionKeyIssuesOrder)?0:1);
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<MesPmHtProducttionKeyIssuesOrder>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchMesPmProducttionKeyIssuesOrder searchMesPmProducttionKeyIssuesOrder) {
        Page<Object> page = PageHelper.startPage(searchMesPmProducttionKeyIssuesOrder.getStartPage(),searchMesPmProducttionKeyIssuesOrder.getPageSize());
        List<MesPmHtProducttionKeyIssuesOrder> list = mesPmHtProducttionKeyIssuesOrderService.findHtList(ControllerUtil.dynamicConditionByEntity(searchMesPmProducttionKeyIssuesOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchMesPmProducttionKeyIssuesOrder searchMesPmProducttionKeyIssuesOrder){
    List<MesPmProducttionKeyIssuesOrder> list = mesPmProducttionKeyIssuesOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchMesPmProducttionKeyIssuesOrder));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "产前关键事项确认", MesPmProducttionKeyIssuesOrder.class, "产前关键事项确认.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
