package com.fantechs.provider.mes.pm.controller;

import com.fantechs.common.base.general.dto.mes.pm.MesPmExplainProcessPlanDTO;
import com.fantechs.common.base.general.entity.mes.pm.MesPmExplainProcessPlan;
import com.fantechs.provider.mes.pm.service.MesPmExplainProcessPlanService;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchMesPmExplainProcessPlanListDTO;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.utils.StringUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import javax.annotation.Resource;
import java.util.List;
/**
 * @Auther: bingo.ren
 * @Date: 2021年1月18日 10:46
 * @Description: 执行工序计划表管理
 * @Version: 1.0
 */
@RestController
@Api(tags = "执行工序计划表管理",basePath = "mesPmExplainProcessPlan")
@RequestMapping("mesPmExplainProcessPlan")
@Slf4j
public class MesPmExplainProcessPlanController {

    @Resource
    private MesPmExplainProcessPlanService mesPmExplainProcessPlanService;

    @ApiOperation("查询执行工序计划表列表")
    @PostMapping("findList")
    public ResponseEntity<List<MesPmExplainProcessPlanDTO>> list(
            @ApiParam(value = "查询条件，请参考Model说明")@RequestBody(required = false) SearchMesPmExplainProcessPlanListDTO searchMesPmExplainProcessPlanListDTO
    ){
        Page<Object> page = PageHelper.startPage(searchMesPmExplainProcessPlanListDTO.getStartPage(), searchMesPmExplainProcessPlanListDTO.getPageSize());
        List<MesPmExplainProcessPlanDTO> mesPmExplainProcessPlanDTOList = mesPmExplainProcessPlanService.selectFilterAll(ControllerUtil.dynamicConditionByEntity(searchMesPmExplainProcessPlanListDTO));
        return ControllerUtil.returnDataSuccess(mesPmExplainProcessPlanDTOList,(int)page.getTotal());
    }

    @ApiOperation("通过ID查询执行工序计划表")
    @GetMapping("detail")
    public ResponseEntity<MesPmExplainProcessPlan> one(@ApiParam(value = "执行工序计划表对象ID",required = true)@RequestParam Long id){
        MesPmExplainProcessPlan mesPmExplainProcessPlan = mesPmExplainProcessPlanService.selectByKey(id);
        return ControllerUtil.returnDataSuccess(mesPmExplainProcessPlan, StringUtils.isEmpty(mesPmExplainProcessPlan)?0:1);
    }

    @ApiOperation("增加执行工序计划表数据")
    @PostMapping("add")
    public ResponseEntity add(@ApiParam(value = "执行工序计划表对象",required = true)@RequestBody MesPmExplainProcessPlan mesPmExplainProcessPlan){
        return ControllerUtil.returnCRUD(mesPmExplainProcessPlanService.save(mesPmExplainProcessPlan));
    }

    @ApiOperation("批量删除执行工序计划表数据")
    @GetMapping("delete")
    public ResponseEntity batchDelete(@ApiParam(value = "执行工序计划表对象ID集，多个用英文逗号隔开",required = true)@RequestParam String ids){
        return ControllerUtil.returnCRUD(mesPmExplainProcessPlanService.batchDelete(ids));
    }

    @ApiOperation("修改执行工序计划表数据")
    @PostMapping("update")
    public ResponseEntity update(@ApiParam(value = "执行工序计划表对象，对象ID必传",required = true)@RequestBody MesPmExplainProcessPlan mesPmExplainProcessPlan){
        return ControllerUtil.returnCRUD(mesPmExplainProcessPlanService.update(mesPmExplainProcessPlan));
    }

    @ApiOperation("修改执行工序计划表数据")
    @PostMapping("batchUpdate")
    public ResponseEntity batchUpdate(@ApiParam(value = "执行工序计划表对象，对象ID必传",required = true)@RequestBody List<MesPmExplainProcessPlan> mesPmExplainProcessPlanList){
        return ControllerUtil.returnCRUD(mesPmExplainProcessPlanService.batchUpdate(mesPmExplainProcessPlanList));
    }

    @PostMapping(value = "export",produces = "application/octet-stream")
    @ApiOperation(value = "导出EXCEL")
    public void export(
            @ApiParam(value = "查询条件，请参考Model说明")@RequestBody(required = false) SearchMesPmExplainProcessPlanListDTO searchMesPmExplainProcessPlanListDTO,
            @ApiParam(value = "当前页",required = false,defaultValue = "1")@RequestParam(defaultValue = "1",required = false) int startPage,
            @ApiParam(value = "显示数量",required = false,defaultValue = "10")@RequestParam(defaultValue = "10",required = false) int pageSize,
            HttpServletResponse response){
        Page<Object> page = PageHelper.startPage(startPage, pageSize);
        List<MesPmExplainProcessPlanDTO> mesPmExplainProcessPlanDTOList = mesPmExplainProcessPlanService.selectFilterAll(ControllerUtil.dynamicConditionByEntity(searchMesPmExplainProcessPlanListDTO));
        if(StringUtils.isEmpty(mesPmExplainProcessPlanDTOList)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012007);
        }
        EasyPoiUtils.exportExcel(mesPmExplainProcessPlanDTOList ,"执行工序计划表信息","执行工序计划表信息", MesPmExplainProcessPlanDTO.class, "执行工序计划表信息.xls", response);
    }
}
