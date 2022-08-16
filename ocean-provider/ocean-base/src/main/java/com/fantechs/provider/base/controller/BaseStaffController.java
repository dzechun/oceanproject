package com.fantechs.provider.base.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseStaffDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseStaffImport;
import com.fantechs.common.base.general.entity.basic.BaseStaff;
import com.fantechs.common.base.general.entity.basic.history.BaseHtStaff;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseStaff;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseHtStaffService;
import com.fantechs.provider.base.service.BaseStaffService;
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
 * Created by leifengzhi on 2021/01/16.
 */
@RestController
@Api(tags = "人员信息管理")
@RequestMapping("/baseStaff")
@Validated
@Slf4j
public class BaseStaffController {

    @Resource
    private BaseStaffService baseStaffService;
    @Resource
    private BaseHtStaffService baseHtStaffService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated BaseStaff baseStaff) {
        return ControllerUtil.returnCRUD(baseStaffService.save(baseStaff));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseStaffService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=BaseStaff.update.class) BaseStaff baseStaff) {
        return ControllerUtil.returnCRUD(baseStaffService.update(baseStaff));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseStaff> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BaseStaff  baseStaff = baseStaffService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseStaff,StringUtils.isEmpty(baseStaff)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseStaffDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseStaff searchBaseStaff) {
        Page<Object> page = PageHelper.startPage(searchBaseStaff.getStartPage(),searchBaseStaff.getPageSize());
        List<BaseStaffDto> list = baseStaffService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseStaff));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseHtStaff>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchBaseStaff searchBaseStaff) {
        Page<Object> page = PageHelper.startPage(searchBaseStaff.getStartPage(),searchBaseStaff.getPageSize());
        List<BaseHtStaff> list = baseHtStaffService.findHtList(ControllerUtil.dynamicConditionByEntity(searchBaseStaff));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchBaseStaff searchBaseStaff){
    List<BaseStaffDto> list = baseStaffService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseStaff));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "员工信息表", "BaseStaff信息", BaseStaffDto.class, "BaseStaff.xls", response);
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
    @ApiOperation(value = "从excel导入部件组成信息",notes = "从excel导入部件组成信息")
    public ResponseEntity importExcel(@ApiParam(value ="输入excel文件",required = true)
                                      @RequestPart(value="file") MultipartFile file){
        try {
            // 导入操作
            List<BaseStaffImport> baseStaffImports = EasyPoiUtils.importExcel(file, 2, 1, BaseStaffImport.class);
            Map<String, Object> resultMap = baseStaffService.importExcel(baseStaffImports);
            return ControllerUtil.returnDataSuccess("操作结果集", resultMap);
        } catch (RuntimeException e) {
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
