package com.fantechs.provider.imes.basic.controller;


import com.fantechs.common.base.dto.basic.SmtFactoryDto;
import com.fantechs.common.base.entity.basic.SmtFactory;
import com.fantechs.common.base.entity.basic.history.SmtHtFactory;
import com.fantechs.common.base.entity.basic.search.SearchSmtFactory;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.basic.service.SmtFactoryService;
import com.fantechs.provider.imes.basic.service.SmtHtFactoryService;
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
 * @Auther: leifengzhi
 * @Date: 2020/9/1
 * @Description:
 * @Version: 1.0
 */
@RestController
@RequestMapping(value = "/smtFactory")
@Api(tags = "厂别管理")
@Slf4j
public class SmtFactoryController {
    @Autowired
    private SmtFactoryService smtFactoryService;

    @Autowired
    private SmtHtFactoryService smtHtFactoryService;

    @ApiOperation("根据条件查询角色信息列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SmtFactoryDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSmtFactory searchSmtFactory ){
        Page<Object> page = PageHelper.startPage(searchSmtFactory.getStartPage(),searchSmtFactory.getPageSize());
        List<SmtFactoryDto> smtFactoryDtos = smtFactoryService.findList(ControllerUtil.dynamicConditionByEntity(searchSmtFactory));
        return ControllerUtil.returnDataSuccess(smtFactoryDtos,(int)page.getTotal());
    }

    @ApiOperation("增加厂别信息")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：factoryCode、factoryName",required = true)@RequestBody SmtFactory smtFactory){
        if(StringUtils.isEmpty(
                smtFactory.getFactoryCode(),
                smtFactory.getFactoryName())){
            return ControllerUtil.returnFailByParameError();
        }
        return ControllerUtil.returnCRUD(smtFactoryService.insert(smtFactory));

    }

    @ApiOperation(value = "获取工厂列表",notes = "获取工厂列表")
    @PostMapping("/findHtList")
    public ResponseEntity< List<SmtHtFactory>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchSmtFactory searchSmtFactory){
        Page<Object> page = PageHelper.startPage(searchSmtFactory.getStartPage(),searchSmtFactory.getPageSize());
        List<SmtHtFactory> menuList = smtHtFactoryService.findList(ControllerUtil.dynamicConditionByEntity(searchSmtFactory));
        return ControllerUtil.returnDataSuccess(menuList,(int)page.getTotal());
    }

    @ApiOperation("修改厂别信息")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "厂别信息对象，厂别信息Id必传",required = true)@RequestBody SmtFactory smtFactory){
        if(StringUtils.isEmpty(smtFactory.getFactoryId(),
                smtFactory.getFactoryCode(),
                smtFactory.getFactoryName()
                )){
            return ControllerUtil.returnFailByParameError();
        }
            return ControllerUtil.returnCRUD(smtFactoryService.updateById(smtFactory));

    }

    @ApiOperation("删除厂别信息")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "厂别对象ID",required = true) @RequestBody List<Long> factoryIds){
        if(StringUtils.isEmpty(factoryIds)){
            return ControllerUtil.returnFailByParameError();
        }
        return ControllerUtil.returnCRUD(smtFactoryService.deleteByIds(factoryIds));
    }

    @ApiOperation("获取厂别详情")
    @PostMapping("/detail")
    public ResponseEntity<SmtFactory> detail(@ApiParam(value = "工厂ID",required = true)@RequestParam Long factoryId){
        if(StringUtils.isEmpty(factoryId)){
            return ControllerUtil.returnFailByParameError();
        }
        SmtFactory smtFactory = smtFactoryService.findById(factoryId);
        return  ControllerUtil.returnDataSuccess(smtFactory,StringUtils.isEmpty(smtFactory)?0:1);
    }


    /**
     * 导出数据
     * @return
     * @throws
     */
    @PostMapping(value = "/export")
    @ApiOperation(value = "导出工厂excel",notes = "导出工厂excel")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")@RequestBody SearchSmtFactory searchSmtFactory){
        List<SmtFactoryDto> smtFactoryDtos = smtFactoryService.findList(ControllerUtil.dynamicConditionByEntity(searchSmtFactory));
        try {
            // 导出操作
            EasyPoiUtils.exportExcel(smtFactoryDtos, "导出工厂信息", "工厂信息", SmtFactoryDto.class, "工厂信息.xls", response);
        } catch (Exception e) {
            throw new BizErrorException(e);
        }
    }

}
