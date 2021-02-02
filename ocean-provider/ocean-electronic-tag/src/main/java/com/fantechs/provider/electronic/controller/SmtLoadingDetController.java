package com.fantechs.provider.electronic.controller;

import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.electronic.dto.SmtLoadingDetDto;
import com.fantechs.common.base.electronic.entity.SmtLoadingDet;
import com.fantechs.common.base.electronic.entity.search.SearchSmtLoadingDet;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.electronic.service.SmtLoadingDetService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/01/09.
 */
@RestController
@Api(tags = "捡料上架物料明细控制器")
@RequestMapping("/smtLoadingDet")
@Validated
public class SmtLoadingDetController {

    @Autowired
    private SmtLoadingDetService smtLoadingDetService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    @Transactional
    @LcnTransaction
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated SmtLoadingDet smtLoadingDet) {
        return ControllerUtil.returnCRUD(smtLoadingDetService.save(smtLoadingDet));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(smtLoadingDetService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    @Transactional
    @LcnTransaction
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=SmtLoadingDet.update.class) SmtLoadingDet smtLoadingDet) {
        return ControllerUtil.returnCRUD(smtLoadingDetService.update(smtLoadingDet));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SmtLoadingDet> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        SmtLoadingDet  smtLoadingDet = smtLoadingDetService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(smtLoadingDet,StringUtils.isEmpty(smtLoadingDet)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SmtLoadingDetDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSmtLoadingDet searchSmtLoadingDet) {
        Page<Object> page = PageHelper.startPage(searchSmtLoadingDet.getStartPage(),searchSmtLoadingDet.getPageSize());
        List<SmtLoadingDetDto> list = smtLoadingDetService.findList(ControllerUtil.dynamicConditionByEntity(searchSmtLoadingDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

//    @ApiOperation("历史列表")
//    @PostMapping("/findHtList")
//    public ResponseEntity<List<SmtLoadingDetDto>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchSmtLoadingDet searchSmtLoadingDet) {
//        Page<Object> page = PageHelper.startPage(searchSmtLoadingDet.getStartPage(),searchSmtLoadingDet.getPageSize());
//        List<SmtLoadingDetDto> list = smtLoadingDetService.findList(ControllerUtil.dynamicConditionByEntity(searchSmtLoadingDet));
//        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
//    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchSmtLoadingDet searchSmtLoadingDet){
    List<SmtLoadingDetDto> list = smtLoadingDetService.findList(ControllerUtil.dynamicConditionByEntity(searchSmtLoadingDet));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "捡料上架明细", "捡料上架明细信息", SmtLoadingDetDto.class, "捡料上架明细.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
