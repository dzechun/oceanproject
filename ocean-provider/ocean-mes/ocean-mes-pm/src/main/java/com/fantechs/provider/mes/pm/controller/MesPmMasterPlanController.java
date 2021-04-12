package com.fantechs.provider.mes.pm.controller;

import com.fantechs.common.base.general.dto.mes.pm.*;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchMesPmWorkOrder;
import com.fantechs.common.base.general.entity.mes.pm.MesPmMasterPlan;
import com.fantechs.provider.mes.pm.service.MesPmMasterPlanService;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchMesPmMasterPlanListDTO;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.mes.pm.service.MesPmWorkOrderService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * @Auther: bingo.ren
 * @Date: 2021年1月15日 15:52
 * @Description: 总计划表（月计划表）管理
 * @Version: 1.0
 */
@RestController
@Api(tags = "总计划表（月计划表）管理",basePath = "mesPmMasterPlan")
@RequestMapping("/mesPmMasterPlan")
@Slf4j
public class MesPmMasterPlanController {

    @Resource
    private MesPmMasterPlanService mesPmMasterPlanService;
    @Resource
    private MesPmWorkOrderService mesPmWorkOrderService;

    @ApiOperation("查询总计划表（月计划表）列表")
    @PostMapping("/findList")
    public ResponseEntity<List<MesPmMasterPlanDTO>> list(
            @ApiParam(value = "查询条件，请参考Model说明")@RequestBody(required = false) SearchMesPmMasterPlanListDTO searchMesPmMasterPlanListDTO
    ){
        Page<Object> page = PageHelper.startPage(searchMesPmMasterPlanListDTO.getStartPage(), searchMesPmMasterPlanListDTO.getPageSize());
        List<MesPmMasterPlanDTO> mesPmMasterPlanDTOList = mesPmMasterPlanService.selectFilterAll(ControllerUtil.dynamicConditionByEntity(searchMesPmMasterPlanListDTO));
        return ControllerUtil.returnDataSuccess(mesPmMasterPlanDTOList,(int)page.getTotal());
    }

    @ApiOperation("通过ID查询总计划表（月计划表）")
    @GetMapping("/detail")
    public ResponseEntity<MesPmMasterPlan> one(@ApiParam(value = "总计划表（月计划表）对象ID",required = true)@RequestParam Long id){
        MesPmMasterPlan mesPmMasterPlan = mesPmMasterPlanService.selectByKey(id);
        return ControllerUtil.returnDataSuccess(mesPmMasterPlan, StringUtils.isEmpty(mesPmMasterPlan)?0:1);
    }

