package com.fantechs.provider.wms.in.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.wms.in.WmsInHtReceivingOrderDto;
import com.fantechs.common.base.general.dto.wms.in.WmsInReceivingOrderDto;
import com.fantechs.common.base.general.dto.wms.in.imports.WmsInReceivingOrderImport;
import com.fantechs.common.base.general.entity.wms.in.WmsInReceivingOrder;
import com.fantechs.common.base.general.entity.wms.in.search.SearchWmsInReceivingOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.CustomFormUtils;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.auth.service.AuthFeignApi;
import com.fantechs.provider.wms.in.service.WmsInHtReceivingOrderService;
import com.fantechs.provider.wms.in.service.WmsInReceivingOrderService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
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
 * Created by mr.lei on 2021/12/13.
 */
@RestController
@Api(tags = "收货作业")
@RequestMapping("/wmsInReceivingOrder")
@Validated
@Slf4j
public class WmsInReceivingOrderController {

    @Resource
    private WmsInReceivingOrderService wmsInReceivingOrderService;
    @Resource
    private WmsInHtReceivingOrderService wmsInHtReceivingOrderService;
    @Resource
    private AuthFeignApi securityFeignApi;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WmsInReceivingOrder wmsInReceivingOrder) {
        return ControllerUtil.returnCRUD(wmsInReceivingOrderService.save(wmsInReceivingOrder));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(wmsInReceivingOrderService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=WmsInReceivingOrder.update.class) WmsInReceivingOrder wmsInReceivingOrder) {
        return ControllerUtil.returnCRUD(wmsInReceivingOrderService.update(wmsInReceivingOrder));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<WmsInReceivingOrder> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        WmsInReceivingOrder  wmsInReceivingOrder = wmsInReceivingOrderService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(wmsInReceivingOrder,StringUtils.isEmpty(wmsInReceivingOrder)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WmsInReceivingOrderDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInReceivingOrder searchWmsInReceivingOrder) {
        Page<Object> page = PageHelper.startPage(searchWmsInReceivingOrder.getStartPage(),searchWmsInReceivingOrder.getPageSize());
        List<WmsInReceivingOrderDto> list = wmsInReceivingOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInReceivingOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<WmsInReceivingOrderDto>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchWmsInReceivingOrder searchWmsInReceivingOrder) {
        List<WmsInReceivingOrderDto> list = wmsInReceivingOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInReceivingOrder));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<WmsInHtReceivingOrderDto>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInReceivingOrder searchWmsInReceivingOrder) {
        Page<Object> page = PageHelper.startPage(searchWmsInReceivingOrder.getStartPage(),searchWmsInReceivingOrder.getPageSize());
        List<WmsInHtReceivingOrderDto> list = wmsInHtReceivingOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInReceivingOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation(value = "下推",notes = "下推")
    @PostMapping("/pushDown")
    public ResponseEntity pushDown(@ApiParam(value = "",required = true)@RequestParam  @NotBlank(message="收货计划明细ID不能为空")String ids) {
        return ControllerUtil.returnCRUD(wmsInReceivingOrderService.pushDown(ids));
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchWmsInReceivingOrder searchWmsInReceivingOrder){
        List<WmsInReceivingOrderDto> list = wmsInReceivingOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInReceivingOrder));
        // 获取自定义导出参数列表
        List<Map<String, Object>> customExportParamList = BeanUtils.objectListToMapList(securityFeignApi.findCustomExportParamList(CustomFormUtils.getFromRout()).getData());
        // 自定义导出操作
        EasyPoiUtils.customExportExcel(list, customExportParamList, "收货作业", "收货作业", "收货作业.xls", response);
    }

    @PostMapping(value = "/import")
    @ApiOperation(value = "从excel导入",notes = "从excel导入")
    public ResponseEntity importExcel(@ApiParam(value ="输入excel文件",required = true) @RequestPart(value="file") MultipartFile file){
        try {
            // 导入操作
            List<WmsInReceivingOrderImport> baseAddressImports = EasyPoiUtils.importExcel(file, 0, 1, WmsInReceivingOrderImport.class);
            Map<String, Object> resultMap = wmsInReceivingOrderService.importExcel(baseAddressImports);
            return ControllerUtil.returnDataSuccess("操作结果集",resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.OPT20012002.getCode());
        }
    }
}
