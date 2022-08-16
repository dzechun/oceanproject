package com.fantechs.provider.base.controller;

import com.fantechs.common.base.general.entity.basic.BaseStaffProcess;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseStaffProcess;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseStaffProcessService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/02/04.
 */
@RestController
@Api(tags = "人员工种信息管理")
@RequestMapping("/baseStaffProcess")
@Validated
public class BaseStaffProcessController {

    @Resource
    private BaseStaffProcessService baseStaffProcessService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated BaseStaffProcess baseStaffProcess) {
        return ControllerUtil.returnCRUD(baseStaffProcessService.save(baseStaffProcess));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseStaffProcessService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=BaseStaffProcess.update.class) BaseStaffProcess baseStaffProcess) {
        return ControllerUtil.returnCRUD(baseStaffProcessService.update(baseStaffProcess));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseStaffProcess> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BaseStaffProcess  baseStaffProcess = baseStaffProcessService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseStaffProcess,StringUtils.isEmpty(baseStaffProcess)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseStaffProcess>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseStaffProcess searchBaseStaffProcess) {
        Page<Object> page = PageHelper.startPage(searchBaseStaffProcess.getStartPage(),searchBaseStaffProcess.getPageSize());
        List<BaseStaffProcess> list = baseStaffProcessService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseStaffProcess));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }
}
