package com.fantechs.provider.electronic.controller;

import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.electronic.entity.SmtLoading;
import com.fantechs.common.base.electronic.entity.search.SearchSmtLoading;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.electronic.service.SmtLoadingService;
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
@Api(tags = "捡料上架控制器")
@RequestMapping("/smtLoading")
@Validated
public class SmtLoadingController {

    @Autowired
    private SmtLoadingService smtLoadingService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    @Transactional
    @LcnTransaction
    public ResponseEntity<SmtLoading> add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated SmtLoading smtLoading) {

        int count = smtLoadingService.save(smtLoading);
        if(count > 0) {
            return ControllerUtil.returnSuccess("操作成功", smtLoading);
        } else {
            return ControllerUtil.returnFail(ErrorCodeEnum.GL99990005.getMsg(), ErrorCodeEnum.GL99990005.getCode());
        }
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(smtLoadingService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    @Transactional
    @LcnTransaction
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=SmtLoading.update.class) SmtLoading smtLoading) {
        return ControllerUtil.returnCRUD(smtLoadingService.update(smtLoading));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SmtLoading> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        SmtLoading  smtLoading = smtLoadingService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(smtLoading,StringUtils.isEmpty(smtLoading)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SmtLoading>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSmtLoading searchSmtLoading) {
        Page<Object> page = PageHelper.startPage(searchSmtLoading.getStartPage(),searchSmtLoading.getPageSize());
        List<SmtLoading> list = smtLoadingService.findList(ControllerUtil.dynamicConditionByEntity(searchSmtLoading));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<SmtLoading>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchSmtLoading searchSmtLoading) {
        Page<Object> page = PageHelper.startPage(searchSmtLoading.getStartPage(),searchSmtLoading.getPageSize());
        List<SmtLoading> list = smtLoadingService.findHtList(ControllerUtil.dynamicConditionByEntity(searchSmtLoading));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchSmtLoading searchSmtLoading){    List<SmtLoading> list = smtLoadingService.findList(ControllerUtil.dynamicConditionByEntity(searchSmtLoading));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "捡料上架单", "捡料上架单信息", SmtLoading.class, "捡料上架单.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
