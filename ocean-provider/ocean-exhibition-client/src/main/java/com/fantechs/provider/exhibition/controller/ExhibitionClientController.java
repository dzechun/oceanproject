package com.fantechs.provider.exhibition.controller;

import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.exhibition.service.ExhibitionClientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "展厅客户端")
public class ExhibitionClientController {

    @Autowired
    private ExhibitionClientService exhibitionClientService;

    /**
     * 订单生产
     * @throws Exception
     */
    @PostMapping(value="/makingOrders")
    @ApiOperation(value = "订单生产",notes = "订单生产")
    public ResponseEntity makingOrders() {
        return ControllerUtil.returnCRUD(exhibitionClientService.makingOrders());
    }
}
