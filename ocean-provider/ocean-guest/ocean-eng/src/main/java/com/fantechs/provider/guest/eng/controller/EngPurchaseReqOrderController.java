package com.fantechs.provider.guest.eng.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eng.EngPurchaseReqOrderDto;
import com.fantechs.common.base.general.entity.eng.EngContractQtyOrder;
import com.fantechs.common.base.general.entity.eng.EngPurchaseReqOrder;
import com.fantechs.common.base.general.entity.eng.search.SearchEngPurchaseReqOrder;
import com.fantechs.provider.guest.eng.service.EngPurchaseReqOrderService;
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
 * Created by leifengzhi on 2021/09/02.
 */
@RestController
@Api(tags = "请购单信息")
@RequestMapping("/engPurchaseReqOrder")
@Validated
public class EngPurchaseReqOrderController {

    @Resource
    private EngPurchaseReqOrderService engPurchaseReqOrderService;

//    @ApiOperation(value = "新增",notes = "新增")
//    @PostMapping("/add")
//    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated EngPurchaseReqOrder engPurchaseReqOrder) {
//        return ControllerUtil.returnCRUD(engPurchaseReqOrderService.save(engPurchaseReqOrder));
//    }

//    @ApiOperation("删除")
//    @PostMapping("/delete")
//    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
//        return ControllerUtil.returnCRUD(engPurchaseReqOrderService.batchDelete(ids));
//    }

//    @ApiOperation("修改")
////    @PostMapping("/update")
////    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=EngPurchaseReqOrder.update.class) EngPurchaseReqOrder engPurchaseReqOrder) {
////        return ControllerUtil.returnCRUD(engPurchaseReqOrderService.update(engPurchaseReqOrder));
////    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<EngPurchaseReqOrder> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        EngPurchaseReqOrder  engPurchaseReqOrder = engPurchaseReqOrderService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(engPurchaseReqOrder,StringUtils.isEmpty(engPurchaseReqOrder)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<EngPurchaseReqOrderDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchEngPurchaseReqOrder searchEngPurchaseReqOrder) {
        Page<Object> page = PageHelper.startPage(searchEngPurchaseReqOrder.getStartPage(),searchEngPurchaseReqOrder.getPageSize());
        List<EngPurchaseReqOrderDto> list = engPurchaseReqOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchEngPurchaseReqOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchEngPurchaseReqOrder searchEngPurchaseReqOrder){
    List<EngPurchaseReqOrderDto> list = engPurchaseReqOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchEngPurchaseReqOrder));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "EngPurchaseReqOrder信息", EngPurchaseReqOrderDto.class, "EngPurchaseReqOrder.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

    @ApiOperation(value = "接口新增或更新",notes = "接口新增或更新")
    @PostMapping("/saveByApi")
    public ResponseEntity saveByApi(@ApiParam(value = "必传：purchaseReqOrderCode、materialCode",required = true)@RequestBody @Validated EngPurchaseReqOrder engPurchaseReqOrder) {
        return ControllerUtil.returnCRUD(engPurchaseReqOrderService.saveByApi(engPurchaseReqOrder));
    }
}
