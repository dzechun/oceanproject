package com.fantechs.provider.wms.inner.controller;

import com.fantechs.common.base.general.dto.wms.inner.*;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.wms.inner.service.WanbaoPlatformService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * Created by Mr.Lei on 2022/06/27.
 */
@RestController
@Api(tags = "月装车出库")
@RequestMapping("/wanbaoPlatform")
@Validated
public class WanbaoPlatformController {

    @Resource
    private WanbaoPlatformService wanbaoPlatformService;

    @ApiOperation("PDA公共机月台列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WanbaoPlatform>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWanbaoPlatform searchWanbaoPlatform) {
        Page<Object> page = PageHelper.startPage(searchWanbaoPlatform.getStartPage(),searchWanbaoPlatform.getPageSize());
        List<WanbaoPlatform> list = wanbaoPlatformService.findList(ControllerUtil.dynamicConditionByEntity(searchWanbaoPlatform));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("公共机月台绑定拣货单")
    @PostMapping("/bindingPlatform")
    public ResponseEntity bindingPlatform(@RequestBody WanbaoPlatform wanbaoPlatform){
        return ControllerUtil.returnCRUD(wanbaoPlatformService.bindingPlatform(wanbaoPlatform));
    }

    @ApiOperation("读头扫码")
    @PostMapping("/doScan")
    public ResponseEntity doScan(@RequestBody ScanBarCodeOut scanBarCodeOut){
        return ControllerUtil.returnCRUD(wanbaoPlatformService.doScan(scanBarCodeOut));
    }

    @ApiOperation("月台出货提交")
    @PostMapping("/submit")
    public ResponseEntity submit(@ApiParam("月台id，逗号隔开")@RequestParam String ids){
        return ControllerUtil.returnCRUD(wanbaoPlatformService.submit(ids));
    }

    @ApiOperation("月台明细")
    @PostMapping("/findDetList")
    public ResponseEntity<List<WanbaoPlatformDetDto>> findDetList(@RequestBody SearchWanbaoPlatformDet searchWanbaoPlatformDet){
        Page<Object> page = PageHelper.startPage(searchWanbaoPlatformDet.getStartPage(),searchWanbaoPlatformDet.getPageSize());
        List<WanbaoPlatformDetDto> list = wanbaoPlatformService.findDetList(ControllerUtil.dynamicConditionByEntity(searchWanbaoPlatformDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("月台明细删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@RequestParam String platformDetId){
        return ControllerUtil.returnCRUD(wanbaoPlatformService.delete(platformDetId));
    }

    @ApiOperation("公共机拣货单列表")
    @PostMapping("/findJobOrderList")
    public ResponseEntity<List<WmsInnerJobOrder>> findJobOrderList() {
        List<WmsInnerJobOrder> list = wanbaoPlatformService.findJobOrderList();
        return ControllerUtil.returnDataSuccess(list,list.size());
    }
}
