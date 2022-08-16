package com.fantechs.provider.base.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseOrganizationUserDto;
import com.fantechs.common.base.general.entity.basic.BaseOrganizationUser;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseOrganizationUser;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseOrganizationUserService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2020/12/29.
 */
@RestController
@Api(tags = "用户组织信息管理")
@RequestMapping("/baseOrganizationUser")
@Validated
public class BaseOrganizationUserController {

    @Autowired
    private BaseOrganizationUserService baseOrganizationUserService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated BaseOrganizationUser baseOrganizationUser) {
        return ControllerUtil.returnCRUD(baseOrganizationUserService.save(baseOrganizationUser));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseOrganizationUserService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=BaseOrganizationUser.update.class) BaseOrganizationUser baseOrganizationUser) {
        return ControllerUtil.returnCRUD(baseOrganizationUserService.update(baseOrganizationUser));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseOrganizationUser> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BaseOrganizationUser  baseOrganizationUser = baseOrganizationUserService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseOrganizationUser,StringUtils.isEmpty(baseOrganizationUser)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseOrganizationUserDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseOrganizationUser searchBaseOrganizationUser) {
        Page<Object> page = PageHelper.startPage(searchBaseOrganizationUser.getStartPage(),searchBaseOrganizationUser.getPageSize());
        List<BaseOrganizationUserDto> list = baseOrganizationUserService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseOrganizationUser));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    /*@ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseOrganizationUser>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchBaseOrganizationUser searchBaseOrganizationUser) {
        Page<Object> page = PageHelper.startPage(searchBaseOrganizationUser.getStartPage(),searchBaseOrganizationUser.getPageSize());
        List<BaseOrganizationUser> list = baseOrganizationUserService.findList(searchBaseOrganizationUser);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }*/

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchBaseOrganizationUser searchBaseOrganizationUser){
    List<BaseOrganizationUserDto> list = baseOrganizationUserService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseOrganizationUser));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "BaseOrganizationUser信息", BaseOrganizationUserDto.class, "BaseOrganizationUser.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

    @ApiOperation(value = "批量新增",notes = "批量新增")
    @PostMapping("/batchSave")
    public ResponseEntity batchSave(@ApiParam(value = "必传：",required = true)@RequestBody List<BaseOrganizationUser> baseOrganizationUsers) {
        return ControllerUtil.returnCRUD(baseOrganizationUserService.batchSave(baseOrganizationUsers));
    }
}
