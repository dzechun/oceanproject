package com.fantechs.provider.om.controller.ht;

import com.fantechs.common.base.general.entity.mes.pm.history.MesHtSchedule;
import com.fantechs.common.base.general.dto.mes.pm.history.MesHtScheduleDTO;
import com.fantechs.common.base.general.dto.mes.pm.history.SearchMesHtScheduleListDTO;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.om.service.ht.MesHtScheduleService;
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
 * @Date: 2021年1月9日 14:18
 * @Description: 排产单履历表管理
 * @Version: 1.0
 */
@RestController
@Api(tags = "排产单履历表管理",basePath = "mesHtSchedule")
@RequestMapping("mesHtSchedule")
@Slf4j
public class MesHtScheduleController {

    @Resource
    private MesHtScheduleService mesHtScheduleService;

    @ApiOperation("查询排产单履历表列表")
    @PostMapping("list")
    public ResponseEntity<List<MesHtScheduleDTO>> list(
            @ApiParam(value = "查询条件，请参考Model说明")@RequestBody(required = false) SearchMesHtScheduleListDTO searchMesHtScheduleListDTO
    ){
        Page<Object> page = PageHelper.startPage(searchMesHtScheduleListDTO.getStartPage(), searchMesHtScheduleListDTO.getPageSize());
        List<MesHtScheduleDTO> mesHtScheduleDTOList = mesHtScheduleService.selectFilterAll(ControllerUtil.dynamicConditionByEntity(searchMesHtScheduleListDTO));
        return ControllerUtil.returnDataSuccess(mesHtScheduleDTOList,(int)page.getTotal());
    }

    @ApiOperation("通过ID查询排产单履历表")
    @GetMapping("one")
    public ResponseEntity<MesHtSchedule> one(@ApiParam(value = "排产单履历表对象ID",required = true)@RequestParam Long id){
        MesHtSchedule mesHtSchedule = mesHtScheduleService.selectByKey(id);
        return ControllerUtil.returnDataSuccess(mesHtSchedule, StringUtils.isEmpty(mesHtSchedule)?0:1);
    }

    @ApiOperation("增加排产单履历表数据")
    @PostMapping("add")
    public ResponseEntity add(@ApiParam(value = "排产单履历表对象",required = true)@RequestBody MesHtSchedule mesHtSchedule){
        return ControllerUtil.returnCRUD(mesHtScheduleService.save(mesHtSchedule));
    }

    @ApiOperation("删除排产单履历表数据")
    @GetMapping("delete")
    public ResponseEntity delete(@ApiParam(value = "排产单履历表对象ID",required = true)@RequestParam Long id){
        return ControllerUtil.returnCRUD(mesHtScheduleService.deleteByKey(id));
    }

    @ApiOperation("批量删除排产单履历表数据")
    @GetMapping("batchDelete")
    public ResponseEntity batchDelete(@ApiParam(value = "排产单履历表对象ID集，多个用英文逗号隔开",required = true)@RequestParam String ids){
        return ControllerUtil.returnCRUD(mesHtScheduleService.batchDelete(ids));
    }

    @ApiOperation("修改排产单履历表数据")
    @PostMapping("update")
    public ResponseEntity update(@ApiParam(value = "排产单履历表对象，对象ID必传",required = true)@RequestBody MesHtSchedule mesHtSchedule){
        return ControllerUtil.returnCRUD(mesHtScheduleService.update(mesHtSchedule));
    }

    @PostMapping(value = "export",produces = "application/octet-stream")
    @ApiOperation(value = "导出EXCEL")
    public void export(
            @ApiParam(value = "查询条件，请参考Model说明")@RequestBody(required = false) SearchMesHtScheduleListDTO searchMesHtScheduleListDTO,
            @ApiParam(value = "当前页",required = false,defaultValue = "1")@RequestParam(defaultValue = "1",required = false) int startPage,
            @ApiParam(value = "显示数量",required = false,defaultValue = "10")@RequestParam(defaultValue = "10",required = false) int pageSize,
            HttpServletResponse response){
        Page<Object> page = PageHelper.startPage(startPage, pageSize);
        List<MesHtScheduleDTO> mesHtScheduleDTOList = mesHtScheduleService.selectFilterAll(ControllerUtil.dynamicConditionByEntity(searchMesHtScheduleListDTO));
        if(StringUtils.isEmpty(mesHtScheduleDTOList)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012007);
        }
        EasyPoiUtils.exportExcel(mesHtScheduleDTOList ,"排产单履历表信息","排产单履历表信息", MesHtScheduleDTO.class, "排产单履历表信息.xls", response);
    }
}
