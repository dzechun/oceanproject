package com.fantechs.provider.mes.pm.controller;

import com.fantechs.common.base.general.entity.mes.pm.MesPmProductionKeyIssuesOrder;
import com.fantechs.common.base.general.entity.mes.pm.history.MesPmHtProductionKeyIssuesOrder;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmProductionKeyIssuesOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.CustomFormUtils;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.auth.service.AuthFeignApi;
import com.fantechs.provider.mes.pm.service.MesPmHtProductionKeyIssuesOrderService;
import com.fantechs.provider.mes.pm.service.MesPmProductionKeyIssuesOrderService;
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
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/06/11.
 */
@RestController
@Api(tags = "产前关键事项确认")
@RequestMapping("/mesPmProductionKeyIssuesOrder")
@Validated
public class MesPmProductionKeyIssuesOrderController {

    @Resource
    private MesPmProductionKeyIssuesOrderService mesPmProductionKeyIssuesOrderService;
    @Resource
    private MesPmHtProductionKeyIssuesOrderService mesPmHtProductionKeyIssuesOrderService;
    @Resource
    private AuthFeignApi securityFeignApi;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated MesPmProductionKeyIssuesOrder mesPmProductionKeyIssuesOrder) {
        return ControllerUtil.returnCRUD(mesPmProductionKeyIssuesOrderService.save(mesPmProductionKeyIssuesOrder));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(mesPmProductionKeyIssuesOrderService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value= MesPmProductionKeyIssuesOrder.update.class) MesPmProductionKeyIssuesOrder mesPmProductionKeyIssuesOrder) {
        return ControllerUtil.returnCRUD(mesPmProductionKeyIssuesOrderService.update(mesPmProductionKeyIssuesOrder));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<MesPmProductionKeyIssuesOrder> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        MesPmProductionKeyIssuesOrder mesPmProductionKeyIssuesOrder = mesPmProductionKeyIssuesOrderService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(mesPmProductionKeyIssuesOrder,StringUtils.isEmpty(mesPmProductionKeyIssuesOrder)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<MesPmProductionKeyIssuesOrder>> findList(@ApiParam(value = "查询对象")@RequestBody SearchMesPmProductionKeyIssuesOrder searchMesPmProductionKeyIssuesOrder) {
        Page<Object> page = PageHelper.startPage(searchMesPmProductionKeyIssuesOrder.getStartPage(), searchMesPmProductionKeyIssuesOrder.getPageSize());
        List<MesPmProductionKeyIssuesOrder> list = mesPmProductionKeyIssuesOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchMesPmProductionKeyIssuesOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("PDA查询产前关键事项")
    @PostMapping("/PDAFindOne")
    public ResponseEntity<MesPmProductionKeyIssuesOrder> PDAFindOne(@ApiParam(value = "工单号",required = true)@RequestParam  @NotBlank(message="工单号不能为空") String workOrderCode) {
        MesPmProductionKeyIssuesOrder mesPmProductionKeyIssuesOrder = mesPmProductionKeyIssuesOrderService.PDAFindOne(workOrderCode);
        return ControllerUtil.returnDataSuccess(mesPmProductionKeyIssuesOrder,StringUtils.isEmpty(mesPmProductionKeyIssuesOrder)?0:1);
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<MesPmHtProductionKeyIssuesOrder>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchMesPmProductionKeyIssuesOrder searchMesPmProductionKeyIssuesOrder) {
        Page<Object> page = PageHelper.startPage(searchMesPmProductionKeyIssuesOrder.getStartPage(), searchMesPmProductionKeyIssuesOrder.getPageSize());
        List<MesPmHtProductionKeyIssuesOrder> list = mesPmHtProductionKeyIssuesOrderService.findHtList(ControllerUtil.dynamicConditionByEntity(searchMesPmProductionKeyIssuesOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchMesPmProductionKeyIssuesOrder searchMesPmProductionKeyIssuesOrder){
        List<MesPmProductionKeyIssuesOrder> list = mesPmProductionKeyIssuesOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchMesPmProductionKeyIssuesOrder));
        // 获取自定义导出参数列表
        List<Map<String, Object>> customExportParamList = BeanUtils.objectListToMapList(securityFeignApi.findCustomExportParamList(CustomFormUtils.getFromRout()).getData());
        // 自定义导出操作
        EasyPoiUtils.customExportExcel(list, customExportParamList, "导出信息", "产前关键事项确认", "产前关键事项确认.xls", response);
    }
}
