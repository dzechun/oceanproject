package com.fantechs.provider.wms.inner.controller;

import com.fantechs.common.base.general.dto.wms.inner.WmsInnerDirectTransferOrderDto;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerDirectTransferOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.CustomFormUtils;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.auth.service.AuthFeignApi;
import com.fantechs.provider.wms.inner.service.WmsInnerDirectTransferOrderService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/12/21.
 */
@RestController
@Api(tags = "直接调拨单")
@RequestMapping("/wmsInnerDirectTransferOrder")
@Validated
public class WmsInnerDirectTransferOrderController {

    @Resource
    private WmsInnerDirectTransferOrderService wmsInnerDirectTransferOrderService;
    @Resource
    private AuthFeignApi securityFeignApi;

/*    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WmsInnerDirectTransferOrder wmsInnerDirectTransferOrder) {
        return ControllerUtil.returnCRUD(wmsInnerDirectTransferOrderService.save(wmsInnerDirectTransferOrder));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(wmsInnerDirectTransferOrderService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=WmsInnerDirectTransferOrder.update.class) WmsInnerDirectTransferOrder wmsInnerDirectTransferOrder) {
        return ControllerUtil.returnCRUD(wmsInnerDirectTransferOrderService.update(wmsInnerDirectTransferOrder));
    }*/

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<WmsInnerDirectTransferOrderDto> detail(@ApiParam(value = "ID",required = true)@RequestParam @NotNull(message="id不能为空") Long id) {
        WmsInnerDirectTransferOrderDto wmsInnerDirectTransferOrderDto = wmsInnerDirectTransferOrderService.detail(id);
        return  ControllerUtil.returnDataSuccess(wmsInnerDirectTransferOrderDto, StringUtils.isEmpty(wmsInnerDirectTransferOrderDto)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WmsInnerDirectTransferOrderDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInnerDirectTransferOrder searchWmsInnerDirectTransferOrder) {
        Page<Object> page = PageHelper.startPage(searchWmsInnerDirectTransferOrder.getStartPage(),searchWmsInnerDirectTransferOrder.getPageSize());
        List<WmsInnerDirectTransferOrderDto> list = wmsInnerDirectTransferOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInnerDirectTransferOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<WmsInnerDirectTransferOrderDto>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchWmsInnerDirectTransferOrder searchWmsInnerDirectTransferOrder) {
        List<WmsInnerDirectTransferOrderDto> list = wmsInnerDirectTransferOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInnerDirectTransferOrder));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchWmsInnerDirectTransferOrder searchWmsInnerDirectTransferOrder){
    List<WmsInnerDirectTransferOrderDto> list = wmsInnerDirectTransferOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInnerDirectTransferOrder));
        // 获取自定义导出参数列表
        List<Map<String, Object>> customExportParamList = BeanUtils.objectListToMapList(securityFeignApi.findCustomExportParamList(CustomFormUtils.getFromRout()).getData());
        // 自定义导出操作
        EasyPoiUtils.customExportExcel(list, customExportParamList, "导出信息", "WmsInnerDirectTransferOrder信息", "WmsInnerDirectTransferOrder.xls", response);
    }

}
