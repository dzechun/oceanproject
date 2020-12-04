package com.fantechs.provider.imes.basic.controller;

import com.fantechs.common.base.dto.basic.SmtClientManageDto;
import com.fantechs.common.base.entity.basic.SmtClientManage;
import com.fantechs.common.base.entity.basic.search.SearchSmtClientManage;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.basic.service.SmtClientManageService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2020/12/01.
 */
@RestController
@Api(tags = "客户端登录管理")
@RequestMapping("/smtClientManage")
@Validated
public class SmtClientManageController {

    @Autowired
    private SmtClientManageService smtClientManageService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated SmtClientManage smtClientManage) {
        return ControllerUtil.returnCRUD(smtClientManageService.save(smtClientManage));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestBody @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(smtClientManageService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=SmtClientManage.update.class) SmtClientManage smtClientManage) {
        return ControllerUtil.returnCRUD(smtClientManageService.update(smtClientManage));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SmtClientManage> detail(@ApiParam(value = "ID",required = true)@RequestBody  @NotNull(message="id不能为空") Long id) {
        SmtClientManage  smtClientManage = smtClientManageService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(smtClientManage,StringUtils.isEmpty(smtClientManage)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SmtClientManageDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSmtClientManage searchSmtClientManage) {
        Page<Object> page = PageHelper.startPage(searchSmtClientManage.getStartPage(),searchSmtClientManage.getPageSize());
        List<SmtClientManageDto> list = smtClientManageService.findList(ControllerUtil.dynamicConditionByEntity(searchSmtClientManage));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }


    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchSmtClientManage searchSmtClientManage){
    List<SmtClientManageDto> list = smtClientManageService.findList(ControllerUtil.dynamicConditionByEntity(searchSmtClientManage));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "SmtClientManage信息", SmtClientManageDto.class, "SmtClientManage.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
