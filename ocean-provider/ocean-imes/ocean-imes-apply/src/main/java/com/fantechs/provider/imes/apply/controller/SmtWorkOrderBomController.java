package com.fantechs.provider.imes.apply.controller;

import com.fantechs.common.base.dto.apply.SmtWorkOrderBomDto;
import com.fantechs.common.base.entity.apply.SmtWorkOrderBom;
import com.fantechs.common.base.entity.apply.history.SmtHtWorkOrderBom;
import com.fantechs.common.base.entity.apply.search.SearchSmtWorkOrderBom;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.apply.service.SmtHtWorkOrderBomService;
import com.fantechs.provider.imes.apply.service.SmtWorkOrderBomService;
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
 * Created by wcz on 2020/10/14.
 */
@RestController
@Api(tags = "工单BOM信息")
@RequestMapping("/smtWorkOrderBom")
@Validated
public class SmtWorkOrderBomController {

    @Autowired
    private SmtWorkOrderBomService smtWorkOrderBomService;
    @Autowired
    private SmtHtWorkOrderBomService smtHtWorkOrderBomService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：workOrderId、partMaterialId、processId、singleQuantity",required = true)@RequestBody @Validated SmtWorkOrderBom smtWorkOrderBom) {
        return ControllerUtil.returnCRUD(smtWorkOrderBomService.save(smtWorkOrderBom));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(smtWorkOrderBomService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=SmtWorkOrderBom.update.class) SmtWorkOrderBom smtWorkOrderBom) {
        return ControllerUtil.returnCRUD(smtWorkOrderBomService.update(smtWorkOrderBom));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SmtWorkOrderBom> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        SmtWorkOrderBom  smtWorkOrderBom = smtWorkOrderBomService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(smtWorkOrderBom,StringUtils.isEmpty(smtWorkOrderBom)?0:1);
    }

    @ApiOperation("工单BOM信息列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SmtWorkOrderBomDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSmtWorkOrderBom searchSmtWorkOrderBom) {
        Page<Object> page = PageHelper.startPage(searchSmtWorkOrderBom.getStartPage(),searchSmtWorkOrderBom.getPageSize());
        List<SmtWorkOrderBomDto> list = smtWorkOrderBomService.findList(searchSmtWorkOrderBom);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("工单BOM信息历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<SmtHtWorkOrderBom>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchSmtWorkOrderBom searchSmtWorkOrderBom) {
        Page<Object> page = PageHelper.startPage(searchSmtWorkOrderBom.getStartPage(),searchSmtWorkOrderBom.getPageSize());
        List<SmtHtWorkOrderBom> list = smtHtWorkOrderBomService.findList(searchSmtWorkOrderBom);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
                            @RequestBody(required = false) SearchSmtWorkOrderBom searchSmtWorkOrderBom){
    List<SmtWorkOrderBomDto> list = smtWorkOrderBomService.findList(searchSmtWorkOrderBom);
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出工单BOM信息", "工单BOM信息", SmtWorkOrderBomDto.class, "工单BOM信息.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
