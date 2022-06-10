package com.fantechs.provider.srm.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.srm.SrmPlanDeliveryOrderDto;
import com.fantechs.common.base.general.dto.srm.imports.SrmPlanDeliveryOrderImport;
import com.fantechs.common.base.general.entity.srm.SrmPlanDeliveryOrder;
import com.fantechs.common.base.general.entity.srm.history.SrmHtPlanDeliveryOrder;
import com.fantechs.common.base.general.entity.srm.search.SearchSrmPlanDeliveryOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.CustomFormUtils;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.auth.service.AuthFeignApi;
import com.fantechs.provider.srm.service.SrmHtPlanDeliveryOrderService;
import com.fantechs.provider.srm.service.SrmPlanDeliveryOrderService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/11/29.
 */
@RestController
@Api(tags = "送货计划控制器")
@RequestMapping("/srmPlanDeliveryOrder")
@Validated
@Log4j
public class SrmPlanDeliveryOrderController {

    @Resource
    private SrmPlanDeliveryOrderService srmPlanDeliveryOrderService;
    @Resource
    private SrmHtPlanDeliveryOrderService srmHtPlanDeliveryOrderService;
    @Resource
    private AuthFeignApi securityFeignApi;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated SrmPlanDeliveryOrder srmPlanDeliveryOrder) {
        return ControllerUtil.returnCRUD(srmPlanDeliveryOrderService.save(srmPlanDeliveryOrder));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(srmPlanDeliveryOrderService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=SrmPlanDeliveryOrder.update.class) SrmPlanDeliveryOrder srmPlanDeliveryOrder) {
        return ControllerUtil.returnCRUD(srmPlanDeliveryOrderService.update(srmPlanDeliveryOrder));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SrmPlanDeliveryOrder> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        SrmPlanDeliveryOrder  srmPlanDeliveryOrder = srmPlanDeliveryOrderService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(srmPlanDeliveryOrder,StringUtils.isEmpty(srmPlanDeliveryOrder)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SrmPlanDeliveryOrderDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSrmPlanDeliveryOrder searchSrmPlanDeliveryOrder) {
        Page<Object> page = PageHelper.startPage(searchSrmPlanDeliveryOrder.getStartPage(),searchSrmPlanDeliveryOrder.getPageSize());
        List<SrmPlanDeliveryOrderDto> list = srmPlanDeliveryOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchSrmPlanDeliveryOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<SrmPlanDeliveryOrderDto>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchSrmPlanDeliveryOrder searchSrmPlanDeliveryOrder) {
        List<SrmPlanDeliveryOrderDto> list = srmPlanDeliveryOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchSrmPlanDeliveryOrder));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<SrmHtPlanDeliveryOrder>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchSrmPlanDeliveryOrder searchSrmPlanDeliveryOrder) {
        Page<Object> page = PageHelper.startPage(searchSrmPlanDeliveryOrder.getStartPage(),searchSrmPlanDeliveryOrder.getPageSize());
        List<SrmHtPlanDeliveryOrder> list = srmHtPlanDeliveryOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchSrmPlanDeliveryOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchSrmPlanDeliveryOrder searchSrmPlanDeliveryOrder){
    List<SrmPlanDeliveryOrderDto> list = srmPlanDeliveryOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchSrmPlanDeliveryOrder));
        // 获取自定义导出参数列表
        List<Map<String, Object>> customExportParamList = BeanUtils.objectListToMapList(securityFeignApi.findCustomExportParamList(CustomFormUtils.getFromRout()).getData());
        // 自定义导出操作
        EasyPoiUtils.customExportExcel(list, customExportParamList, "导出信息", "SrmPlanDeliveryOrder信息", "SrmPlanDeliveryOrder.xls", response);
    }

    @PostMapping(value = "/import")
    @ApiOperation(value = "从excel导入",notes = "从excel导入")
    public ResponseEntity importExcel(@ApiParam(value ="输入excel文件",required = true) @RequestPart(value="file") MultipartFile file){
        try {
            // 导入操作
            List<SrmPlanDeliveryOrderImport> list = EasyPoiUtils.importExcel(file, 2, 1, SrmPlanDeliveryOrderImport.class);
            Map<String, Object> resultMap = srmPlanDeliveryOrderService.importExcel(list);
            return ControllerUtil.returnDataSuccess("操作结果集",resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.OPT20012002.getCode());
        }
    }
}
