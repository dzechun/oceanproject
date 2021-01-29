package com.fantechs.provider.om.controller.ht;

import com.fantechs.common.base.general.entity.mes.pm.history.MesHtOrderMaterial;
import com.fantechs.common.base.general.dto.mes.pm.history.MesHtOrderMaterialDTO;
import com.fantechs.common.base.general.dto.mes.pm.history.SearchMesHtOrderMaterialListDTO;
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
    @PostMapping("findList")
    public ResponseEntity<List<MesHtOrderMaterialDTO>> list(
            @ApiParam(value = "查询条件，请参考Model说明")@RequestBody(required = false) SearchMesHtOrderMaterialListDTO searchMesHtOrderMaterialListDTO
    ){
        Page<Object> page = PageHelper.startPage(searchMesHtOrderMaterialListDTO.getStartPage(), searchMesHtOrderMaterialListDTO.getPageSize());
        List<MesHtOrderMaterialDTO> mesHtOrderMaterialDTOList = mesHtOrderMaterialService.selectFilterAll(ControllerUtil.dynamicConditionByEntity(searchMesHtOrderMaterialListDTO));
        return ControllerUtil.returnDataSuccess(mesHtOrderMaterialDTOList,(int)page.getTotal());
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
