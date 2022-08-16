package com.fantechs.security.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.security.SysDefaultCustomFormDetDto;
import com.fantechs.common.base.general.entity.security.SysDefaultCustomFormDet;
import com.fantechs.common.base.general.entity.security.search.SearchSysDefaultCustomFormDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.security.service.SysDefaultCustomFormDetService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 *
 * Created by leifengzhi on 2021/09/07.
 */
@RestController
@Api(tags = "默认自定义表单明细控制器")
@RequestMapping("/sysDefaultCustomFormDet")
@Validated
public class SysDefaultCustomFormDetController {

    @Resource
    private SysDefaultCustomFormDetService sysDefaultCustomFormDetService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated SysDefaultCustomFormDet sysDefaultCustomFormDet) {
        return ControllerUtil.returnCRUD(sysDefaultCustomFormDetService.save(sysDefaultCustomFormDet));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(sysDefaultCustomFormDetService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=SysDefaultCustomFormDet.update.class) SysDefaultCustomFormDet sysDefaultCustomFormDet) {
        return ControllerUtil.returnCRUD(sysDefaultCustomFormDetService.update(sysDefaultCustomFormDet));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SysDefaultCustomFormDet> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        SysDefaultCustomFormDet  sysDefaultCustomFormDet = sysDefaultCustomFormDetService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(sysDefaultCustomFormDet,StringUtils.isEmpty(sysDefaultCustomFormDet)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SysDefaultCustomFormDetDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSysDefaultCustomFormDet searchSysDefaultCustomFormDet) {
        Page<Object> page = PageHelper.startPage(searchSysDefaultCustomFormDet.getStartPage(),searchSysDefaultCustomFormDet.getPageSize());
        List<SysDefaultCustomFormDetDto> list = sysDefaultCustomFormDetService.findList(ControllerUtil.dynamicConditionByEntity(searchSysDefaultCustomFormDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchSysDefaultCustomFormDet searchSysDefaultCustomFormDet){
    List<SysDefaultCustomFormDetDto> list = sysDefaultCustomFormDetService.findList(ControllerUtil.dynamicConditionByEntity(searchSysDefaultCustomFormDet));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "SysDefaultCustomFormDet信息", SysDefaultCustomFormDetDto.class, "SysDefaultCustomFormDet.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
