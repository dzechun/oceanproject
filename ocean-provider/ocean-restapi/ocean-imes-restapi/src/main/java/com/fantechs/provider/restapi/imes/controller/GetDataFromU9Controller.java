package com.fantechs.provider.restapi.imes.controller;

import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.restapi.imes.service.impl.GetDataFromU9ServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/U9API")
@Api(tags = "同步U9数据")
public class GetDataFromU9Controller {

    @Resource
    private GetDataFromU9ServiceImpl getDataFromU9Service;

    @GetMapping("/updateMaterialFromU9")
    @ApiOperation(value = "同步U9物料数据", notes = "同步U9物料数据")
    public ResponseEntity updateMaterial() throws Exception {
        return ControllerUtil.returnCRUD(getDataFromU9Service.updateMaterial());
    }

    @GetMapping("/updateWarehouseFromU9")
    @ApiOperation(value = "同步U9仓库数据", notes = "同步U9仓库数据")
    public ResponseEntity updateWarehouse() throws Exception {

        int i = getDataFromU9Service.updateWarehouse();
        if (i==500){
            return ControllerUtil.returnFail("正在同步更新物料数据，请勿重复操作",500);
        }
        if (i==1){
            return ControllerUtil.returnSuccess("同步完成");
        }else {
            return ControllerUtil.returnFail("同步失败", -1);
        }
    }
}
