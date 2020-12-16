package com.fantechs.provider.imes.storage.controller;

import com.fantechs.common.base.dto.storage.SmtStorageInventoryDetDto;
import com.fantechs.common.base.entity.storage.SmtStorageInventoryDet;
import com.fantechs.common.base.entity.basic.search.SearchSmtStorageInventoryDet;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.storage.service.SmtStorageInventoryDetService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2020/12/04.
 */
@RestController
@Api(tags = "储位库存明细")
@RequestMapping("/smtStorageInventoryDet")
@Validated
public class SmtStorageInventoryDetController {

    @Autowired
    private SmtStorageInventoryDetService smtStorageInventoryDetService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated SmtStorageInventoryDet smtStorageInventoryDet) {
        return ControllerUtil.returnCRUD(smtStorageInventoryDetService.save(smtStorageInventoryDet));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(smtStorageInventoryDetService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=SmtStorageInventoryDet.update.class) SmtStorageInventoryDet smtStorageInventoryDet) {
        return ControllerUtil.returnCRUD(smtStorageInventoryDetService.update(smtStorageInventoryDet));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SmtStorageInventoryDet> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        SmtStorageInventoryDet  smtStorageInventoryDet = smtStorageInventoryDetService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(smtStorageInventoryDet,StringUtils.isEmpty(smtStorageInventoryDet)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SmtStorageInventoryDetDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSmtStorageInventoryDet searchSmtStorageInventoryDet) {
        Page<Object> page = PageHelper.startPage(searchSmtStorageInventoryDet.getStartPage(),searchSmtStorageInventoryDet.getPageSize());
        List<SmtStorageInventoryDetDto> list = smtStorageInventoryDetService.findList(ControllerUtil.dynamicConditionByEntity(searchSmtStorageInventoryDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchSmtStorageInventoryDet searchSmtStorageInventoryDet){
    List<SmtStorageInventoryDetDto> list = smtStorageInventoryDetService.findList(ControllerUtil.dynamicConditionByEntity(searchSmtStorageInventoryDet));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "SmtStorageInventoryDet信息", SmtStorageInventoryDet.class, "SmtStorageInventoryDet.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
