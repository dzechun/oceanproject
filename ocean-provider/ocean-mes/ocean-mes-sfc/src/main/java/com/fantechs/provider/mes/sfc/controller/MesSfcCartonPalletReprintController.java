package com.fantechs.provider.mes.sfc.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.mes.sfc.MesSfcCartonPalletReprintDto;
import com.fantechs.common.base.general.dto.mes.sfc.Search.SearchMesSfcCartonPalletReprint;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcCartonPalletReprint;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.mes.sfc.service.MesSfcCartonPalletReprintService;
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
 * Created by leifengzhi on 2021/05/19.
 */
@RestController
@Api(tags = "生产管理-条码补打控制器")
@RequestMapping("/mesSfcCartonPalletReprint")
@Validated
public class MesSfcCartonPalletReprintController {

    @Resource
    private MesSfcCartonPalletReprintService mesSfcCartonPalletReprintService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated MesSfcCartonPalletReprint mesSfcCartonPalletReprint) {
        return ControllerUtil.returnCRUD(mesSfcCartonPalletReprintService.save(mesSfcCartonPalletReprint));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(mesSfcCartonPalletReprintService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=MesSfcCartonPalletReprint.update.class) MesSfcCartonPalletReprint mesSfcCartonPalletReprint) {
        return ControllerUtil.returnCRUD(mesSfcCartonPalletReprintService.update(mesSfcCartonPalletReprint));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<MesSfcCartonPalletReprint> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        MesSfcCartonPalletReprint  mesSfcCartonPalletReprint = mesSfcCartonPalletReprintService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(mesSfcCartonPalletReprint,StringUtils.isEmpty(mesSfcCartonPalletReprint)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<MesSfcCartonPalletReprintDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchMesSfcCartonPalletReprint searchMesSfcCartonPalletReprint) {
        Page<Object> page = PageHelper.startPage(searchMesSfcCartonPalletReprint.getStartPage(),searchMesSfcCartonPalletReprint.getPageSize());
        List<MesSfcCartonPalletReprintDto> list = mesSfcCartonPalletReprintService.findList(ControllerUtil.dynamicConditionByEntity(searchMesSfcCartonPalletReprint));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchMesSfcCartonPalletReprint searchMesSfcCartonPalletReprint){
    List<MesSfcCartonPalletReprintDto> list = mesSfcCartonPalletReprintService.findList(ControllerUtil.dynamicConditionByEntity(searchMesSfcCartonPalletReprint));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "MesSfcCartonPalletReprint信息", MesSfcCartonPalletReprintDto.class, "MesSfcCartonPalletReprint.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
