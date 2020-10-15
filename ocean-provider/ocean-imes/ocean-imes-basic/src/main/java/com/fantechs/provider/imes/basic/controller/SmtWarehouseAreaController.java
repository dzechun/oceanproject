package com.fantechs.provider.imes.basic.controller;

import com.fantechs.common.base.dto.basic.SmtWarehouseAreaDto;
import com.fantechs.common.base.entity.basic.SmtWarehouseArea;
import com.fantechs.common.base.entity.basic.history.SmtHtWarehouseArea;
import com.fantechs.common.base.entity.basic.search.SearchSmtWarehouseArea;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.basic.service.SmtHtWarehouseAreaService;
import com.fantechs.provider.imes.basic.service.SmtWarehouseAreaService;
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
 * Created by leifengzhi on 2020/09/23.
 */
@RestController
@Api(tags = "仓库区域控制器")
@RequestMapping("/smtWarehouseArea")
@Validated
public class SmtWarehouseAreaController {

    @Autowired
    private SmtWarehouseAreaService smtWarehouseAreaService;
    @Autowired
    private SmtHtWarehouseAreaService smtHtWarehouseAreaService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：warehouseAreaCode、warehouseAreaName、warehouseId",required = true)@RequestBody @Validated SmtWarehouseArea smtWarehouseArea) {
        return ControllerUtil.returnCRUD(smtWarehouseAreaService.save(smtWarehouseArea));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotNull(message = "ids不能为空") @NotBlank(message = "ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(smtWarehouseAreaService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value = SmtWarehouseArea.update.class) SmtWarehouseArea smtWarehouseArea) {
        return ControllerUtil.returnCRUD(smtWarehouseAreaService.update(smtWarehouseArea));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SmtWarehouseArea> detail(@ApiParam(value = "工厂ID",required = true)@RequestParam @NotNull(message = "id不能为空") Long id) {
        SmtWarehouseArea  smtWarehouseArea = smtWarehouseAreaService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(smtWarehouseArea,StringUtils.isEmpty(smtWarehouseArea)?0:1);
    }

    @ApiOperation("根据条件查询角色信息列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SmtWarehouseAreaDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSmtWarehouseArea searchSmtWarehouseArea) {
        Page<Object> page = PageHelper.startPage(searchSmtWarehouseArea.getStartPage(),searchSmtWarehouseArea.getPageSize());
        List<SmtWarehouseAreaDto> list = smtWarehouseAreaService.findList(ControllerUtil.dynamicConditionByEntity(searchSmtWarehouseArea));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation(value = "获取物料履历列表",notes = "获取物料履历列表")
    @PostMapping("/findHtList")
    public ResponseEntity< List<SmtHtWarehouseArea>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchSmtWarehouseArea searchSmtWarehouseArea){
        Page<Object> page = PageHelper.startPage(searchSmtWarehouseArea.getStartPage(),searchSmtWarehouseArea.getPageSize());
        List<SmtHtWarehouseArea> htMaterials = smtHtWarehouseAreaService.findHtList(ControllerUtil.dynamicConditionByEntity(searchSmtWarehouseArea));
        return ControllerUtil.returnDataSuccess(htMaterials,(int)page.getTotal());
    }

    /**
     * 导出数据
     * @return
     * @throws
     */
    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出仓库区域excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")@RequestBody SearchSmtWarehouseArea searchSmtWarehouseArea){
        List<SmtWarehouseAreaDto> list = smtWarehouseAreaService.findList(ControllerUtil.dynamicConditionByEntity(searchSmtWarehouseArea));
        try {
            // 导出操作
            EasyPoiUtils.exportExcel(list, "导出仓库区域", "仓库区域信息", SmtWarehouseAreaDto.class, "仓库区域信息.xls", response);
        } catch (Exception e) {
            throw new BizErrorException(e);
        }
    }
}
