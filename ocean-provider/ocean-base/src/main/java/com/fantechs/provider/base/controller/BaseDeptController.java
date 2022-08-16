package com.fantechs.provider.base.controller;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.basic.imports.BaseDeptImport;
import com.fantechs.common.base.general.entity.basic.BaseDept;
import com.fantechs.common.base.general.entity.basic.history.BaseHtDept;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseDept;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseDeptService;
import com.fantechs.provider.base.service.BaseHtDeptService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
 * @Auther: wcz
 * @Date: 2020/9/1 10:03
 * @Description:
 * @Version: 1.0
 */
@RestController
@RequestMapping(value = "/baseDept")
@Api(tags = "部门信息管理")
@Slf4j
@Validated
public class BaseDeptController {

    @Resource
    private BaseDeptService baseDeptService;

    @Resource
    private BaseHtDeptService baseHtDeptService;

    @ApiOperation("查询部门列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseDept>> selectDepts(
            @ApiParam(value = "查询条件，请参考Model说明")@RequestBody(required = false) SearchBaseDept searchBaseDept
    ){
        Page<Object> page = PageHelper.startPage(searchBaseDept.getStartPage(), searchBaseDept.getPageSize());
        List<BaseDept> baseDeptes = baseDeptService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseDept));
        return ControllerUtil.returnDataSuccess(baseDeptes,(int)page.getTotal());
    }

    @ApiOperation("增加部门")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：deptCode、deptName,factoryId",required = true)@RequestBody @Validated BaseDept baseDept){
        return ControllerUtil.returnCRUD(baseDeptService.save(baseDept));
    }

    @ApiOperation("修改部门")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "部门信息对象，部门信息Id必传",required = true)@RequestBody @Validated(value = BaseDept.update.class) BaseDept baseDept){
        return ControllerUtil.returnCRUD(baseDeptService.update(baseDept));
    }

    @ApiOperation("删除部门")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "部门对象ID",required = true)@RequestParam @NotBlank(message = "ids不能为空") String ids){
        return ControllerUtil.returnCRUD(baseDeptService.batchDelete(ids));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseDept> detail(@ApiParam(value = "ID",required = true)@RequestParam @NotNull(message="id不能为空") Long id) {
        BaseDept baseDept = baseDeptService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseDept,StringUtils.isEmpty(baseDept)?0:1);
    }

    /**
     * 导出数据
     * @return
     * @throws
     */
    @PostMapping(value = "/export")
    @ApiOperation(value = "导出部门excel",notes = "导出部门excel",produces = "application/octet-stream")
    public void exportDepts(HttpServletResponse response, @ApiParam(value ="输入查询条件",required = false)
    @RequestBody(required = false) SearchBaseDept searchBaseDept){
        List<BaseDept> list = baseDeptService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseDept));
        try {
            // 导出操作
            EasyPoiUtils.exportExcel(list, "部门信息表", "部门信息", BaseDept.class, "部门信息.xls", response);
        } catch (Exception e) {
            throw new BizErrorException(e);
        }
    }


    @PostMapping("/findHtList")
    @ApiOperation(value = "根据条件查询部门履历信息",notes = "根据条件查询部门履历信息")
    public ResponseEntity<List<BaseHtDept>> selectHtDepts(
            @ApiParam(value = "查询条件，请参考Model说明")@RequestBody(required = false) SearchBaseDept searchBaseDept
             ) {
        Page<Object> page = PageHelper.startPage(searchBaseDept.getStartPage(), searchBaseDept.getPageSize());
        List<BaseHtDept> baseHtDepts = baseHtDeptService.selectHtDepts(ControllerUtil.dynamicConditionByEntity(searchBaseDept));
        return  ControllerUtil.returnDataSuccess(baseHtDepts, (int)page.getTotal());
    }

    /**
     * 从excel导入数据
     * @return
     * @throws
     */
    @PostMapping(value = "/import")
    @ApiOperation(value = "从excel导入部门信息",notes = "从excel导入部门信息")
    public ResponseEntity importExcel(@ApiParam(value ="输入excel文件",required = true)
                                      @RequestPart(value="file") MultipartFile file){
        try {
            // 导入操作
            List<BaseDeptImport> baseDeptImports = EasyPoiUtils.importExcel(file, 2, 1, BaseDeptImport.class);
            Map<String, Object> resultMap = baseDeptService.importExcel(baseDeptImports);
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

    @ApiOperation("批量添加")
    @PostMapping("/batchAdd")
    public ResponseEntity <List<BaseDept>> batchAdd(@ApiParam(value = "",required = true)@RequestBody @Validated List<BaseDept> baseDepts){
        List<BaseDept> list = baseDeptService.batchAdd(baseDepts);
        return  ControllerUtil.returnDataSuccess(list,StringUtils.isEmpty(list)?0:1);
    }
}
