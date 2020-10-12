package com.fantechs.provider.imes.basic.controller;

import com.fantechs.common.base.entity.basic.SmtProductBomDet;
import com.fantechs.common.base.entity.basic.search.SearchSmtProductBomDet;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.basic.service.SmtHtProductBomDetService;
import com.fantechs.provider.imes.basic.service.SmtProductBomDetService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 *
 * Created by wcz on 2020/10/12.
 */
@RestController
@Api(tags = "产品BOM祥细信息")
@RequestMapping("/smtProductBomDet")
public class SmtProductBomDetController {

    @Autowired
    private SmtProductBomDetService smtProductBomDetService;
    @Autowired
    private SmtHtProductBomDetService smtHtProductBomDetService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody SmtProductBomDet smtProductBomDet) {
        return ControllerUtil.returnCRUD(smtProductBomDetService.save(smtProductBomDet));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam String ids) {
        if(StringUtils.isEmpty(ids)){
            return ControllerUtil.returnFailByParameError();
        }
        return ControllerUtil.returnCRUD(smtProductBomDetService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody SmtProductBomDet smtProductBomDet) {
        if(StringUtils.isEmpty(smtProductBomDet.getProductBomDetId())){
            return ControllerUtil.returnFailByParameError();
        }
        return ControllerUtil.returnCRUD(smtProductBomDetService.update(smtProductBomDet));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SmtProductBomDet> detail(@ApiParam(value = "ID",required = true)@RequestParam Long id) {
        if(StringUtils.isEmpty(id)){
            return ControllerUtil.returnFailByParameError();
        }
        SmtProductBomDet  smtProductBomDet = smtProductBomDetService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(smtProductBomDet,StringUtils.isEmpty(smtProductBomDet)?0:1);
    }

    @ApiOperation("产品BOM祥细信息列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SmtProductBomDet>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSmtProductBomDet searchSmtProductBomDet) {
        Page<Object> page = PageHelper.startPage(searchSmtProductBomDet.getStartPage(),searchSmtProductBomDet.getPageSize());
        List<SmtProductBomDet> list = smtProductBomDetService.findList(searchSmtProductBomDet);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("产品BOM祥细信息历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<SmtProductBomDet>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchSmtProductBomDet searchSmtProductBomDet) {
        Page<Object> page = PageHelper.startPage(searchSmtProductBomDet.getStartPage(),searchSmtProductBomDet.getPageSize());
        List<SmtProductBomDet> list = smtHtProductBomDetService.findList(searchSmtProductBomDet);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchSmtProductBomDet searchSmtProductBomDet){
    List<SmtProductBomDet> list = smtProductBomDetService.findList(searchSmtProductBomDet);
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出产品BOM祥细信息", "产品BOM祥细信息", SmtProductBomDet.class, "产品BOM祥细信息.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
