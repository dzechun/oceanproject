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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * Created by lfz on 2020/9/1.
 */
@RestController
@RequestMapping(value = "/smtWorkShop")
@Api(tags = "车间管理")
@Slf4j
@Validated
public class SmtWorkShopController {
    @Autowired
    private SmtWorkShopService smtWorkShopService;

    @Autowired
    private SmtHtWorkShopService smtHtWorkShopService;

    @ApiOperation("查询列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SmtWorkShopDto>> findList(
            @ApiParam(value = "查询对象")@RequestBody SearchSmtWorkShop searchSmtWorkShop
    ){
        Page<Object> page = PageHelper.startPage(searchSmtWorkShop.getStartPage(),searchSmtWorkShop.getPageSize());
        List<SmtWorkShopDto> smtFactoryDtos = smtWorkShopService.findList(ControllerUtil.dynamicConditionByEntity(searchSmtWorkShop));
        return ControllerUtil.returnDataSuccess(smtFactoryDtos,(int)page.getTotal());
    }

    @ApiOperation("新增车间")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：workShopCode、workShopName、factoryId",required = true)@RequestBody @Validated SmtWorkShop smtWorkShop){
        return ControllerUtil.returnCRUD(smtWorkShopService.save(smtWorkShop));

    }

    @ApiOperation(value = "履历列表",notes = "履历列表")
    @PostMapping("/findHtList")
    public ResponseEntity< List<SmtHtWorkShop>> findHtList(
            @ApiParam(value = "查询对象")@RequestBody SearchSmtWorkShop searchSmtWorkShop){
        Page<Object> page = PageHelper.startPage(searchSmtWorkShop.getStartPage(),searchSmtWorkShop.getPageSize());
        List<SmtHtWorkShop> menuList = smtHtWorkShopService.findList(ControllerUtil.dynamicConditionByEntity(searchSmtWorkShop));
        return ControllerUtil.returnDataSuccess(menuList,(int)page.getTotal());
    }

    @ApiOperation("修改车间")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "车间信息对象，workShopId、workShopCode、workShopName、factoryId必传",required = true)@RequestBody @Validated SmtWorkShop smtWorkShop){
        return ControllerUtil.returnCRUD(smtWorkShopService.update(smtWorkShop));

    }

    @ApiOperation("删除车间")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "车间对象ID",required = true) @RequestParam @NotBlank(message = "ids不能为空") String ids){
        return ControllerUtil.returnCRUD(smtWorkShopService.batchDelete(ids));
    }

    @ApiOperation("获取车间详情")
    @PostMapping("/detail")
    public ResponseEntity<SmtWorkShop> detail(@ApiParam(value = "车间ID",required = true)@RequestParam @NotBlank String workShopId){
        SmtWorkShop smtWorkShop = smtWorkShopService.selectByKey(workShopId);
        return  ControllerUtil.returnDataSuccess(smtWorkShop,StringUtils.isEmpty(smtWorkShop)?0:1);
    }


    /**
     * 导出数据
     * @return
     * @throws
     */
    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出车间excel")
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
