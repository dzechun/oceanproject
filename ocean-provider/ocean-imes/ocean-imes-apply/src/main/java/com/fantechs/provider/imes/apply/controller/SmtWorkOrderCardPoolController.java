package com.fantechs.provider.imes.apply.controller;

import com.fantechs.common.base.dto.apply.SmtWorkOrderCardPoolDto;
import com.fantechs.common.base.entity.apply.SmtWorkOrderCardPool;
import com.fantechs.common.base.entity.apply.search.SearchSmtWorkOrderCardPool;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.apply.service.SmtWorkOrderCardPoolService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by wcz on 2020/11/25.
 */
@RestController
@Api(tags = "smtWorkOrderCardPool控制器")
@RequestMapping("/smtWorkOrderCardPool")
@Validated
public class SmtWorkOrderCardPoolController {

    @Autowired
    private SmtWorkOrderCardPoolService smtWorkOrderCardPoolService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody  SmtWorkOrderCardPool smtWorkOrderCardPool) {
        return ControllerUtil.returnCRUD(smtWorkOrderCardPoolService.save(smtWorkOrderCardPool));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(smtWorkOrderCardPoolService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody SmtWorkOrderCardPool smtWorkOrderCardPool) {
        return ControllerUtil.returnCRUD(smtWorkOrderCardPoolService.update(smtWorkOrderCardPool));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SmtWorkOrderCardPool> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        SmtWorkOrderCardPool  smtWorkOrderCardPool = smtWorkOrderCardPoolService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(smtWorkOrderCardPool,StringUtils.isEmpty(smtWorkOrderCardPool)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SmtWorkOrderCardPoolDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSmtWorkOrderCardPool searchSmtWorkOrderCardPool) {
        Page<Object> page = PageHelper.startPage(searchSmtWorkOrderCardPool.getStartPage(),searchSmtWorkOrderCardPool.getPageSize());
        List<SmtWorkOrderCardPoolDto> list = smtWorkOrderCardPoolService.findList(searchSmtWorkOrderCardPool);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }
}
