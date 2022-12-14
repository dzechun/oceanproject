package com.fantechs.controller;

import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.entity.ProLineBoardModel;
import com.fantechs.entity.search.SearchProLineBoard;
import com.fantechs.service.ProLineBoardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/proLineBoard")
@Api(tags = "雷赛-管理看板")
public class ProLineBoardController {

    @Resource
    private ProLineBoardService proLineBoardService;

    @ApiOperation("工单完工列表")
    @PostMapping("/findList")
    public ResponseEntity<ProLineBoardModel> findList(@ApiParam(value = "查询对象")@RequestBody SearchProLineBoard searchProLineBoard) {
        ProLineBoardModel model = proLineBoardService.findList(searchProLineBoard);
        return ControllerUtil.returnDataSuccess(model,1);
    }

}


