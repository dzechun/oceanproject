package com.fantechs.provider.om.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.mes.pm.history.MesHtScheduleDTO;
import com.fantechs.common.base.general.dto.mes.pm.history.SearchMesHtScheduleListDTO;
import com.fantechs.common.base.general.dto.om.MesScheduleDTO;
import com.fantechs.common.base.general.dto.om.SearchMesScheduleListDTO;
import com.fantechs.common.base.general.entity.om.MesSchedule;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.om.service.MesScheduleService;
import com.fantechs.provider.om.service.ht.MesHtScheduleService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
/**
 * @Auther: bingo.ren
 * @Date: 2021年1月6日 18:01
 * @Description: 工单排产表管理
 * @Version: 1.0
 */
@RestController
@Api(tags = "工单排产表管理",basePath = "mesSchedule")
@RequestMapping("mesSchedule")
@Slf4j
public class MesScheduleController {

    @Resource
    private MesScheduleService mesScheduleService;
    @Autowired
    private MesHtScheduleService mesHtScheduleService;

    @ApiOperation("查询工单排产表列表")
    @PostMapping("findList")
    public ResponseEntity<List<MesScheduleDTO>> list(
            @ApiParam(value = "查询条件，请参考Model说明")@RequestBody(required = false) SearchMesScheduleListDTO searchMesScheduleListDTO,
            @ApiParam(value = "当前页",required = false,defaultValue = "1")@RequestParam(defaultValue = "1",required = false) int startPage,
            @ApiParam(value = "显示数量",required = false,defaultValue = "10")@RequestParam(defaultValue = "10",required = false) int pageSize
    ){
        Page<Object> page = PageHelper.startPage(startPage, pageSize);
        List<MesScheduleDTO> mesScheduleDTOList = mesScheduleService.selectFilterAll(ControllerUtil.dynamicConditionByEntity(searchMesScheduleListDTO));
        return ControllerUtil.returnDataSuccess(mesScheduleDTOList,(int)page.getTotal());
    }

    @ApiOperation(value = "获取工单排产履历列表",notes = "获取工单排产履历列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<MesHtScheduleDTO>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchMesHtScheduleListDTO searchMesHtScheduleListDTO){
        Page<Object> page = PageHelper.startPage(searchMesHtScheduleListDTO.getStartPage(),searchMesHtScheduleListDTO.getPageSize());
        List<MesHtScheduleDTO> mesHtScheduleDTOList = mesHtScheduleService.selectFilterAll(ControllerUtil.dynamicConditionByEntity(searchMesHtScheduleListDTO));
        return ControllerUtil.returnDataSuccess(mesHtScheduleDTOList,(int)page.getTotal());
    }

    @ApiOperation("通过ID查询工单排产表")
    @GetMapping("detail")
    public ResponseEntity<MesSchedule> one(@ApiParam(value = "工单排产表对象ID",required = true)@RequestParam Long id){
        MesSchedule mesSchedule = mesScheduleService.selectByKey(id);
        return ControllerUtil.returnDataSuccess(mesSchedule, StringUtils.isEmpty(mesSchedule)?0:1);
    }

    @ApiOperation("增加工单排产表数据")
    @PostMapping("add")
    public ResponseEntity add(
            @ApiParam(value = "产线ID",required = true)@RequestParam Long proLineId,
            @ApiParam(value = "订单物料ID集合",required = true)@RequestBody List<Long> orderMaterialIdList){
        return ControllerUtil.returnCRUD(mesScheduleService.saveByOrderMaterialIdList(proLineId,orderMaterialIdList));
    }

    @ApiOperation("批量删除工单排产表数据")
    @GetMapping("delete")
    public ResponseEntity batchDelete(@ApiParam(value = "工单排产表对象ID集，多个用英文逗号隔开",required = true)@RequestParam String ids){
        return ControllerUtil.returnCRUD(mesScheduleService.batchDelete(ids));
    }

    @ApiOperation("修改工单排产表数据")
    @PostMapping("update")
    public ResponseEntity update(@ApiParam(value = "工单排产表对象，对象ID必传",required = true)@RequestBody MesSchedule mesSchedule){
        return ControllerUtil.returnCRUD(mesScheduleService.update(mesSchedule));
    }

    @PostMapping(value = "export",produces = "application/octet-stream")
    @ApiOperation(value = "导出EXCEL")
    public void export(
            @ApiParam(value = "查询条件，请参考Model说明")@RequestBody(required = false) SearchMesScheduleListDTO searchMesScheduleListDTO,
            @ApiParam(value = "当前页",required = false,defaultValue = "1")@RequestParam(defaultValue = "1",required = false) int startPage,
            @ApiParam(value = "显示数量",required = false,defaultValue = "10")@RequestParam(defaultValue = "10",required = false) int pageSize,
            HttpServletResponse response){
        Page<Object> page = PageHelper.startPage(startPage, pageSize);
        List<MesScheduleDTO> mesScheduleDTOList = mesScheduleService.selectFilterAll(ControllerUtil.dynamicConditionByEntity(searchMesScheduleListDTO));
        if(StringUtils.isEmpty(mesScheduleDTOList)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012007);
        }
        EasyPoiUtils.exportExcel(mesScheduleDTOList,"工单排产表信息","工单排产表信息", MesScheduleDTO.class, "工单排产表信息.xls", response);
    }
}
