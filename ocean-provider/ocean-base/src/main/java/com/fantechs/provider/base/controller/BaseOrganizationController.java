package com.fantechs.provider.base.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseOrganizationDto;
import com.fantechs.common.base.general.entity.basic.BaseOrganization;
import com.fantechs.common.base.general.entity.basic.history.BaseHtProductFamily;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseOrganization;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseHtProductFamilyService;
import com.fantechs.provider.base.service.BaseOrganizationService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by leifengzhi on 2020/12/29.
 */
@RestController
@Api(tags = "组织信息管理")
@RequestMapping("/baseOrganization")
@Validated
public class BaseOrganizationController {

    @Autowired
    private BaseOrganizationService baseOrganizationService;
    @Resource
    private BaseHtProductFamilyService baseHtProductFamilyService;

    @ApiOperation(value = "新增", notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：organizationCode、organizationName", required = true) @RequestBody @Validated BaseOrganization baseOrganization) {
        return ControllerUtil.returnCRUD(baseOrganizationService.save(baseOrganization));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔", required = true) @RequestParam @NotBlank(message = "ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseOrganizationService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传", required = true) @RequestBody @Validated(value = BaseOrganization.update.class) BaseOrganization baseOrganization) {
        return ControllerUtil.returnCRUD(baseOrganizationService.update(baseOrganization));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseOrganization> detail(@ApiParam(value = "ID", required = true) @RequestParam @NotNull(message = "id不能为空") Long id) {
        BaseOrganization baseOrganization = baseOrganizationService.selectByKey(id);
        return ControllerUtil.returnDataSuccess(baseOrganization, StringUtils.isEmpty(baseOrganization) ? 0 : 1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseOrganizationDto>> findList(@ApiParam(value = "查询对象") @RequestBody SearchBaseOrganization searchBaseOrganization) {
        Page<Object> page = PageHelper.startPage(searchBaseOrganization.getStartPage(), searchBaseOrganization.getPageSize());
        List<BaseOrganizationDto> list = baseOrganizationService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseOrganization));
        return ControllerUtil.returnDataSuccess(list, (int) page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseHtProductFamily>> findHtList(@ApiParam(value = "查询对象") @RequestBody SearchBaseOrganization searchBaseOrganization) {
        Page<Object> page = PageHelper.startPage(searchBaseOrganization.getStartPage(), searchBaseOrganization.getPageSize());
        List<BaseHtProductFamily> list = baseHtProductFamilyService.findHtList(ControllerUtil.dynamicConditionByEntity(searchBaseOrganization));
        return ControllerUtil.returnDataSuccess(list, (int) page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel", notes = "导出excel", produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchBaseOrganization searchBaseOrganization) {
        List<BaseOrganizationDto> list = baseOrganizationService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseOrganization));
        try {
            // 导出操作
            EasyPoiUtils.exportExcel(list, "导出信息", "BaseOrganization信息", BaseOrganizationDto.class, "BaseOrganization.xls", response);
        } catch (Exception e) {
            throw new BizErrorException(e);
        }
    }

}
