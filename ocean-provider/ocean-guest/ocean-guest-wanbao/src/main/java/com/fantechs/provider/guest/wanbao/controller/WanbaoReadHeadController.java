package com.fantechs.provider.guest.wanbao.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.common.base.general.dto.wanbao.WanbaoReadHeadDto;
import com.fantechs.common.base.general.entity.wanbao.search.SearchWanbaoReadHead;
import com.fantechs.common.base.general.entity.wanbao.WanbaoReadHead;
import com.fantechs.provider.guest.wanbao.service.WanbaoReadHeadService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by bgkun on 2021/11/29.
 */
@RestController
@Api(tags = "万宝-读头控制器")
@RequestMapping("/wanbaoReadHead")
@Validated
public class WanbaoReadHeadController {

    @Resource
    private WanbaoReadHeadService wanbaoReadHeadService;

    @ApiOperation(value = "新增读头",notes = "新增读头")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WanbaoReadHead wanbaoReadHead) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        wanbaoReadHead.setCreateTime(new Date());
        wanbaoReadHead.setCreateUserId(sysUser.getUserId());
        wanbaoReadHead.setModifiedTime(new Date());
        wanbaoReadHead.setModifiedUserId(sysUser.getUserId());
        wanbaoReadHead.setOrgId(sysUser.getOrganizationId());
        wanbaoReadHead.setStatus((byte) 1);
        return ControllerUtil.returnCRUD(wanbaoReadHeadService.save(wanbaoReadHead));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(wanbaoReadHeadService.batchDelete(ids));
    }

    @ApiOperation("修改读头")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=WanbaoReadHead.update.class) WanbaoReadHead wanbaoReadHead) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        wanbaoReadHead.setModifiedTime(new Date());
        wanbaoReadHead.setModifiedUserId(sysUser.getUserId());
        return ControllerUtil.returnCRUD(wanbaoReadHeadService.update(wanbaoReadHead));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<WanbaoReadHead> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        WanbaoReadHead  wanbaoReadHead = wanbaoReadHeadService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(wanbaoReadHead,StringUtils.isEmpty(wanbaoReadHead)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WanbaoReadHeadDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWanbaoReadHead searchWanbaoReadHead) {
        Page<Object> page = PageHelper.startPage(searchWanbaoReadHead.getStartPage(),searchWanbaoReadHead.getPageSize());
        List<WanbaoReadHeadDto> list = wanbaoReadHeadService.findList(ControllerUtil.dynamicConditionByEntity(searchWanbaoReadHead));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<WanbaoReadHeadDto>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchWanbaoReadHead searchWanbaoReadHead) {
        List<WanbaoReadHeadDto> list = wanbaoReadHeadService.findList(ControllerUtil.dynamicConditionByEntity(searchWanbaoReadHead));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchWanbaoReadHead searchWanbaoReadHead){
    List<WanbaoReadHeadDto> list = wanbaoReadHeadService.findList(ControllerUtil.dynamicConditionByEntity(searchWanbaoReadHead));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "读头信息", WanbaoReadHeadDto.class, "读头信息.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

    @PostMapping(value = "/import")
    @ApiOperation(value = "从excel导入",notes = "从excel导入")
    public ResponseEntity importExcel(@ApiParam(value ="输入excel文件",required = true) @RequestPart(value="file") MultipartFile file){
        try {
            // 导入操作
            List<WanbaoReadHeadDto> dtos = EasyPoiUtils.importExcel(file, 0, 1, WanbaoReadHeadDto.class);
            Map<String, Object> resultMap = wanbaoReadHeadService.importExcel(dtos);
            return ControllerUtil.returnDataSuccess("操作结果集",resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.OPT20012002.getCode());
        }
    }
}
