package com.fantechs.provider.base.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseProductProcessReMDto;
import com.fantechs.common.base.general.entity.basic.BaseProductProcessReM;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProductProcessReM;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseProductProcessReMService;
import com.fantechs.provider.base.vo.BaseProductProcessReMVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/04/28.
 */
@RestController
@Api(tags = "物料工序关系控制器")
@RequestMapping("/baseProductProcessReM")
@Validated
public class BaseProductProcessReMController {

    @Resource
    private BaseProductProcessReMService baseProductProcessReMService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated BaseProductProcessReM baseProductProcessReM) {
        return ControllerUtil.returnCRUD(baseProductProcessReMService.save(baseProductProcessReM));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseProductProcessReMService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=BaseProductProcessReM.update.class) BaseProductProcessReM baseProductProcessReM) {
        return ControllerUtil.returnCRUD(baseProductProcessReMService.update(baseProductProcessReM));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseProductProcessReM> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BaseProductProcessReM  baseProductProcessReM = baseProductProcessReMService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseProductProcessReM,StringUtils.isEmpty(baseProductProcessReM)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseProductProcessReMVo>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseProductProcessReM searchBaseProductProcessReM) {
        Page<Object> page = PageHelper.startPage(searchBaseProductProcessReM.getStartPage(),searchBaseProductProcessReM.getPageSize());
        List<BaseProductProcessReMVo> list = baseProductProcessReMService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseProductProcessReM));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseProductProcessReMVo>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchBaseProductProcessReM searchBaseProductProcessReM) {
        Page<Object> page = PageHelper.startPage(searchBaseProductProcessReM.getStartPage(),searchBaseProductProcessReM.getPageSize());
        List<BaseProductProcessReMVo> list = baseProductProcessReMService.findHtList(ControllerUtil.dynamicConditionByEntity(searchBaseProductProcessReM));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchBaseProductProcessReM searchBaseProductProcessReM){
    List<BaseProductProcessReMVo> list = baseProductProcessReMService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseProductProcessReM));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "BaseProductProcessReM信息", BaseProductProcessReMDto.class, "BaseProductProcessReM.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
