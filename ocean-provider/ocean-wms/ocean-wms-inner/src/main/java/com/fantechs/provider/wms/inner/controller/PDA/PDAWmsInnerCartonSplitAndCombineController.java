package com.fantechs.provider.wms.inner.controller.PDA;

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
@Api(tags = "PDA分合包箱作业")
@RequestMapping("/PDAWmsInnerCartonSplitAndCombine")
@Validated
public class PDAWmsInnerCartonSplitAndCombineController {

    @Resource
    private WmsInnerDirectTransferOrderService wmsInnerDirectTransferOrderService;

    @ApiOperation(value = "PDA新增",notes = "PDA新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated List<PDAWmsInnerDirectTransferOrderDto> pdaWmsInnerDirectTransferOrderDtos) {
        return ControllerUtil.returnCRUD(wmsInnerDirectTransferOrderService.save(pdaWmsInnerDirectTransferOrderDtos));
    }

}
