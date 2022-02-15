package com.fantechs.provider.wms.inner.controller.PDA;

import com.fantechs.common.base.general.dto.wms.inner.PDAWmsInnerDirectTransferOrderDetDto;
import com.fantechs.common.base.general.dto.wms.inner.PDAWmsInnerDirectTransferOrderDto;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.wms.inner.service.WmsInnerDirectTransferOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/12/21.
 */
@RestController
@Api(tags = "PDA直接调拨单")
@RequestMapping("/PDAwmsInnerDirectTransferOrder")
@Validated
public class PDAWmsInnerDirectTransferOrderController {

    @Resource
    private WmsInnerDirectTransferOrderService wmsInnerDirectTransferOrderService;

    @ApiOperation(value = "PDA新增",notes = "PDA新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated List<PDAWmsInnerDirectTransferOrderDto> pdaWmsInnerDirectTransferOrderDtos) {
        return ControllerUtil.returnCRUD(wmsInnerDirectTransferOrderService.save(pdaWmsInnerDirectTransferOrderDtos));
    }

    @ApiOperation(value = "PDA校验",notes = "PDA校验")
    @PostMapping("/check")
    public ResponseEntity check(@ApiParam(value = "必传：",required = true)@RequestBody @Validated List<PDAWmsInnerDirectTransferOrderDetDto> pdaWmsInnerDirectTransferOrderDetDtos) {
        return ControllerUtil.returnCRUD(wmsInnerDirectTransferOrderService.check(pdaWmsInnerDirectTransferOrderDetDtos));
    }

/*    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(wmsInnerDirectTransferOrderService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=WmsInnerDirectTransferOrder.update.class) WmsInnerDirectTransferOrder wmsInnerDirectTransferOrder) {
        return ControllerUtil.returnCRUD(wmsInnerDirectTransferOrderService.update(wmsInnerDirectTransferOrder));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<WmsInnerDirectTransferOrder> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        WmsInnerDirectTransferOrder  wmsInnerDirectTransferOrder = wmsInnerDirectTransferOrderService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(wmsInnerDirectTransferOrder,StringUtils.isEmpty(wmsInnerDirectTransferOrder)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WmsInnerDirectTransferOrderDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInnerDirectTransferOrder searchWmsInnerDirectTransferOrder) {
        Page<Object> page = PageHelper.startPage(searchWmsInnerDirectTransferOrder.getStartPage(),searchWmsInnerDirectTransferOrder.getPageSize());
        List<WmsInnerDirectTransferOrderDto> list = wmsInnerDirectTransferOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInnerDirectTransferOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }*/

}
