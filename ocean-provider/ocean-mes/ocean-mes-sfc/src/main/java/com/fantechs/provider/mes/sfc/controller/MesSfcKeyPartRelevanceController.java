package com.fantechs.provider.mes.sfc.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.mes.sfc.MesSfcKeyPartRelevanceDto;
import com.fantechs.common.base.general.dto.mes.sfc.Search.SearchMesSfcKeyPartRelevance;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcKeyPartRelevance;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.mes.sfc.service.MesSfcKeyPartRelevanceService;
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
 * Created by leifengzhi on 2021/05/08.
 */
@RestController
@Api(tags = "生产管理-产品关键物料清单控制器")
@RequestMapping("/mesSfcKeyPartRelevance")
@Validated
public class MesSfcKeyPartRelevanceController {

    @Resource
    private MesSfcKeyPartRelevanceService mesSfcKeyPartRelevanceService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated MesSfcKeyPartRelevance mesSfcKeyPartRelevance) {
        return ControllerUtil.returnCRUD(mesSfcKeyPartRelevanceService.save(mesSfcKeyPartRelevance));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(mesSfcKeyPartRelevanceService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value= MesSfcKeyPartRelevance.update.class) MesSfcKeyPartRelevance mesSfcKeyPartRelevance) {
        return ControllerUtil.returnCRUD(mesSfcKeyPartRelevanceService.update(mesSfcKeyPartRelevance));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<MesSfcKeyPartRelevance> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        MesSfcKeyPartRelevance  mesSfcKeyPartRelevance = mesSfcKeyPartRelevanceService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(mesSfcKeyPartRelevance,StringUtils.isEmpty(mesSfcKeyPartRelevance)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<MesSfcKeyPartRelevanceDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchMesSfcKeyPartRelevance searchMesSfcKeyPartRelevance) {
        Page<Object> page = PageHelper.startPage(searchMesSfcKeyPartRelevance.getStartPage(),searchMesSfcKeyPartRelevance.getPageSize());
        List<MesSfcKeyPartRelevanceDto> list = mesSfcKeyPartRelevanceService.findList(ControllerUtil.dynamicConditionByEntity(searchMesSfcKeyPartRelevance));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }


    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchMesSfcKeyPartRelevance searchMesSfcKeyPartRelevance){
    List<MesSfcKeyPartRelevanceDto> list = mesSfcKeyPartRelevanceService.findList(ControllerUtil.dynamicConditionByEntity(searchMesSfcKeyPartRelevance));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "MesSfcKeyPartRelevance信息", MesSfcKeyPartRelevanceDto.class, "MesSfcKeyPartRelevance.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

    @ApiOperation("条码解绑")
    @PostMapping("/barcodeUnbinding")
    public ResponseEntity barcodeUnbinding(@RequestParam @NotBlank(message="条码不能为空") String barcode) {
        if(mesSfcKeyPartRelevanceService.barcodeUnbinding(barcode)){
            return ControllerUtil.returnSuccess("条码解绑成功!");
        }else{
            return ControllerUtil.returnFail(ErrorCodeEnum.valueOf("条码解绑失败!"));
        }
    }
}
