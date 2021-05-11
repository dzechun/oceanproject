package com.fantechs.provider.wms.inner.controller;

import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrderDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.wms.inner.service.PickingOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 拣货作业
 * @Author mr.lei
 * @Date 2021/5/10
 */
@RestController
@Api(tags = "拣货作业")
@RequestMapping("/pickingOrder")
@Validated
public class PickingOrderController {

    @Resource
    private PickingOrderService pickingOrderService;

    @ApiOperation("自动分配")
    @PostMapping("/autoDistribution")
    public ResponseEntity autoDistribution(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids){
        return ControllerUtil.returnCRUD(pickingOrderService.autoDistribution(ids));
    }

    @ApiOperation("手动分配")
    @PostMapping("/handDistribution")
    public ResponseEntity handDistribution(@RequestBody List<WmsInnerJobOrderDet> list){
        return ControllerUtil.returnCRUD(pickingOrderService.handDistribution(list));
    }

    @ApiOperation("取消分配")
    @PostMapping("/cancelDistribution")
    public ResponseEntity cancelDistribution(@RequestParam String ids){
        return ControllerUtil.returnCRUD(pickingOrderService.cancelDistribution(ids));
    }

    @ApiOperation("整单确认")
    @PostMapping("/allReceiving")
    public ResponseEntity allReceiving(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids){
        return ControllerUtil.returnCRUD(pickingOrderService.allReceiving(ids));
    }

    @ApiOperation("单一确认")
    @PostMapping("/singleReceiving")
    public ResponseEntity singleReceiving(@RequestBody(required = true) List<WmsInnerJobOrderDet> wmsInPutawayOrderDets){
        return ControllerUtil.returnCRUD(pickingOrderService.singleReceiving(wmsInPutawayOrderDets));
    }
}
