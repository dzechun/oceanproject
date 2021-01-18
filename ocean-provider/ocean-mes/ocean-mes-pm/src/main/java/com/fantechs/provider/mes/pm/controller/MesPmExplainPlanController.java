package com.fantechs.provider.mes.pm.controller;

import com.fantechs.common.base.general.dto.mes.pm.SaveMesPmExplainPlanDTO;
import com.fantechs.common.base.general.dto.mes.pm.SaveMesPmMasterPlanDTO;
import com.fantechs.common.base.general.entity.mes.pm.MesPmExplainPlan;
import com.fantechs.common.base.general.dto.mes.pm.MesPmExplainPlanDTO;
import com.fantechs.provider.mes.pm.service.MesPmExplainPlanService;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchMesPmExplainPlanListDTO;
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
 * @Date: 2021年1月15日 15:58
 * @Description: 执行计划（周/日计划表）管理
 * @Version: 1.0
 */
@RestController
@Api(tags = "执行计划（周/日计划表）管理",basePath = "mesPmExplainPlan")
@RequestMapping("mesPmExplainPlan")
@Slf4j
public class MesPmExplainPlanController {

    @Resource
    private MesPmExplainPlanService mesPmExplainPlanService;

    @ApiOperation("查询执行计划（周/日计划表）列表")
    @PostMapping("findAllList")
    public ResponseEntity<List<MesPmExplainPlanDTO>> list(
            @ApiParam(value = "查询条件，请参考Model说明")@RequestBody(required = false) SearchMesPmExplainPlanListDTO searchMesPmExplainPlanListDTO
    ){
        Page<Object> page = PageHelper.startPage(searchMesPmExplainPlanListDTO.getStartPage(), searchMesPmExplainPlanListDTO.getPageSize());
        List<MesPmExplainPlanDTO> mesPmExplainPlanDTOList = mesPmExplainPlanService.selectFilterAll(ControllerUtil.dynamicConditionByEntity(searchMesPmExplainPlanListDTO));
        return ControllerUtil.returnDataSuccess(mesPmExplainPlanDTOList,(int)page.getTotal());
    }

    @ApiOperation("通过ID查询执行计划（周/日计划表）")
    @GetMapping("detail")
    public ResponseEntity<MesPmExplainPlan> one(@ApiParam(value = "执行计划（周/日计划表）对象ID",required = true)@RequestParam Long id){
        MesPmExplainPlan mesPmExplainPlan = mesPmExplainPlanService.selectByKey(id);
        return ControllerUtil.returnDataSuccess(mesPmExplainPlan, StringUtils.isEmpty(mesPmExplainPlan)?0:1);
    }

    @ApiOperation("增加执行计划（周/日计划表）数据")
    @PostMapping("add")
    public ResponseEntity add(@ApiParam(value = "执行计划（周/日计划表）对象",required = true)@RequestBody MesPmExplainPlan mesPmExplainPlan){
        return ControllerUtil.returnCRUD(mesPmExplainPlanService.save(mesPmExplainPlan));
    }

    @ApiOperation("增加或更新执行计划（周/日计划表）数据及工序计划数据")
    @PostMapping("save")
    public ResponseEntity save(@ApiParam(value = "对象",required = true)@RequestBody SaveMesPmExplainPlanDTO saveMesPmExplainPlanDTO){
        return ControllerUtil.returnCRUD(mesPmExplainPlanService.save(saveMesPmExplainPlanDTO));
    }

    @ApiOperation("删除执行计划（周/日计划表）数据")
    @GetMapping("delete")
    public ResponseEntity delete(@ApiParam(value = "执行计划（周/日计划表）对象ID",required = true)@RequestParam Long id){
        return ControllerUtil.returnCRUD(mesPmExplainPlanService.deleteByKey(id));
    }

    @ApiOperation("批量删除执行计划（周/日计划表）数据")
    @GetMapping("batchDelete")
    public ResponseEntity batchDelete(@ApiParam(value = "执行计划（周/日计划表）对象ID集，多个用英文逗号隔开",required = true)@RequestParam String ids){
        return ControllerUtil.returnCRUD(mesPmExplainPlanService.batchDelete(ids));
    }

    @ApiOperation("修改执行计划（周/日计划表）数据")
    @PostMapping("update")
    public ResponseEntity update(@ApiParam(value = "执行计划（周/日计划表）对象，对象ID必传",required = true)@RequestBody MesPmExplainPlan mesPmExplainPlan){
        return ControllerUtil.returnCRUD(mesPmExplainPlanService.update(mesPmExplainPlan));
    }

    @PostMapping(value = "export",produces = "application/octet-stream")
    @ApiOperation(value = "导出EXCEL")
    public void export(
            @ApiParam(value = "查询条件，请参考Model说明")@RequestBody(required = false) SearchMesPmExplainPlanListDTO searchMesPmExplainPlanListDTO,
            @ApiParam(value = "当前页",required = false,defaultValue = "1")@RequestParam(defaultValue = "1",required = false) int startPage,
            @ApiParam(value = "显示数量",required = false,defaultValue = "10")@RequestParam(defaultValue = "10",required = false) int pageSize,
            HttpServletResponse response){
        Page<Object> page = PageHelper.startPage(startPage, pageSize);
        List<MesPmExplainPlanDTO> mesPmExplainPlanDTOList = mesPmExplainPlanService.selectFilterAll(ControllerUtil.dynamicConditionByEntity(searchMesPmExplainPlanListDTO));
        if(StringUtils.isEmpty(mesPmExplainPlanDTOList)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012007);
        }
        EasyPoiUtils.exportExcel(mesPmExplainPlanDTOList ,"执行计划（周/日计划表）信息","执行计划（周/日计划表）信息", MesPmExplainPlanDTO.class, "执行计划（周/日计划表）信息.xls", response);
    }
}
