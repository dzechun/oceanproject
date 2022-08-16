package com.fantechs.provider.base.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseTeamDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseTeamImport;
import com.fantechs.common.base.general.entity.basic.BaseTeam;
import com.fantechs.common.base.general.entity.basic.history.BaseHtTeam;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseTeam;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseHtTeamService;
import com.fantechs.provider.base.service.BaseTeamService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 *
 * Created by leifengzhi on 2021/01/15.
 */
@RestController
@Api(tags = "班组信息管理")
@RequestMapping("/baseTeam")
@Validated
@Slf4j
public class BaseTeamController {

    @Resource
    private BaseTeamService baseTeamService;
    @Resource
    private BaseHtTeamService baseHtTeamService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated BaseTeam baseTeam) {
        return ControllerUtil.returnCRUD(baseTeamService.save(baseTeam));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseTeamService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=BaseTeam.update.class) BaseTeam baseTeam) {
        return ControllerUtil.returnCRUD(baseTeamService.update(baseTeam));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseTeam> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BaseTeam  baseTeam = baseTeamService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseTeam,StringUtils.isEmpty(baseTeam)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseTeamDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseTeam searchBaseTeam) {
        Page<Object> page = PageHelper.startPage(searchBaseTeam.getStartPage(),searchBaseTeam.getPageSize());
        List<BaseTeamDto> list =  baseTeamService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseTeam));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseHtTeam>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchBaseTeam searchBaseTeam) {
        Page<Object> page = PageHelper.startPage(searchBaseTeam.getStartPage(),searchBaseTeam.getPageSize());
        List<BaseHtTeam> list = baseHtTeamService.findHtList(ControllerUtil.dynamicConditionByEntity(searchBaseTeam));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchBaseTeam searchBaseTeam){
    List<BaseTeamDto> list = baseTeamService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseTeam));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "BaseTeam信息", BaseTeamDto.class, "BaseTeam.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

    /**
     * 从excel导入数据
     * @return
     * @throws
     */
    @PostMapping(value = "/import")
    @ApiOperation(value = "从excel导入电子标签信息",notes = "从excel导入电子标签信息")
    public ResponseEntity importExcel(@ApiParam(value ="输入excel文件",required = true)
                                      @RequestPart(value="file") MultipartFile file){
        try {
            // 导入操作
            List<BaseTeamImport> baseTeamImports = EasyPoiUtils.importExcel(file, 2, 1, BaseTeamImport.class);
            Map<String, Object> resultMap = baseTeamService.importExcel(baseTeamImports);
            return ControllerUtil.returnDataSuccess("操作结果集", resultMap);
        }catch (RuntimeException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ControllerUtil.returnFail("文件格式错误", ErrorCodeEnum.OPT20012002.getCode());
        }catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.OPT20012002.getCode());
        }
    }
}
