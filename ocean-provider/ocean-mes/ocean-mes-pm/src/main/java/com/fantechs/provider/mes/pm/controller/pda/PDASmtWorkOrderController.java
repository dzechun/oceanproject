package com.fantechs.provider.mes.pm.controller.pda;

import com.fantechs.common.base.general.dto.mes.pm.SmtWorkOrderDto;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtWorkOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.mes.pm.service.SmtWorkOrderService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Auther: bingo.ren
 * @Date: 2021/1/7 15:02
 * @Description:
 * @Version: 1.0
 */
@RestController
@Api(tags = "工单管理")
@RequestMapping("pda/smtWorkOrder")
public class PDASmtWorkOrderController {

    @Autowired
    private SmtWorkOrderService smtWorkOrderService;

    @ApiOperation("工单列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SmtWorkOrderDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSmtWorkOrder searchSmtWorkOrder) {
        Page<Object> page = PageHelper.startPage(searchSmtWorkOrder.getStartPage(),searchSmtWorkOrder.getPageSize());
        List<SmtWorkOrderDto> list = smtWorkOrderService.pdaFindList(searchSmtWorkOrder);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }
}
