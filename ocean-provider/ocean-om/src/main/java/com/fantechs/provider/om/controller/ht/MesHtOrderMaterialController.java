package com.fantechs.provider.om.controller.ht;

import com.fantechs.common.base.entity.apply.history.MesHtOrderMaterial;
import com.fantechs.common.base.dto.apply.history.MesHtOrderMaterialDTO;
import com.fantechs.common.base.dto.apply.history.SearchMesHtOrderMaterialListDTO;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.om.service.ht.MesHtOrderMaterialService;
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
 * @Date: 2021年1月9日 11:51
 * @Description: 销售订单与物料历史管理
 * @Version: 1.0
 */
@RestController
@Api(tags = "销售订单与物料历史管理",basePath = "mesHtOrderMaterial")
@RequestMapping("mesHtOrderMaterial")
@Slf4j
public class MesHtOrderMaterialController {

    @Resource
    private MesHtOrderMaterialService mesHtOrderMaterialService;

    @ApiOperation("查询销售订单与物料历史列表")
    @PostMapping("list")
    public ResponseEntity<List<MesHtOrderMaterialDTO>> list(
            @ApiParam(value = "查询条件，请参考Model说明")@RequestBody(required = false) SearchMesHtOrderMaterialListDTO searchMesHtOrderMaterialListDTO
    ){
        Page<Object> page = PageHelper.startPage(searchMesHtOrderMaterialListDTO.getStartPage(), searchMesHtOrderMaterialListDTO.getPageSize());
        List<MesHtOrderMaterialDTO> mesHtOrderMaterialDTOList = mesHtOrderMaterialService.selectFilterAll(ControllerUtil.dynamicConditionByEntity(searchMesHtOrderMaterialListDTO));
        return ControllerUtil.returnDataSuccess(mesHtOrderMaterialDTOList,(int)page.getTotal());
    }

    @ApiOperation("通过ID查询销售订单与物料历史")
    @GetMapping("one")
    public ResponseEntity<MesHtOrderMaterial> one(@ApiParam(value = "销售订单与物料历史对象ID",required = true)@RequestParam Long id){
        MesHtOrderMaterial mesHtOrderMaterial = mesHtOrderMaterialService.selectByKey(id);
        return ControllerUtil.returnDataSuccess(mesHtOrderMaterial, StringUtils.isEmpty(mesHtOrderMaterial)?0:1);
    }

    @ApiOperation("增加销售订单与物料历史数据")
    @PostMapping("add")
    public ResponseEntity add(@ApiParam(value = "销售订单与物料历史对象",required = true)@RequestBody MesHtOrderMaterial mesHtOrderMaterial){
        return ControllerUtil.returnCRUD(mesHtOrderMaterialService.save(mesHtOrderMaterial));
    }

    @ApiOperation("删除销售订单与物料历史数据")
    @GetMapping("delete")
    public ResponseEntity delete(@ApiParam(value = "销售订单与物料历史对象ID",required = true)@RequestParam Long id){
        return ControllerUtil.returnCRUD(mesHtOrderMaterialService.deleteByKey(id));
    }

    @ApiOperation("批量删除销售订单与物料历史数据")
    @GetMapping("batchDelete")
    public ResponseEntity batchDelete(@ApiParam(value = "销售订单与物料历史对象ID集，多个用英文逗号隔开",required = true)@RequestParam String ids){
        return ControllerUtil.returnCRUD(mesHtOrderMaterialService.batchDelete(ids));
    }

    @ApiOperation("修改销售订单与物料历史数据")
    @PostMapping("update")
    public ResponseEntity update(@ApiParam(value = "销售订单与物料历史对象，对象ID必传",required = true)@RequestBody MesHtOrderMaterial mesHtOrderMaterial){
        return ControllerUtil.returnCRUD(mesHtOrderMaterialService.update(mesHtOrderMaterial));
    }

    @PostMapping(value = "export",produces = "application/octet-stream")
    @ApiOperation(value = "导出EXCEL")
    public void export(
            @ApiParam(value = "查询条件，请参考Model说明")@RequestBody(required = false) SearchMesHtOrderMaterialListDTO searchMesHtOrderMaterialListDTO,
            @ApiParam(value = "当前页",required = false,defaultValue = "1")@RequestParam(defaultValue = "1",required = false) int startPage,
            @ApiParam(value = "显示数量",required = false,defaultValue = "10")@RequestParam(defaultValue = "10",required = false) int pageSize,
            HttpServletResponse response){
        Page<Object> page = PageHelper.startPage(startPage, pageSize);
        List<MesHtOrderMaterialDTO> mesHtOrderMaterialDTOList = mesHtOrderMaterialService.selectFilterAll(ControllerUtil.dynamicConditionByEntity(searchMesHtOrderMaterialListDTO));
        if(StringUtils.isEmpty(mesHtOrderMaterialDTOList)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012007);
        }
        EasyPoiUtils.exportExcel(mesHtOrderMaterialDTOList ,"销售订单与物料历史信息","销售订单与物料历史信息", MesHtOrderMaterialDTO.class, "销售订单与物料历史信息.xls", response);
    }
}
