package com.fantechs.provider.om.controller.ht;

import com.fantechs.common.base.general.entity.om.SmtHtOrder;
import com.fantechs.common.base.general.dto.mes.pm.history.SmtHtOrderDTO;
import com.fantechs.common.base.general.dto.mes.pm.history.SearchSmtHtOrderListDTO;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.om.service.ht.SmtHtOrderService;
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
 * @Date: 2021年1月8日 16:22
 * @Description: 订单历史信息表管理
 * @Version: 1.0
 */
@RestController
@Api(tags = "订单历史信息表管理",basePath = "smtHtOrder")
@RequestMapping("smtHtOrder")
@Slf4j
public class SmtHtOrderController {

    @Resource
    private SmtHtOrderService smtHtOrderService;

    @ApiOperation("查询订单历史信息表列表")
    @PostMapping("findList")
    public ResponseEntity<List<SmtHtOrderDTO>> list(
            @ApiParam(value = "查询条件，请参考Model说明")@RequestBody(required = false) SearchSmtHtOrderListDTO searchSmtHtOrderListDTO
    ){
        Page<Object> page = PageHelper.startPage(searchSmtHtOrderListDTO.getStartPage(), searchSmtHtOrderListDTO.getPageSize());
        List<SmtHtOrderDTO> smtHtOrderDTOList = smtHtOrderService.selectFilterAll(ControllerUtil.dynamicConditionByEntity(searchSmtHtOrderListDTO));
        return ControllerUtil.returnDataSuccess(smtHtOrderDTOList,(int)page.getTotal());
    }

    @PostMapping(value = "export",produces = "application/octet-stream")
    @ApiOperation(value = "导出EXCEL")
    public void export(
            @ApiParam(value = "查询条件，请参考Model说明")@RequestBody(required = false) SearchSmtHtOrderListDTO searchSmtHtOrderListDTO,
            @ApiParam(value = "当前页",required = false,defaultValue = "1")@RequestParam(defaultValue = "1",required = false) int startPage,
            @ApiParam(value = "显示数量",required = false,defaultValue = "10")@RequestParam(defaultValue = "10",required = false) int pageSize,
            HttpServletResponse response){
        Page<Object> page = PageHelper.startPage(startPage, pageSize);
        List<SmtHtOrderDTO> smtHtOrderDTOList = smtHtOrderService.selectFilterAll(ControllerUtil.dynamicConditionByEntity(searchSmtHtOrderListDTO));
        if(StringUtils.isEmpty(smtHtOrderDTOList)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012007);
        }
        EasyPoiUtils.exportExcel(smtHtOrderDTOList ,"订单历史信息表信息","订单历史信息表信息", SmtHtOrderDTO.class, "订单历史信息表信息.xls", response);
    }
}
