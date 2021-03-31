package com.fantechs.provider.mes.pm.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.mes.pm.MesProcessReportWorkDto;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchMesProcessReportWork;
import com.fantechs.common.base.general.entity.mes.pm.MesProcessReportWork;
import com.fantechs.provider.mes.pm.service.MesProcessReportWorkService;
import com.fantechs.common.base.constants.ErrorCodeEnum;
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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/03/19.
 */
@RestController
@Api(tags = "工序手动报工控制器")
@RequestMapping("/mesProcessReportWork")
@Validated
public class MesProcessReportWorkController {

    @Resource
    private MesProcessReportWorkService mesProcessReportWorkService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated MesProcessReportWork mesProcessReportWork) {
        return ControllerUtil.returnCRUD(mesProcessReportWorkService.save(mesProcessReportWork));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(mesProcessReportWorkService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=MesProcessReportWork.update.class) MesProcessReportWork mesProcessReportWork) {
        return ControllerUtil.returnCRUD(mesProcessReportWorkService.update(mesProcessReportWork));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<MesProcessReportWork> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        MesProcessReportWork  mesProcessReportWork = mesProcessReportWorkService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(mesProcessReportWork,StringUtils.isEmpty(mesProcessReportWork)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<MesProcessReportWorkDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchMesProcessReportWork searchMesProcessReportWork) {
        Page<Object> page = PageHelper.startPage(searchMesProcessReportWork.getStartPage(),searchMesProcessReportWork.getPageSize());
        List<MesProcessReportWorkDto> list = mesProcessReportWorkService.findList(searchMesProcessReportWork);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchMesProcessReportWork searchMesProcessReportWork){
    List<MesProcessReportWorkDto> list = mesProcessReportWorkService.findList(searchMesProcessReportWork);
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "MesProcessReportWork信息", MesProcessReportWorkDto.class, "MesProcessReportWork.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
