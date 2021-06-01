package com.fantechs.provider.electronic.controller;

import com.fantechs.common.base.electronic.dto.PtlClientManageDto;
import com.fantechs.common.base.electronic.entity.PtlClientManage;
import com.fantechs.common.base.electronic.entity.search.SearchPtlClientManage;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.electronic.service.PtlClientManageService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2020/12/01.
 */
@RestController
@Api(tags = "客户端登录管理")
@RequestMapping("/smtClientManage")
@Validated
public class PtlClientManageController {

    @Autowired
    private PtlClientManageService ptlClientManageService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated PtlClientManage ptlClientManage) {
        return ControllerUtil.returnCRUD(ptlClientManageService.save(ptlClientManage));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(ptlClientManageService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value= PtlClientManage.update.class) PtlClientManage ptlClientManage) {
        return ControllerUtil.returnCRUD(ptlClientManageService.update(ptlClientManage));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<PtlClientManage> detail(@ApiParam(value = "ID",required = true)@RequestBody  @NotNull(message="id不能为空") Long id) {
        PtlClientManage ptlClientManage = ptlClientManageService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(ptlClientManage,StringUtils.isEmpty(ptlClientManage)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<PtlClientManageDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchPtlClientManage searchPtlClientManage) {
        Page<Object> page = PageHelper.startPage(searchPtlClientManage.getStartPage(), searchPtlClientManage.getPageSize());
        List<PtlClientManageDto> list = ptlClientManageService.findList(ControllerUtil.dynamicConditionByEntity(searchPtlClientManage));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }


    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchPtlClientManage searchPtlClientManage){
    List<PtlClientManageDto> list = ptlClientManageService.findList(ControllerUtil.dynamicConditionByEntity(searchPtlClientManage));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "SmtClientManage信息", PtlClientManageDto.class, "SmtClientManage.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
