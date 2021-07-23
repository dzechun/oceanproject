package com.fantechs.provider.baseapi.esop.controller;

import com.fantechs.common.base.general.entity.basic.BaseDept;
import com.fantechs.common.base.general.entity.basic.BaseProLine;
import com.fantechs.common.base.general.entity.restapi.esop.search.SearchEsop;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.baseapi.esop.service.EsopDeptService;
import com.fantechs.provider.baseapi.esop.service.EsopLineService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/07/21.
 */
@RestController
@Api(tags = "Esop同步产线信息")
@RequestMapping("/esopLine")
@Validated
public class EsopLineController {

    @Resource
    private EsopLineService esopLineService;

    @ApiOperation("同步产线")
    @PostMapping("/addLine")
    public ResponseEntity<List<BaseProLine>> addLine(@ApiParam(value = "查询对象")@RequestBody SearchEsop searchEsop) throws ParseException {
        List<BaseProLine> baseProLines = esopLineService.addLine(ControllerUtil.dynamicConditionByEntity(searchEsop));
        return ControllerUtil.returnDataSuccess(baseProLines, StringUtils.isEmpty(baseProLines) ? 0 : 1);
    }

}
