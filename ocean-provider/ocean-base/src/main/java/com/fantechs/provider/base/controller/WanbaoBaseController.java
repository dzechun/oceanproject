package com.fantechs.provider.base.controller;

import com.fantechs.common.base.general.dto.basic.BaseBarcodeRuleSetDto;
import com.fantechs.common.base.general.dto.basic.BaseMaterialDto;
import com.fantechs.common.base.general.dto.basic.WanbaoBaseBySyncOrderDto;
import com.fantechs.common.base.general.entity.basic.BaseProLine;
import com.fantechs.common.base.general.entity.basic.BaseRoute;
import com.fantechs.common.base.general.entity.basic.BaseRouteProcess;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProLine;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.base.service.*;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * @Auther: bgkun
 * @Date: 2022-02-21
 */
@RestController
@RequestMapping(value = "/wanbaoBase")
@Api(tags = "万宝同步定时任务查询基础数据汇总控制器")
@Slf4j
@Validated
public class WanbaoBaseController {

    @Resource
    BaseProLineService proLineService;
    @Resource
    BaseMaterialService materialService;
    @Resource
    BaseRouteService routeService;
    @Resource
    BaseBarcodeRuleSetService barcodeRuleSetService;
    @Resource
    BaseRouteProcessService routeProcessService;

    @ApiOperation("同步工单查询基础数据汇总")
    @PostMapping("/findBySyncOrder")
    public ResponseEntity<WanbaoBaseBySyncOrderDto> findBySyncOrder(){
        List<BaseProLine> proLineList = proLineService.findList(new HashMap<>());
        List<BaseMaterialDto> materialDtoList = materialService.findAll(new HashMap<>());
        List<BaseRoute> routeList = routeService.findList(new HashMap<>());
        List<BaseBarcodeRuleSetDto> barcodeRuleSetDtoList = barcodeRuleSetService.findList(new HashMap<>());
        List<BaseRouteProcess> routeProcessList = routeProcessService.findList(new HashMap<>());

        WanbaoBaseBySyncOrderDto dto = new WanbaoBaseBySyncOrderDto();
        dto.setMaterialDtoList(materialDtoList);
        dto.setBarcodeRuleSetDtoList(barcodeRuleSetDtoList);
        dto.setProLineList(proLineList);
        dto.setRouteList(routeList);
        dto.setRouteProcessList(routeProcessList);
        return ControllerUtil.returnDataSuccess(dto, 1);
    }
}
