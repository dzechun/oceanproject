package com.fantechs.provider.imes.storage.controller;

import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.storage.SmtStorageInventoryDto;
import com.fantechs.common.base.entity.storage.SmtStorageInventory;
import com.fantechs.common.base.entity.basic.search.SearchSmtStorageInventory;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.storage.service.SmtStorageInventoryService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2020/12/02.
 */
@RestController
@Api(tags = "储位库存")
@RequestMapping("/smtStorageInventory")
@Validated
public class SmtStorageInventoryController {

    @Resource
    private SmtStorageInventoryService smtStorageInventoryService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    @Transactional
    @LcnTransaction
    public ResponseEntity<SmtStorageInventory> add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated SmtStorageInventory smtStorageInventory) {
        smtStorageInventoryService.save(smtStorageInventory);
        return ControllerUtil.returnDataSuccess(smtStorageInventory, StringUtils.isEmpty(smtStorageInventory)?0:1);
    }

    @ApiOperation(value = "扣除库存",notes = "扣除库存")
    @PostMapping("/out")
    public ResponseEntity<SmtStorageInventory> out(@ApiParam(value = "必传：",required = true)@RequestBody @Validated SmtStorageInventory smtStorageInventory) {
        return ControllerUtil.returnCRUD(smtStorageInventoryService.out(smtStorageInventory));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(smtStorageInventoryService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    @Transactional
    @LcnTransaction
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=SmtStorageInventory.update.class) SmtStorageInventory smtStorageInventory) {
        return ControllerUtil.returnCRUD(smtStorageInventoryService.update(smtStorageInventory));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SmtStorageInventory> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        SmtStorageInventory  smtStorageInventory = smtStorageInventoryService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(smtStorageInventory, StringUtils.isEmpty(smtStorageInventory)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SmtStorageInventoryDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSmtStorageInventory searchSmtStorageInventory) {
        Page<Object> page = PageHelper.startPage(searchSmtStorageInventory.getStartPage(),searchSmtStorageInventory.getPageSize());
        List<SmtStorageInventoryDto> list = smtStorageInventoryService.findList(ControllerUtil.dynamicConditionByEntity(searchSmtStorageInventory));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchSmtStorageInventory searchSmtStorageInventory){
    List<SmtStorageInventoryDto> list = smtStorageInventoryService.findList(ControllerUtil.dynamicConditionByEntity(searchSmtStorageInventory));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "SmtStorageInventory信息", SmtStorageInventory.class, "SmtStorageInventory.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }


}
