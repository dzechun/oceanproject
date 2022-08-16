package com.fantechs.provider.wms.out.controller.PDA;

import com.fantechs.common.base.general.dto.wms.out.WmsOutDespatchOrderDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutDespatchOrderReJoReDetDto;
import com.fantechs.common.base.general.entity.wms.out.search.SearchWmsOutDespatchOrderReJoReDet;
import com.fantechs.common.base.general.entity.wms.out.search.SearchWmsOutDespatchOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.out.service.WmsOutDespatchOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author mr.lei
 * @Date 2021/5/10
 */
@RestController
@Api(tags = "PDA装车单")
@RequestMapping("/PDAWmsOutDespatch")
public class PDAWmsOutDespatchOrderController {

    @Resource
    private WmsOutDespatchOrderService wmsOutDespatchOrderService;

    @ApiOperation("PDA装车单列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WmsOutDespatchOrderDto>> findList(@RequestBody SearchWmsOutDespatchOrder searchWmsOutDespatchOrder){
        searchWmsOutDespatchOrder.setPda(true);
        List<WmsOutDespatchOrderDto> list = wmsOutDespatchOrderService.findList(searchWmsOutDespatchOrder);
        return ControllerUtil.returnDataSuccess(list, StringUtils.isEmpty(list)?0:1);
    }

    @ApiOperation("PDA装车单明细列表")
    @PostMapping("/findDetList")
    public ResponseEntity<List<WmsOutDespatchOrderReJoReDetDto>> findDetList(@RequestBody SearchWmsOutDespatchOrderReJoReDet searchWmsOutDespatchOrderReJoReDet){
        List<WmsOutDespatchOrderReJoReDetDto> list = wmsOutDespatchOrderService.findDetList(searchWmsOutDespatchOrderReJoReDet);
        return ControllerUtil.returnDataSuccess(list,list.size());
    }

    @ApiOperation("PDA发运")
    @PostMapping("/forwarding")
    public ResponseEntity forwarding(@ApiParam("逗号间隔") @RequestParam @NotNull(message = "id不能为空") String ids){
        return ControllerUtil.returnCRUD(wmsOutDespatchOrderService.forwarding(ids));
    }
}
