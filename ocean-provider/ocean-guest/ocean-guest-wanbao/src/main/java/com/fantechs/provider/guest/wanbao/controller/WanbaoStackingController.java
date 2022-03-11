package com.fantechs.provider.guest.wanbao.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.common.base.general.dto.wanbao.WanbaoStackingDto;
import com.fantechs.common.base.general.entity.wanbao.search.SearchWanbaoStacking;
import com.fantechs.common.base.general.entity.wanbao.WanbaoStacking;
import com.fantechs.provider.guest.wanbao.service.WanbaoStackingService;
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
@Api(tags = "万宝-堆垛控制器")
@RequestMapping("/wanbaoStacking")
@Validated
public class WanbaoStackingController {

    @Resource
    private WanbaoStackingService wanbaoStackingService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WanbaoStacking wanbaoStacking) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        wanbaoStacking.setCreateTime(new Date());
        wanbaoStacking.setCreateUserId(sysUser.getUserId());
        wanbaoStacking.setModifiedTime(new Date());
        wanbaoStacking.setModifiedUserId(sysUser.getUserId());
        wanbaoStacking.setOrgId(sysUser.getOrganizationId());
        wanbaoStacking.setStatus((byte) 1);
        return ControllerUtil.returnCRUD(wanbaoStackingService.save(wanbaoStacking));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(wanbaoStackingService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=WanbaoStacking.update.class) WanbaoStacking wanbaoStacking) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        wanbaoStacking.setModifiedTime(new Date());
        wanbaoStacking.setModifiedUserId(sysUser.getUserId());
        return ControllerUtil.returnCRUD(wanbaoStackingService.update(wanbaoStacking));
    }

    @ApiOperation("修改状态并清空明细表条码")
    @PostMapping("/updateAndClearBarcode")
    public ResponseEntity updateAndClearBarcode(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=WanbaoStacking.update.class) WanbaoStacking wanbaoStacking) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        wanbaoStacking.setModifiedTime(new Date());
        wanbaoStacking.setModifiedUserId(sysUser.getUserId());
        return ControllerUtil.returnCRUD(wanbaoStackingService.updateAndClearBarcode(wanbaoStacking));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<WanbaoStacking> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        WanbaoStacking  wanbaoStacking = wanbaoStackingService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(wanbaoStacking,StringUtils.isEmpty(wanbaoStacking)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WanbaoStackingDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWanbaoStacking searchWanbaoStacking) {
        Page<Object> page = PageHelper.startPage(searchWanbaoStacking.getStartPage(),searchWanbaoStacking.getPageSize());
        List<WanbaoStackingDto> list = wanbaoStackingService.findList(ControllerUtil.dynamicConditionByEntity(searchWanbaoStacking));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<WanbaoStackingDto>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchWanbaoStacking searchWanbaoStacking) {
        List<WanbaoStackingDto> list = wanbaoStackingService.findList(ControllerUtil.dynamicConditionByEntity(searchWanbaoStacking));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchWanbaoStacking searchWanbaoStacking){
    List<WanbaoStackingDto> list = wanbaoStackingService.findList(ControllerUtil.dynamicConditionByEntity(searchWanbaoStacking));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "堆垛信息", WanbaoStackingDto.class, "堆垛信息.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

    @PostMapping(value = "/import")
    @ApiOperation(value = "从excel导入",notes = "从excel导入")
    public ResponseEntity importExcel(@ApiParam(value ="输入excel文件",required = true) @RequestPart(value="file") MultipartFile file){
        try {
            // 导入操作
            List<WanbaoStackingDto> dtos = EasyPoiUtils.importExcel(file, 0, 1, WanbaoStackingDto.class);
            Map<String, Object> resultMap = wanbaoStackingService.importExcel(dtos);
            return ControllerUtil.returnDataSuccess("操作结果集",resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.OPT20012002.getCode());
        }
    }
}
