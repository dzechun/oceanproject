package com.fantechs.provider.imes.basic.controller;


import com.fantechs.common.base.dto.basic.SmtWorkShopDto;
import com.fantechs.common.base.entity.basic.SmtWorkShop;
import com.fantechs.common.base.entity.basic.history.SmtHtWorkShop;
import com.fantechs.common.base.entity.basic.search.SearchSmtWorkShop;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.basic.service.SmtHtWorkShopService;
import com.fantechs.provider.imes.basic.service.SmtWorkShopService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by lfz on 2020/9/1.
 */
@RestController
@RequestMapping(value = "/smtWorkShop")
@Api(tags = "车间管理")
@Slf4j
public class SmtWorkShopController {
    @Autowired
    private SmtWorkShopService smtWorkShopService;

    @Autowired
    private SmtHtWorkShopService smtHtWorkShopService;

    @ApiOperation("根据条件查询角色信息列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SmtWorkShopDto>> findList(
            @ApiParam(value = "查询对象")@RequestBody SearchSmtWorkShop searchSmtWorkShop
    ){
        Page<Object> page = PageHelper.startPage(searchSmtWorkShop.getStartPage(),searchSmtWorkShop.getPageSize());
        List<SmtWorkShopDto> smtFactoryDtos = smtWorkShopService.findList(ControllerUtil.dynamicConditionByEntity(searchSmtWorkShop));
        return ControllerUtil.returnDataSuccess(smtFactoryDtos,(int)page.getTotal());
    }

    @ApiOperation("增加车间信息")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：workShopCode、workShopName、factoryId",required = true)@RequestBody SmtWorkShop smtWorkShop){
        if(StringUtils.isEmpty(
                smtWorkShop.getWorkShopCode(),
                smtWorkShop.getWorkShopName(),
                smtWorkShop.getFactoryId())){
            return ControllerUtil.returnFailByParameError();
        }
        return ControllerUtil.returnCRUD(smtWorkShopService.insert(smtWorkShop));

    }

    @ApiOperation(value = "获取菜单列表",notes = "返回数据包含菜单对应的角色权限")
    @PostMapping("/findHtList")
    public ResponseEntity< List<SmtHtWorkShop>> findHtList(
            @ApiParam(value = "查询对象")@RequestBody SearchSmtWorkShop searchSmtWorkShop){
        Page<Object> page = PageHelper.startPage(searchSmtWorkShop.getStartPage(),searchSmtWorkShop.getPageSize());
        List<SmtHtWorkShop> menuList = smtHtWorkShopService.findList(ControllerUtil.dynamicConditionByEntity(searchSmtWorkShop));
        return ControllerUtil.returnDataSuccess(menuList,(int)page.getTotal());
    }

    @ApiOperation("修改车间信息")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "车间信息对象，workShopId、workShopCode、workShopName、factoryId必传",required = true)@RequestBody SmtWorkShop smtWorkShop){
        if(StringUtils.isEmpty(smtWorkShop.getWorkShopId(),
                smtWorkShop.getWorkShopCode(),
                smtWorkShop.getWorkShopName(),
                smtWorkShop.getFactoryId()
        )){
            return ControllerUtil.returnFailByParameError();
        }
        return ControllerUtil.returnCRUD(smtWorkShopService.update(smtWorkShop));

    }

    @ApiOperation("删除车间信息")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "车间对象ID",required = true) @RequestBody List<String> workShopIds){
        if(StringUtils.isEmpty(workShopIds)){
            return ControllerUtil.returnFailByParameError();
        }
        return ControllerUtil.returnCRUD(smtWorkShopService.deleteByIds(workShopIds));
    }

    @ApiOperation("获取车间详情")
    @PostMapping("/detail")
    public ResponseEntity<SmtWorkShop> detail(@ApiParam(value = "车间ID",required = true)@RequestParam String workShopId){
        if(StringUtils.isEmpty(workShopId)){
            return ControllerUtil.returnFailByParameError();
        }
        SmtWorkShop smtWorkShop = smtWorkShopService.selectByKey(workShopId);
        return  ControllerUtil.returnDataSuccess(smtWorkShop,StringUtils.isEmpty(smtWorkShop)?0:1);
    }


    /**
     * 导出数据
     * @return
     * @throws
     */
    @PostMapping(value = "/export")
    @ApiOperation(value = "导出车间excel",notes = "导出车间excel")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")@RequestBody SearchSmtWorkShop searchSmtWorkShop){
        List<SmtWorkShopDto> smtWorkShopDtos = smtWorkShopService.findList(ControllerUtil.dynamicConditionByEntity(searchSmtWorkShop));
        try {
            // 导出操作
            EasyPoiUtils.exportExcel(smtWorkShopDtos, "导出车间信息", "车间信息", SmtWorkShopDto.class, "车间信息.xls", response);
        } catch (Exception e) {
            throw new BizErrorException(e);
        }
    }
}
