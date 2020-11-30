package com.fantechs.provider.imes.basic.controller;

import com.fantechs.common.base.entity.basic.SmtProductBom;
import com.fantechs.common.base.entity.basic.history.SmtHtProductBom;
import com.fantechs.common.base.entity.basic.search.SearchSmtProductBom;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.basic.service.SmtHtProductBomService;
import com.fantechs.provider.imes.basic.service.SmtProductBomService;
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
 * Created by wcz on 2020/10/12.
 */
@RestController
@Api(tags = "产品BOM信息")
@RequestMapping("/smtProductBom")
@Validated
public class SmtProductBomController {

    @Autowired
    private SmtProductBomService smtProductBomService;
    @Autowired
    private SmtHtProductBomService smtHtProductBomService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：productBomCode",required = true)@RequestBody @Validated SmtProductBom smtProductBom) {
        return ControllerUtil.returnCRUD(smtProductBomService.save(smtProductBom));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message = "ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(smtProductBomService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value = SmtProductBom.update.class) SmtProductBom smtProductBom) {
        return ControllerUtil.returnCRUD(smtProductBomService.update(smtProductBom));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SmtProductBom> detail(@ApiParam(value = "ID",required = true)@RequestParam @NotNull(message = "id不能为空") Long id) {
        SmtProductBom  smtProductBom = smtProductBomService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(smtProductBom,StringUtils.isEmpty(smtProductBom)?0:1);
    }

    @ApiOperation("产品BOM信息列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SmtProductBom>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSmtProductBom searchSmtProductBom) {
        Page<Object> page = PageHelper.startPage(searchSmtProductBom.getStartPage(),searchSmtProductBom.getPageSize());
        List<SmtProductBom> list = smtProductBomService.findList(searchSmtProductBom);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("产品BOM信息历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<SmtHtProductBom>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchSmtProductBom searchSmtProductBom) {
        Page<Object> page = PageHelper.startPage(searchSmtProductBom.getStartPage(),searchSmtProductBom.getPageSize());
        List<SmtHtProductBom> list = smtHtProductBomService.findList(searchSmtProductBom);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchSmtProductBom searchSmtProductBom){
    List<SmtProductBom> list = smtProductBomService.findList(searchSmtProductBom);
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出产品BOM信息", "产品BOM信息", SmtProductBom.class, "产品BOM信息.xls", response);
        } catch (Exception e) {
         throw new BizErrorException(e);
        }
    }

}
