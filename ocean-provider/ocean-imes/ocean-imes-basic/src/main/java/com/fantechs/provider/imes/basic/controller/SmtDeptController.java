package com.fantechs.provider.imes.basic.controller;


import com.fantechs.common.base.entity.basic.SmtDept;
import com.fantechs.common.base.entity.basic.history.SmtHtDept;
import com.fantechs.common.base.entity.basic.search.SearchSmtDept;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.basic.service.SmtDeptService;
import com.fantechs.provider.imes.basic.service.SmtHtDeptService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Auther: wcz
 * @Date: 2020/9/1 10:03
 * @Description:
 * @Version: 1.0
 */
@RestController
@RequestMapping(value = "/smtDept")
@Api(tags = "部门管理")
@Slf4j
public class SmtDeptController {

    @Autowired
    private SmtDeptService smtDeptServiceImpl;

    @Autowired
    private SmtHtDeptService smtHtDeptServiceImpl;

    @ApiOperation("根据条件查询部门信息列表")
    @PostMapping("/selectDepts")
    public ResponseEntity<List<SmtDept>> selectDepts(
            @ApiParam(value = "查询条件，请参考Model说明")@RequestBody(required = false) SearchSmtDept searchSmtDept
    ){
        Page<Object> page = PageHelper.startPage(searchSmtDept.getStartPage(),searchSmtDept.getPageSize());
        List<SmtDept> smtDeptes = smtDeptServiceImpl.selectDepts(searchSmtDept);
        return ControllerUtil.returnDataSuccess(smtDeptes,(int)page.getTotal());
    }

    @ApiOperation("根据厂别ID查询部门信息列表(用户功能调用)")
    @GetMapping("/selectDeptByFactoryId")
    public ResponseEntity<List<SmtDept>> selectDeptByFactoryId(@ApiParam(value = "传入factoryId",required = true) @RequestParam String factoryId){
        List<SmtDept> smtDeptes = smtDeptServiceImpl.selectDeptByFactoryId(factoryId);
        return ControllerUtil.returnDataSuccess(smtDeptes, StringUtils.isEmpty(smtDeptes)?0:1);
    }

    @ApiOperation("增加部门信息")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：deptCode、deptName,factoryId",required = true)@RequestBody SmtDept smtDept){
        if(StringUtils.isEmpty(
                smtDept.getDeptCode(),
                smtDept.getDeptName(),
                smtDept.getFactoryId())){
            return ControllerUtil.returnFailByParameError();
        }
        return ControllerUtil.returnCRUD(smtDeptServiceImpl.insert(smtDept));
    }

    @ApiOperation("修改部门信息")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "部门信息对象，部门信息Id必传",required = true)@RequestBody SmtDept smtDept){
        if(StringUtils.isEmpty(smtDept.getDeptId())){
            return ControllerUtil.returnFailByParameError();
        }
        return ControllerUtil.returnCRUD(smtDeptServiceImpl.updateById(smtDept));
    }

    @ApiOperation("删除部门信息")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "部门对象ID",required = true)@RequestBody List<Long> deptIds){
        if(StringUtils.isEmpty(deptIds)){
            return ControllerUtil.returnFailByParameError();
        }
        return ControllerUtil.returnCRUD(smtDeptServiceImpl.deleteByIds(deptIds));
    }

    /**
     * 导出数据
     * @return
     * @throws
     */
    @PostMapping(value = "/exportDepts")
    @ApiOperation(value = "导出部门信息excel",notes = "导出部门信息excel")
    public void exportDepts(HttpServletResponse response, @ApiParam(value ="输入查询条件",required = false)
    @RequestBody(required = false) SearchSmtDept searchSmtDept){
        List<SmtDept> list = smtDeptServiceImpl.exportDepts(searchSmtDept);
        try {
            // 导出操作
            EasyPoiUtils.exportExcel(list, "部门信息导出", "部门信息", SmtDept.class, "部门信息.xls", response);
        } catch (Exception e) {
            throw new BizErrorException(e);
        }
    }


    @PostMapping("/selectHtDepts")
    @ApiOperation(value = "根据条件查询部门履历信息",notes = "根据条件查询部门履历信息")
    public ResponseEntity<List<SmtHtDept>> selectHtDepts(
            @ApiParam(value = "查询条件，请参考Model说明")@RequestBody(required = false) SearchSmtDept searchSmtDept
             ) {
        Page<Object> page = PageHelper.startPage(searchSmtDept.getStartPage(),searchSmtDept.getPageSize());
        List<SmtHtDept> smtHtDepts=smtHtDeptServiceImpl.selectHtDepts(searchSmtDept);
        return  ControllerUtil.returnDataSuccess(smtHtDepts, (int)page.getTotal());
    }

}
