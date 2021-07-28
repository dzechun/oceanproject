package com.fantechs.provider.smt.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.smt.SmtSolderPasteJobDto;
import com.fantechs.common.base.general.entity.smt.SmtSolderPasteJob;
import com.fantechs.common.base.general.entity.smt.search.SearchSmtSolderPasteJob;
import com.fantechs.provider.smt.service.SmtSolderPasteJobService;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/07/27.
 */
@RestController
@Api(tags = "锡膏作业")
@RequestMapping("/smtSolderPasteJob")
@Validated
public class SmtSolderPasteJobController {

    @Resource
    private SmtSolderPasteJobService smtSolderPasteJobService;

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SmtSolderPasteJobDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSmtSolderPasteJob searchSmtSolderPasteJob) {
        Page<Object> page = PageHelper.startPage(searchSmtSolderPasteJob.getStartPage(),searchSmtSolderPasteJob.getPageSize());
        List<SmtSolderPasteJobDto> list = smtSolderPasteJobService.findList(ControllerUtil.dynamicConditionByEntity(searchSmtSolderPasteJob));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }
}
