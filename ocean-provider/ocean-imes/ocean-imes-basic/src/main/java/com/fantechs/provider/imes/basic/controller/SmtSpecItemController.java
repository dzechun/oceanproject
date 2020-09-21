package com.fantechs.provider.imes.basic.controller;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.basic.SmtSpecItemExcelDTO;
import com.fantechs.common.base.entity.basic.SmtSpecItem;
import com.fantechs.common.base.entity.basic.history.SmtHtSpecItem;
import com.fantechs.common.base.entity.basic.search.SearchSmtSpecItem;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.basic.service.SmtHtSpecItemService;
import com.fantechs.provider.imes.basic.service.SmtSpecItemService;
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
 * @Auther: wcz
 * @Date: 2020/8/19 13:50
 * @Description:
 * @Version: 1.0
 */
@RestController
@RequestMapping(value = "/smtSpecItem")
@Api(tags = "程序配置项")
@Slf4j
public class SmtSpecItemController {
    @Autowired
    private SmtSpecItemService smtSpecItemService;
    @Autowired
    private SmtHtSpecItemService smtHtSpecItemService;

    @ApiOperation("根据条件查询程序配置项列表")
    @PostMapping("/selectSpecItems")
    public ResponseEntity<List<SmtSpecItem>> list(
            @ApiParam(value = "查询条件，请参考Model说明")@RequestBody(required = false) SearchSmtSpecItem searchSmtSpecItem){
        Page<Object> page = PageHelper.startPage(searchSmtSpecItem.getStartPage(),searchSmtSpecItem.getPageSize());
        List<SmtSpecItem> smtSpecItems = smtSpecItemService.selectSpecItems(searchSmtSpecItem);
        return ControllerUtil.returnDataSuccess(smtSpecItems,(int)page.getTotal());
    }

    @GetMapping("/selectSpecItemById")
    @ApiOperation(value = "通过ID获取单个程序配置项信息",notes = "通过ID获取单个程序配置项信息")
    public ResponseEntity<SmtSpecItem> selectSpecItemById(@ApiParam(value = "传入主键specId",required = true) @RequestParam Long specId) {
        if(StringUtils.isEmpty(specId)){
            return ControllerUtil.returnFail("缺少必需参数", ErrorCodeEnum.GL99990100.getCode());
        }
        SmtSpecItem smtSpecItem=smtSpecItemService.selectById(specId);
        return ControllerUtil.returnDataSuccess(smtSpecItem,StringUtils.isEmpty(smtSpecItem)?0:1);
    }


    @ApiOperation("增加程序配置项")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：specCode、specName,para",required = true)@RequestBody SmtSpecItem smtSpecItem){
        if(StringUtils.isEmpty(
                smtSpecItem.getSpecCode(),
                smtSpecItem.getSpecName(),
                smtSpecItem.getPara())){
            return ControllerUtil.returnFailByParameError();
        }
        return ControllerUtil.returnCRUD(smtSpecItemService.insert(smtSpecItem));
    }

    @ApiOperation("修改程序配置项")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "程序配置项对象，程序配置项Id必传",required = true)@RequestBody SmtSpecItem smtSpecItem){
        if(StringUtils.isEmpty(smtSpecItem.getSpecId())){
            return ControllerUtil.returnFailByParameError();
        }
        return ControllerUtil.returnCRUD(smtSpecItemService.updateById(smtSpecItem));
    }

    @ApiOperation("删除程序配置项")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "程序配置项对象ID",required = true)@RequestBody List<String> specIds){
        if(StringUtils.isEmpty(specIds)){
            return ControllerUtil.returnFailByParameError();
        }
        return ControllerUtil.returnCRUD(smtSpecItemService.deleteByIds(specIds));
    }

    /**
     * 导出数据
     * @return
     * @throws
     */
    @PostMapping(value = "/exportSpecItems")
    @ApiOperation(value = "导出程序配置项信息excel",notes = "导出程序配置项信息excel")
    public void exportSpecItems(HttpServletResponse response, @ApiParam(value ="输入查询条件",required = false)
                            @RequestBody(required = false) SearchSmtSpecItem searchSmtSpecItem){
        List<SmtSpecItemExcelDTO> list = smtSpecItemService.exportSpecItems(searchSmtSpecItem);
        try {
            // 导出操作
            EasyPoiUtils.exportExcel(list, "程序配置项信息导出", "程序配置项信息", SmtSpecItemExcelDTO.class, "程序配置项信息.xls", response);
        } catch (Exception e) {
            throw new BizErrorException(e);
        }
    }

    @PostMapping("/selectHtSpecItems")
    @ApiOperation(value = "根据条件查询程序配置项履历信息",notes = "根据条件查询程序配置项履历信息")
    public ResponseEntity<List<SmtHtSpecItem>> selectHtSpecItems(@RequestBody(required = false) SearchSmtSpecItem searchSmtSpecItem) {
        Page<Object> page = PageHelper.startPage(searchSmtSpecItem.getStartPage(),searchSmtSpecItem.getPageSize());
        List<SmtHtSpecItem> smtHtSpecItems = smtHtSpecItemService.findHtSpecItemList(ControllerUtil.dynamicConditionByEntity(searchSmtSpecItem));
        return  ControllerUtil.returnDataSuccess(smtHtSpecItems, (int)page.getTotal());
    }
}

