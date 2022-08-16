package com.fantechs.provider.qms.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.qms.QmsIpqcInspectionOrderAuditUser;
import com.fantechs.common.base.general.entity.qms.search.SearchQmsIpqcInspectionOrderAuditUser;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.qms.service.QmsIpqcInspectionOrderAuditUserService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/06/08.
 */
@RestController
@Api(tags = "IPQC检验单审批")
@RequestMapping("/qmsIpqcInspectionOrderAuditUser")
@Validated
public class QmsIpqcInspectionOrderAuditUserController {

    @Resource
    private QmsIpqcInspectionOrderAuditUserService qmsIpqcInspectionOrderAuditUserService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated QmsIpqcInspectionOrderAuditUser qmsIpqcInspectionOrderAuditUser) {
        return ControllerUtil.returnCRUD(qmsIpqcInspectionOrderAuditUserService.save(qmsIpqcInspectionOrderAuditUser));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(qmsIpqcInspectionOrderAuditUserService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=QmsIpqcInspectionOrderAuditUser.update.class) QmsIpqcInspectionOrderAuditUser qmsIpqcInspectionOrderAuditUser) {
        return ControllerUtil.returnCRUD(qmsIpqcInspectionOrderAuditUserService.update(qmsIpqcInspectionOrderAuditUser));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<QmsIpqcInspectionOrderAuditUser> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        QmsIpqcInspectionOrderAuditUser  qmsIpqcInspectionOrderAuditUser = qmsIpqcInspectionOrderAuditUserService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(qmsIpqcInspectionOrderAuditUser,StringUtils.isEmpty(qmsIpqcInspectionOrderAuditUser)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<QmsIpqcInspectionOrderAuditUser>> findList(@ApiParam(value = "查询对象")@RequestBody SearchQmsIpqcInspectionOrderAuditUser searchQmsIpqcInspectionOrderAuditUser) {
        Page<Object> page = PageHelper.startPage(searchQmsIpqcInspectionOrderAuditUser.getStartPage(),searchQmsIpqcInspectionOrderAuditUser.getPageSize());
        List<QmsIpqcInspectionOrderAuditUser> list = qmsIpqcInspectionOrderAuditUserService.findList(ControllerUtil.dynamicConditionByEntity(searchQmsIpqcInspectionOrderAuditUser));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

   /* @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<QmsIpqcInspectionOrderAuditUser>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchQmsIpqcInspectionOrderAuditUser searchQmsIpqcInspectionOrderAuditUser) {
        Page<Object> page = PageHelper.startPage(searchQmsIpqcInspectionOrderAuditUser.getStartPage(),searchQmsIpqcInspectionOrderAuditUser.getPageSize());
        List<QmsIpqcInspectionOrderAuditUser> list = qmsIpqcInspectionOrderAuditUserService.findHtList(ControllerUtil.dynamicConditionByEntity(searchQmsIpqcInspectionOrderAuditUser));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }*/

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchQmsIpqcInspectionOrderAuditUser searchQmsIpqcInspectionOrderAuditUser){
    List<QmsIpqcInspectionOrderAuditUser> list = qmsIpqcInspectionOrderAuditUserService.findList(ControllerUtil.dynamicConditionByEntity(searchQmsIpqcInspectionOrderAuditUser));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "IPQC检验单审批", QmsIpqcInspectionOrderAuditUser.class, "IPQC检验单审批.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
