package com.fantechs.provider.leisai.api.controller;

import com.fantechs.common.base.general.entity.leisai.LeisaiWmsCarton;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.leisai.api.service.SyncWmsDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/leisaiSyncData")
@Api(tags = "雷赛同步数据控制器")
@Slf4j
@Validated
public class LeisaiSyncDataController {

    @Resource
    private SyncWmsDataService syncWmsDataService;

    @ApiOperation(value = "雷赛-包箱信息同步",notes = "雷赛-包箱信息同步")
    @PostMapping("/syncCartonData")
    public ResponseEntity syncCartonData(@ApiParam(value = "必传：",required = true)@RequestBody @Validated LeisaiWmsCarton leisaiWmsCarton){
        log.info("雷赛-包箱信息同步");
        syncWmsDataService.syncCartonData(leisaiWmsCarton);
        return ControllerUtil.returnSuccess();
    }

}

