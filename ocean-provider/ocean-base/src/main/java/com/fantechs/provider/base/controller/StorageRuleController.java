package com.fantechs.provider.base.controller;

import com.fantechs.common.base.general.dto.basic.BaseStorageRule;
import com.fantechs.common.base.general.dto.basic.StorageRuleInventry;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.base.util.WanbaoStorageRule;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author mr.lei
 * @Date 2022/3/7
 */
@RestController
@Api(tags = "出入库规则")
@RequestMapping("/storageRule")
@Validated
public class StorageRuleController {


    @ApiOperation(value = "入库规则",notes = "入库规则")
    @PostMapping("/inRule")
    public ResponseEntity<Long> inRule(@ApiParam(value = "必传：",required = true)@RequestBody @Validated BaseStorageRule baseStorageRule) {
        return ControllerUtil.returnDataSuccess(WanbaoStorageRule.retInStorage(baseStorageRule),1);
    }

    @ApiOperation(value = "出库规则",notes = "出库规则")
    @PostMapping("/outRule")
    public ResponseEntity<Long> outRule(@ApiParam(value = "必传：",required = true)@RequestBody @Validated BaseStorageRule baseStorageRule) {
        return ControllerUtil.returnDataSuccess(WanbaoStorageRule.retOutStorage(baseStorageRule),1);
    }

    @ApiOperation(value = "出库规则",notes = "出库规则")
    @PostMapping("/returnOutStorage")
    public ResponseEntity<List<StorageRuleInventry>> returnOutStorage(@ApiParam(value = "必传：",required = true)@RequestBody @Validated BaseStorageRule baseStorageRule) {
        return ControllerUtil.returnDataSuccess(WanbaoStorageRule.returnOutStorage(baseStorageRule),1);
    }
}
