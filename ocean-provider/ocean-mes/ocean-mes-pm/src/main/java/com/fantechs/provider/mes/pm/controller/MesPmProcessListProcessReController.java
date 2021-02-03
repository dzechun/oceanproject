package com.fantechs.provider.mes.pm.controller;

import com.fantechs.common.base.general.dto.mes.pm.SaveProcessListProcessReDTO;
import com.fantechs.common.base.general.entity.mes.pm.MesPmProcessListProcessRe;
import com.fantechs.common.base.general.dto.mes.pm.MesPmProcessListProcessReDTO;
import com.fantechs.provider.mes.pm.service.MesPmProcessListProcessReService;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchMesPmProcessListProcessReListDTO;
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
 * @Date: 2021年1月19日 20:18
 * @Description: 流程单工序退回表管理
 * @Version: 1.0
 */
@RestController
@Api(tags = "流程单工序退回表管理",basePath = "mesPmProcessListProcessRe")
@RequestMapping("mesPmProcessListProcessRe")
@Slf4j
public class MesPmProcessListProcessReController {

    @Resource
    private MesPmProcessListProcessReService mesPmProcessListProcessReService;

    @ApiOperation("查询流程单工序退回表列表")
    @PostMapping("findList")
    public ResponseEntity<List<MesPmProcessListProcessReDTO>> list(
            @ApiParam(value = "查询条件，请参考Model说明")@RequestBody(required = false) SearchMesPmProcessListProcessReListDTO searchMesPmProcessListProcessReListDTO
    ){
        Page<Object> page = PageHelper.startPage(searchMesPmProcessListProcessReListDTO.getStartPage(), searchMesPmProcessListProcessReListDTO.getPageSize());
        List<MesPmProcessListProcessReDTO> mesPmProcessListProcessReDTOList = mesPmProcessListProcessReService.selectFilterAll(ControllerUtil.dynamicConditionByEntity(searchMesPmProcessListProcessReListDTO));
        return ControllerUtil.returnDataSuccess(mesPmProcessListProcessReDTOList,(int)page.getTotal());
    }

    @ApiOperation("通过ID查询流程单工序退回表")
    @GetMapping("detail")
    public ResponseEntity<MesPmProcessListProcessRe> one(@ApiParam(value = "流程单工序退回表对象ID",required = true)@RequestParam Long id){
        MesPmProcessListProcessRe mesPmProcessListProcessRe = mesPmProcessListProcessReService.selectByKey(id);
        return ControllerUtil.returnDataSuccess(mesPmProcessListProcessRe, StringUtils.isEmpty(mesPmProcessListProcessRe)?0:1);
    }

    @ApiOperation("增加流程单工序退回表数据")
    @PostMapping("add")
    public ResponseEntity add(@ApiParam(value = "流程单工序退回表对象",required = true)@RequestBody MesPmProcessListProcessRe mesPmProcessListProcessRe){
        return ControllerUtil.returnCRUD(mesPmProcessListProcessReService.save(mesPmProcessListProcessRe));
    }

    @ApiOperation("增加流程单工序退回表数据")
    @PostMapping("save")
    public ResponseEntity add(
            @ApiParam(value = "流程单工序退回表对象",required = true)@RequestBody SaveProcessListProcessReDTO saveProcessListProcessReDTO){
        return ControllerUtil.returnCRUD(mesPmProcessListProcessReService.save(saveProcessListProcessReDTO));
    }

    @ApiOperation("批量删除流程单工序退回表数据")
    @GetMapping("delete")
    public ResponseEntity batchDelete(@ApiParam(value = "流程单工序退回表对象ID集，多个用英文逗号隔开",required = true)@RequestParam String ids){
        return ControllerUtil.returnCRUD(mesPmProcessListProcessReService.batchDelete(ids));
    }

    @ApiOperation("修改流程单工序退回表数据")
    @PostMapping("update")
    public ResponseEntity update(@ApiParam(value = "流程单工序退回表对象，对象ID必传",required = true)@RequestBody MesPmProcessListProcessRe mesPmProcessListProcessRe){
        return ControllerUtil.returnCRUD(mesPmProcessListProcessReService.update(mesPmProcessListProcessRe));
    }

    @PostMapping(value = "export",produces = "application/octet-stream")
    @ApiOperation(value = "导出EXCEL")
    public void export(
            @ApiParam(value = "查询条件，请参考Model说明")@RequestBody(required = false) SearchMesPmProcessListProcessReListDTO searchMesPmProcessListProcessReListDTO,
            HttpServletResponse response){
        List<MesPmProcessListProcessReDTO> mesPmProcessListProcessReDTOList = mesPmProcessListProcessReService.selectFilterAll(ControllerUtil.dynamicConditionByEntity(searchMesPmProcessListProcessReListDTO));
        if(StringUtils.isEmpty(mesPmProcessListProcessReDTOList)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012007);
        }
        EasyPoiUtils.exportExcel(mesPmProcessListProcessReDTOList ,"流程单工序退回表信息","流程单工序退回表信息", MesPmProcessListProcessReDTO.class, "流程单工序退回表信息.xls", response);
    }
}
