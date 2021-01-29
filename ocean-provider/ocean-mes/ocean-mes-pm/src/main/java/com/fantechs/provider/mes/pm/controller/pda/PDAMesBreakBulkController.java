package com.fantechs.provider.mes.pm.controller.pda;

import com.fantechs.common.base.general.dto.mes.pm.MesPmBreakBulkDetDto;
import com.fantechs.common.base.general.dto.mes.pm.MesPmBreakBulkPrintDto;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchMesPmBreakBulk;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchMesPmBreakBulkDet;
import com.fantechs.common.base.general.entity.mes.pm.MesPmBreakBulk;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.mes.pm.service.MesPmBreakBulkDetService;
import com.fantechs.provider.mes.pm.service.MesPmBreakBulkService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Mr.Lei
 * @create 2021/1/18
 */
@RestController
@Api(tags = "pda拆批/合批作业作业")
@RequestMapping("pda/mesBreakBulk")
public class PDAMesBreakBulkController {
    @Resource
    private MesPmBreakBulkService mesPmBreakBulkService;
    @Resource
    private MesPmBreakBulkDetService mesPmBreakBulkDetService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated MesPmBreakBulk mesPmBreakBulk) {
        MesPmBreakBulk record = mesPmBreakBulkService.saveBreak(mesPmBreakBulk);
        return ControllerUtil.returnDataSuccess(record, StringUtils.isEmpty(record.getMesPmBreakBulkDets())?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<MesPmBreakBulkDetDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchMesPmBreakBulkDet searchMesPmBreakBulkDet) {
        Page<Object> page = PageHelper.startPage(searchMesPmBreakBulkDet.getStartPage(),searchMesPmBreakBulkDet.getPageSize());
        List<MesPmBreakBulkDetDto> list = mesPmBreakBulkDetService.findList(ControllerUtil.dynamicConditionByEntity(searchMesPmBreakBulkDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("pda补打")
    @PostMapping("/reprint")
    public ResponseEntity<MesPmBreakBulkPrintDto> reprint(@ApiParam(value = "查询对象")@RequestBody SearchMesPmBreakBulk searchMesPmBreakBulk){
        MesPmBreakBulkPrintDto mesPmBreakBulkPrintDto = mesPmBreakBulkService.reprint(searchMesPmBreakBulk);
        return ControllerUtil.returnDataSuccess(mesPmBreakBulkPrintDto,1);
    }
}
