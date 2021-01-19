package com.fantechs.provider.mes.pm.controller;

import com.fantechs.common.base.general.entity.mes.pm.MesPmProcessPlan;
import com.fantechs.common.base.general.dto.mes.pm.MesPmProcessPlanDTO;
import com.fantechs.provider.mes.pm.service.MesPmProcessPlanService;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchMesPmProcessPlanListDTO;
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
 * @Date: 2021年1月15日 16:26
 * @Description: 工序计划表管理
 * @Version: 1.0
 */
@RestController
@Api(tags = "工序计划表管理",basePath = "mesPmProcessPlan")
@RequestMapping("mesPmProcessPlan")
@Slf4j
public class MesPmProcessPlanController {

    @Resource
    private MesPmProcessPlanService mesPmProcessPlanService;

    @ApiOperation("查询工序计划表列表")
    @PostMapping("findAllList")
    public ResponseEntity<List<MesPmProcessPlanDTO>> list(
            @ApiParam(value = "查询条件，请参考Model说明")@RequestBody(required = false) SearchMesPmProcessPlanListDTO searchMesPmProcessPlanListDTO
    ){
        Page<Object> page = PageHelper.startPage(searchMesPmProcessPlanListDTO.getStartPage(), searchMesPmProcessPlanListDTO.getPageSize());
        List<MesPmProcessPlanDTO> mesPmProcessPlanDTOList = mesPmProcessPlanService.selectFilterAll(ControllerUtil.dynamicConditionByEntity(searchMesPmProcessPlanListDTO));
        return ControllerUtil.returnDataSuccess(mesPmProcessPlanDTOList,(int)page.getTotal());
    }

    @ApiOperation("通过ID查询工序计划表")
    @GetMapping("detail")
    public ResponseEntity<MesPmProcessPlan> one(@ApiParam(value = "工序计划表对象ID",required = true)@RequestParam Long id){
        MesPmProcessPlan mesPmProcessPlan = mesPmProcessPlanService.selectByKey(id);
        return ControllerUtil.returnDataSuccess(mesPmProcessPlan, StringUtils.isEmpty(mesPmProcessPlan)?0:1);
    }

    @ApiOperation("增加工序计划表数据")
    @PostMapping("add")
    public ResponseEntity add(@ApiParam(value = "工序计划表对象",required = true)@RequestBody MesPmProcessPlan mesPmProcessPlan){
        return ControllerUtil.returnCRUD(mesPmProcessPlanService.save(mesPmProcessPlan));
    }

    @ApiOperation("删除工序计划表数据")
    @GetMapping("delete")
    public ResponseEntity delete(@ApiParam(value = "工序计划表对象ID",required = true)@RequestParam Long id){
        return ControllerUtil.returnCRUD(mesPmProcessPlanService.deleteByKey(id));
    }

    @ApiOperation("批量删除工序计划表数据")
    @GetMapping("batchDelete")
    public ResponseEntity batchDelete(@ApiParam(value = "工序计划表对象ID集，多个用英文逗号隔开",required = true)@RequestParam String ids){
        return ControllerUtil.returnCRUD(mesPmProcessPlanService.batchDelete(ids));
    }

    @ApiOperation("修改工序计划表数据")
    @PostMapping("update")
    public ResponseEntity update(@ApiParam(value = "工序计划表对象，对象ID必传",required = true)@RequestBody MesPmProcessPlan mesPmProcessPlan){
        return ControllerUtil.returnCRUD(mesPmProcessPlanService.update(mesPmProcessPlan));
    }

    @PostMapping(value = "export",produces = "application/octet-stream")
    @ApiOperation(value = "导出EXCEL")
    public void export(
            @ApiParam(value = "查询条件，请参考Model说明")@RequestBody(required = false) SearchMesPmProcessPlanListDTO searchMesPmProcessPlanListDTO,
            @ApiParam(value = "当前页",required = false,defaultValue = "1")@RequestParam(defaultValue = "1",required = false) int startPage,
            @ApiParam(value = "显示数量",required = false,defaultValue = "10")@RequestParam(defaultValue = "10",required = false) int pageSize,
            HttpServletResponse response){
        Page<Object> page = PageHelper.startPage(startPage, pageSize);
        List<MesPmProcessPlanDTO> mesPmProcessPlanDTOList = mesPmProcessPlanService.selectFilterAll(ControllerUtil.dynamicConditionByEntity(searchMesPmProcessPlanListDTO));
        if(StringUtils.isEmpty(mesPmProcessPlanDTOList)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012007);
        }
        EasyPoiUtils.exportExcel(mesPmProcessPlanDTOList ,"工序计划表信息","工序计划表信息", MesPmProcessPlanDTO.class, "工序计划表信息.xls", response);
    }
}
