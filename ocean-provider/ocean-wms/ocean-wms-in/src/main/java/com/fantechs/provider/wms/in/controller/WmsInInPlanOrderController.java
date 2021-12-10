package com.fantechs.provider.wms.in.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.in.WmsInInPlanOrderDto;
import com.fantechs.common.base.general.entity.wms.in.WmsInInPlanOrder;
import com.fantechs.common.base.general.entity.wms.in.search.SearchWmsInInPlanOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.in.service.WmsInHtInPlanOrderService;
import com.fantechs.provider.wms.in.service.WmsInInPlanOrderService;
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
 * Created by leifengzhi on 2021/12/08.
 */
@RestController
@Api(tags = "入库计划")
@RequestMapping("/wmsInInPlanOrder")
@Validated
@Slf4j
public class WmsInInPlanOrderController {

    @Resource
    private WmsInInPlanOrderService wmsInInPlanOrderService;
    @Resource
    private WmsInHtInPlanOrderService wmsInHtInPlanOrderService;


    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WmsInInPlanOrder wmsInInPlanOrder) {
        return ControllerUtil.returnCRUD(wmsInInPlanOrderService.save(wmsInInPlanOrder));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(wmsInInPlanOrderService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=WmsInInPlanOrder.update.class) WmsInInPlanOrder wmsInInPlanOrder) {
        return ControllerUtil.returnCRUD(wmsInInPlanOrderService.update(wmsInInPlanOrder));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<WmsInInPlanOrder> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        WmsInInPlanOrder  wmsInInPlanOrder = wmsInInPlanOrderService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(wmsInInPlanOrder,StringUtils.isEmpty(wmsInInPlanOrder)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WmsInInPlanOrderDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInInPlanOrder searchWmsInInPlanOrder) {
        Page<Object> page = PageHelper.startPage(searchWmsInInPlanOrder.getStartPage(),searchWmsInInPlanOrder.getPageSize());
        List<WmsInInPlanOrderDto> list = wmsInInPlanOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInInPlanOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<WmsInInPlanOrderDto>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchWmsInInPlanOrder searchWmsInInPlanOrder) {
        List<WmsInInPlanOrderDto> list = wmsInInPlanOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInInPlanOrder));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<WmsInInPlanOrderDto>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInInPlanOrder searchWmsInInPlanOrder) {
        Page<Object> page = PageHelper.startPage(searchWmsInInPlanOrder.getStartPage(),searchWmsInInPlanOrder.getPageSize());
        List<WmsInInPlanOrderDto> list = wmsInHtInPlanOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInInPlanOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchWmsInInPlanOrder searchWmsInInPlanOrder){
    List<WmsInInPlanOrderDto> list = wmsInInPlanOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInInPlanOrder));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "WmsInInPlanOrder信息", WmsInInPlanOrderDto.class, "WmsInInPlanOrder.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

    @PostMapping(value = "/import")
    @ApiOperation(value = "从excel导入",notes = "从excel导入")
    public ResponseEntity importExcel(@ApiParam(value ="输入excel文件",required = true) @RequestPart(value="file") MultipartFile file){
        try {
            // 导入操作
            List<WmsInInPlanOrder> baseAddressImports = EasyPoiUtils.importExcel(file, 0, 1, WmsInInPlanOrder.class);
            Map<String, Object> resultMap = wmsInInPlanOrderService.importExcel(baseAddressImports);
            return ControllerUtil.returnDataSuccess("操作结果集",resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.OPT20012002.getCode());
        }
    }
}
