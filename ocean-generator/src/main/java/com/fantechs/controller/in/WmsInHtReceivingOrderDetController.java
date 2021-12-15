package com.fantechs.controller.in;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.entity.WmsInHtReceivingOrderDet;
import com.fantechs.service.WmsInHtReceivingOrderDetService;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by mr.lei on 2021/12/15.
 */
@RestController
@Api(tags = "wmsInHtReceivingOrderDet控制器")
@RequestMapping("/wmsInHtReceivingOrderDet")
@Validated
public class WmsInHtReceivingOrderDetController {

    @Resource
    private WmsInHtReceivingOrderDetService wmsInHtReceivingOrderDetService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WmsInHtReceivingOrderDet wmsInHtReceivingOrderDet) {
        return ControllerUtil.returnCRUD(wmsInHtReceivingOrderDetService.save(wmsInHtReceivingOrderDet));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(wmsInHtReceivingOrderDetService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=WmsInHtReceivingOrderDet.update.class) WmsInHtReceivingOrderDet wmsInHtReceivingOrderDet) {
        return ControllerUtil.returnCRUD(wmsInHtReceivingOrderDetService.update(wmsInHtReceivingOrderDet));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<WmsInHtReceivingOrderDet> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        WmsInHtReceivingOrderDet  wmsInHtReceivingOrderDet = wmsInHtReceivingOrderDetService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(wmsInHtReceivingOrderDet,StringUtils.isEmpty(wmsInHtReceivingOrderDet)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WmsInHtReceivingOrderDetDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInHtReceivingOrderDet searchWmsInHtReceivingOrderDet) {
        Page<Object> page = PageHelper.startPage(searchWmsInHtReceivingOrderDet.getStartPage(),searchWmsInHtReceivingOrderDet.getPageSize());
        List<WmsInHtReceivingOrderDetDto> list = wmsInHtReceivingOrderDetService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInHtReceivingOrderDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<WmsInHtReceivingOrderDetDto>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchWmsInHtReceivingOrderDet searchWmsInHtReceivingOrderDet) {
        List<WmsInHtReceivingOrderDetDto> list = wmsInHtReceivingOrderDetService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInHtReceivingOrderDet));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<WmsInHtReceivingOrderDet>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInHtReceivingOrderDet searchWmsInHtReceivingOrderDet) {
        Page<Object> page = PageHelper.startPage(searchWmsInHtReceivingOrderDet.getStartPage(),searchWmsInHtReceivingOrderDet.getPageSize());
        List<WmsInHtReceivingOrderDet> list = wmsInHtReceivingOrderDetService.findHtList(ControllerUtil.dynamicConditionByEntity(searchWmsInHtReceivingOrderDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchWmsInHtReceivingOrderDet searchWmsInHtReceivingOrderDet){
    List<WmsInHtReceivingOrderDetDto> list = wmsInHtReceivingOrderDetService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInHtReceivingOrderDet));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "WmsInHtReceivingOrderDet信息", WmsInHtReceivingOrderDetDto.class, "WmsInHtReceivingOrderDet.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

    @PostMapping(value = "/import")
    @ApiOperation(value = "从excel导入",notes = "从excel导入")
    public ResponseEntity importExcel(@ApiParam(value ="输入excel文件",required = true) @RequestPart(value="file") MultipartFile file){
        try {
            // 导入操作
            List<WmsInHtReceivingOrderDet> baseAddressImports = EasyPoiUtils.importExcel(file, 0, 1, WmsInHtReceivingOrderDet.class);
            Map<String, Object> resultMap = wmsInHtReceivingOrderDetService.importExcel(baseAddressImports);
            return ControllerUtil.returnDataSuccess("操作结果集",resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.OPT20012002.getCode());
        }
    }
}