    @ApiOperation("增加总计划表（月计划表）数据")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "总计划表（月计划表）对象",required = true)@RequestBody MesPmMasterPlan mesPmMasterPlan){
        return ControllerUtil.returnCRUD(mesPmMasterPlanService.save(mesPmMasterPlan));
    }

    @ApiOperation("增加或更新总计划表（月计划表）数据及工序计划数据")
    @PostMapping("/save")
    public ResponseEntity save(@ApiParam(value = "对象",required = true)@RequestBody SaveMesPmMasterPlanDTO saveMesPmMasterPlanDTO){
        return ControllerUtil.returnCRUD(mesPmMasterPlanService.save(saveMesPmMasterPlanDTO));
    }

    @ApiOperation("批量删除总计划表（月计划表）数据")
    @GetMapping("/delete")
    public ResponseEntity batchDelete(@ApiParam(value = "总计划表（月计划表）对象ID集，多个用英文逗号隔开",required = true)@RequestParam String ids){
        return ControllerUtil.returnCRUD(mesPmMasterPlanService.batchDelete(ids));
    }

    @ApiOperation("修改总计划表（月计划表）数据")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "总计划表（月计划表）对象，对象ID必传",required = true)@RequestBody MesPmMasterPlan mesPmMasterPlan){
        return ControllerUtil.returnCRUD(mesPmMasterPlanService.update(mesPmMasterPlan));
    }

    @ApiOperation("总计划表（月计划表）转执行计划（周/日计划）")
    @PostMapping("/turnExplainPlan")
    public ResponseEntity turnExplainPlan(@ApiParam(value = "对象",required = true)@RequestBody TurnExplainPlanDTO turnExplainPlanDTO){
        return ControllerUtil.returnCRUD(mesPmMasterPlanService.turnExplainPlan(turnExplainPlanDTO));
    }

    @ApiOperation("总计划表（月计划表）转流程卡")
    @PostMapping("/turnWorkOrderCardPool")
    public ResponseEntity turnWorkOrderCardPool(@ApiParam(value = "对象",required = true)@RequestBody TurnWorkOrderCardPoolDTO turnWorkOrderCardPoolDTO){
        return ControllerUtil.returnCRUD(mesPmMasterPlanService.turnWorkOrderCardPool(turnWorkOrderCardPoolDTO));
    }

    @ApiOperation("输出工单相关信息（总计划打印A4）")
    @GetMapping("/masterPlanPrintWorkOrder")
    public ResponseEntity<MasterPlanPrintWorkOrderDTO> masterPlanPrintWorkOrder(@ApiParam(value = "总计划表（月计划表）对象ID",required = true)@RequestParam Long masterPlanId){
        MasterPlanPrintWorkOrderDTO masterPlanPrintWorkOrderDTO = mesPmMasterPlanService.masterPlanPrintWorkOrder(masterPlanId);
        return ControllerUtil.returnDataSuccess(masterPlanPrintWorkOrderDTO,StringUtils.isEmpty(masterPlanPrintWorkOrderDTO)?0:1);
    }

    @PostMapping(value = "/export",produces = "application/octet-stream")
    @ApiOperation(value = "导出EXCEL")
    public void export(
            @ApiParam(value = "查询条件，请参考Model说明")@RequestBody(required = false) SearchMesPmMasterPlanListDTO searchMesPmMasterPlanListDTO,
            @ApiParam(value = "当前页",required = false,defaultValue = "1")@RequestParam(defaultValue = "1",required = false) int startPage,
            @ApiParam(value = "显示数量",required = false,defaultValue = "10")@RequestParam(defaultValue = "10",required = false) int pageSize,
            HttpServletResponse response){
        Page<Object> page = PageHelper.startPage(startPage, pageSize);
        List<MesPmMasterPlanDTO> mesPmMasterPlanDTOList = mesPmMasterPlanService.selectFilterAll(ControllerUtil.dynamicConditionByEntity(searchMesPmMasterPlanListDTO));
        if(StringUtils.isEmpty(mesPmMasterPlanDTOList)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012007);
        }
        EasyPoiUtils.exportExcel(mesPmMasterPlanDTOList ,"总计划表（月计划表）信息","总计划表（月计划表）信息", MesPmMasterPlanDTO.class, "总计划表（月计划表）信息.xls", response);
    }

    @PostMapping(value = "/import")
    @ApiOperation(value = "导入EXCEL",notes = "导入EXCEL")
    public ResponseEntity<String> importExcel(@ApiParam(value ="输入excel文件",required = true)
                                      @RequestPart(value="file") MultipartFile file) throws IOException {
        List<MesPmMasterPlanDTO> mesPmMasterPlanDTOList;
        try{
            mesPmMasterPlanDTOList = EasyPoiUtils.importExcel(file, MesPmMasterPlanDTO.class);
        }catch (Exception e){
            throw new BizErrorException("请选择正确的模板");
        }
        if(StringUtils.isEmpty(mesPmMasterPlanDTOList)){
            return ControllerUtil.returnCRUD(0);
        }
        StringBuffer errorWorkOrderCode=new StringBuffer();
        for (MesPmMasterPlanDTO mesPmMasterPlanDTO : mesPmMasterPlanDTOList) {
            //=====校验数据正确性
            if(StringUtils.isEmpty(
                    mesPmMasterPlanDTO.getWorkOrderCode(),
                    mesPmMasterPlanDTO.getProductQty(),
                    mesPmMasterPlanDTO.getPlanedStartDate(),
                    mesPmMasterPlanDTO.getPlanedEndDate())){
                throw new BizErrorException("缺少必要数据（工单编码/计划生产总数及排产数/计划开工完工时间）");
            }
            SearchMesPmWorkOrder searchMesPmWorkOrder = new SearchMesPmWorkOrder();
            searchMesPmWorkOrder.setWorkOrderCode(mesPmMasterPlanDTO.getWorkOrderCode());
            List<MesPmWorkOrderDto> smtWorkOrderDtoList = mesPmWorkOrderService.findList(searchMesPmWorkOrder);
            if(StringUtils.isEmpty(smtWorkOrderDtoList) || smtWorkOrderDtoList.size()>1){
                throw new BizErrorException("工单编号未找到或工单编号不唯一");
            }
            //=====
            MesPmWorkOrderDto smtWorkOrderDto = smtWorkOrderDtoList.get(0);
            MesPmMasterPlan mesPmMasterPlan = new MesPmMasterPlan();
            mesPmMasterPlan.setWorkOrderId(smtWorkOrderDto.getWorkOrderId());
            mesPmMasterPlan.setProLineId(smtWorkOrderDto.getProLineId());
            mesPmMasterPlan.setWorkOrderQuantity(smtWorkOrderDto.getWorkOrderQuantity());
            mesPmMasterPlan.setProductQty(mesPmMasterPlanDTO.getProductQty());
            mesPmMasterPlan.setNoScheduleQty(mesPmMasterPlan.getProductQty());
            mesPmMasterPlan.setPlanedStartDate(mesPmMasterPlanDTO.getPlanedStartDate());
            mesPmMasterPlan.setPlanedEndDate(mesPmMasterPlanDTO.getPlanedEndDate());
            if(mesPmMasterPlanService.save(mesPmMasterPlan)<=0){
                errorWorkOrderCode.append( mesPmMasterPlanDTO.getWorkOrderCode()+",");
            }
        }
        return ControllerUtil.returnDataSuccess(errorWorkOrderCode.toString(), 1);
    }
}
