package com.fantechs.provider.base.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.basic.BaseSampleProcess;
import com.fantechs.common.base.general.entity.basic.history.BaseHtSampleProcess;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseSampleProcess;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseHtSampleProcessService;
import com.fantechs.provider.base.service.BaseSampleProcessService;
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
 * Created by leifengzhi on 2021/05/19.
 */
@RestController
@Api(tags = "抽样过程控制器")
@RequestMapping("/baseSampleProcess")
@Validated
public class BaseSampleProcessController {

    @Resource
    private BaseSampleProcessService baseSampleProcessService;
    @Resource
    private BaseHtSampleProcessService baseHtSampleProcessService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated BaseSampleProcess baseSampleProcess) {
        return ControllerUtil.returnCRUD(baseSampleProcessService.save(baseSampleProcess));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseSampleProcessService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=BaseSampleProcess.update.class) BaseSampleProcess baseSampleProcess) {
        return ControllerUtil.returnCRUD(baseSampleProcessService.update(baseSampleProcess));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseSampleProcess> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BaseSampleProcess  baseSampleProcess = baseSampleProcessService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseSampleProcess,StringUtils.isEmpty(baseSampleProcess)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseSampleProcess>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseSampleProcess searchBaseSampleProcess) {
        Page<Object> page = PageHelper.startPage(searchBaseSampleProcess.getStartPage(),searchBaseSampleProcess.getPageSize());
        List<BaseSampleProcess> list = baseSampleProcessService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseSampleProcess));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("根据多个ID查询列表")
    @PostMapping("/findListByIds")
    public ResponseEntity<List<BaseSampleProcess>> findListByIds(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        List<BaseSampleProcess> list = baseSampleProcessService.findListByIds(ids);
        return ControllerUtil.returnDataSuccess(list,list.size());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseHtSampleProcess>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchBaseSampleProcess searchBaseSampleProcess) {
        Page<Object> page = PageHelper.startPage(searchBaseSampleProcess.getStartPage(),searchBaseSampleProcess.getPageSize());
        List<BaseHtSampleProcess> list = baseHtSampleProcessService.findHtList(ControllerUtil.dynamicConditionByEntity(searchBaseSampleProcess));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchBaseSampleProcess searchBaseSampleProcess){
    List<BaseSampleProcess> list = baseSampleProcessService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseSampleProcess));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "抽样过程信息", BaseSampleProcess.class, "抽样过程.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
