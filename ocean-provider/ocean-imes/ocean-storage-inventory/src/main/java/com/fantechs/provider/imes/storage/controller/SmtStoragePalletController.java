package com.fantechs.provider.imes.storage.controller;

import com.fantechs.common.base.dto.storage.SmtStoragePalletDto;
import com.fantechs.common.base.entity.storage.SmtStoragePallet;
import com.fantechs.common.base.entity.storage.search.SearchSmtStoragePallet;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;

import com.fantechs.provider.imes.storage.service.SmtStoragePalletService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/01/07.
 */
@RestController
@Api(tags = "smtStoragePallet控制器")
@RequestMapping("/smtStoragePallet")
@Validated
public class SmtStoragePalletController {

    @Resource
    private SmtStoragePalletService smtStoragePalletService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated SmtStoragePallet smtStoragePallet) {
        return ControllerUtil.returnCRUD(smtStoragePalletService.save(smtStoragePallet));
    }

//    @ApiOperation("删除")
//    @PostMapping("/delete")
//    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
//        return ControllerUtil.returnCRUD(smtStoragePalletService.batchDelete(ids));
//    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=SmtStoragePallet.update.class) SmtStoragePallet smtStoragePallet) {
        return ControllerUtil.returnCRUD(smtStoragePalletService.update(smtStoragePallet));
    }

//    @ApiOperation("获取详情")
//    @PostMapping("/detail")
//    public ResponseEntity<SmtStoragePallet> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
//        SmtStoragePallet  smtStoragePallet = smtStoragePalletService.selectByKey(id);
//        return  ControllerUtil.returnDataSuccess(smtStoragePallet,StringUtils.isEmpty(smtStoragePallet)?0:1);
//    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SmtStoragePalletDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSmtStoragePallet searchSmtStoragePallet) {
        Page<Object> page = PageHelper.startPage(searchSmtStoragePallet.getStartPage(),searchSmtStoragePallet.getPageSize());
        List<SmtStoragePalletDto> list = smtStoragePalletService.findList(ControllerUtil.dynamicConditionByEntity(searchSmtStoragePallet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }
//
//    @ApiOperation("历史列表")
//    @PostMapping("/findHtList")
//    public ResponseEntity<List<SmtStoragePallet>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchSmtStoragePallet searchSmtStoragePallet) {
//        Page<Object> page = PageHelper.startPage(searchSmtStoragePallet.getStartPage(),searchSmtStoragePallet.getPageSize());
//        List<SmtStoragePallet> list = smtStoragePalletService.findList(searchSmtStoragePallet);
//        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
//    }
//
//    @PostMapping(value = "/export")
//    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
//    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
//    @RequestBody(required = false) SearchSmtStoragePallet searchSmtStoragePallet){
//    List<SmtStoragePallet> list = smtStoragePalletService.findList(searchSmtStoragePallet);
//    try {
//        // 导出操作
//        EasyPoiUtils.exportExcel(list, "导出信息", "SmtStoragePallet信息", SmtStoragePallet.class, "SmtStoragePallet.xls", response);
//        } catch (Exception e) {
//        throw new BizErrorException(e);
//        }
//    }
}
