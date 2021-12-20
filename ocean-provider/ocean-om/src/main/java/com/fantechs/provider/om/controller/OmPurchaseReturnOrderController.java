package com.fantechs.provider.om.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.om.OmPurchaseReturnOrderDto;
import com.fantechs.common.base.general.entity.om.OmHtPurchaseReturnOrder;
import com.fantechs.common.base.general.entity.om.OmPurchaseReturnOrder;
import com.fantechs.common.base.general.entity.om.search.SearchOmPurchaseReturnOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.om.service.OmPurchaseReturnOrderService;
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
 * Created by leifengzhi on 2021/12/20.
 */
@RestController
@Api(tags = "采退订单")
@RequestMapping("/omPurchaseReturnOrder")
@Validated
@Slf4j
public class OmPurchaseReturnOrderController {

    @Resource
    private OmPurchaseReturnOrderService omPurchaseReturnOrderService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated OmPurchaseReturnOrderDto omPurchaseReturnOrderDto) {
        return ControllerUtil.returnCRUD(omPurchaseReturnOrderService.save(omPurchaseReturnOrderDto));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(omPurchaseReturnOrderService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=OmPurchaseReturnOrder.update.class) OmPurchaseReturnOrderDto omPurchaseReturnOrderDto) {
        return ControllerUtil.returnCRUD(omPurchaseReturnOrderService.update(omPurchaseReturnOrderDto));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<OmPurchaseReturnOrder> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        OmPurchaseReturnOrder  omPurchaseReturnOrder = omPurchaseReturnOrderService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(omPurchaseReturnOrder,StringUtils.isEmpty(omPurchaseReturnOrder)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<OmPurchaseReturnOrderDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchOmPurchaseReturnOrder searchOmPurchaseReturnOrder) {
        Page<Object> page = PageHelper.startPage(searchOmPurchaseReturnOrder.getStartPage(),searchOmPurchaseReturnOrder.getPageSize());
        List<OmPurchaseReturnOrderDto> list = omPurchaseReturnOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchOmPurchaseReturnOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<OmPurchaseReturnOrderDto>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchOmPurchaseReturnOrder searchOmPurchaseReturnOrder) {
        List<OmPurchaseReturnOrderDto> list = omPurchaseReturnOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchOmPurchaseReturnOrder));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<OmHtPurchaseReturnOrder>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchOmPurchaseReturnOrder searchOmPurchaseReturnOrder) {
        Page<Object> page = PageHelper.startPage(searchOmPurchaseReturnOrder.getStartPage(),searchOmPurchaseReturnOrder.getPageSize());
        List<OmHtPurchaseReturnOrder> list = omPurchaseReturnOrderService.findHtList(ControllerUtil.dynamicConditionByEntity(searchOmPurchaseReturnOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchOmPurchaseReturnOrder searchOmPurchaseReturnOrder){
    List<OmPurchaseReturnOrderDto> list = omPurchaseReturnOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchOmPurchaseReturnOrder));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "采退订单", OmPurchaseReturnOrderDto.class, "采退订单.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

    @PostMapping(value = "/import")
    @ApiOperation(value = "从excel导入",notes = "从excel导入")
    public ResponseEntity importExcel(@ApiParam(value ="输入excel文件",required = true) @RequestPart(value="file") MultipartFile file){
        try {
            // 导入操作
            List<OmPurchaseReturnOrder> baseAddressImports = EasyPoiUtils.importExcel(file, 0, 1, OmPurchaseReturnOrder.class);
            Map<String, Object> resultMap = omPurchaseReturnOrderService.importExcel(baseAddressImports);
            return ControllerUtil.returnDataSuccess("操作结果集",resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.OPT20012002.getCode());
        }
    }
}
