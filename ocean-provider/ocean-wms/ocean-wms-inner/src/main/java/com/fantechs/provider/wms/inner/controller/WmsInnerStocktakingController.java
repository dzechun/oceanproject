package com.fantechs.provider.wms.inner.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerStocktakingDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerStocktaking;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerStocktaking;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.inner.service.WmsInnerStocktakingService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/12/16.
 */
@RestController
@Api(tags = "盘点单信息管理")
@RequestMapping("/wmsInnerStocktaking")
@Validated
@Slf4j
public class WmsInnerStocktakingController {

    @Autowired
    private WmsInnerStocktakingService wmsInnerStocktakingService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：materialId",required = true)@RequestBody @Validated WmsInnerStocktaking wmsInnerStocktaking) {
        return ControllerUtil.returnCRUD(wmsInnerStocktakingService.save(wmsInnerStocktaking));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(wmsInnerStocktakingService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=WmsInnerStocktaking.update.class) WmsInnerStocktaking wmsInnerStocktaking) {
        return ControllerUtil.returnCRUD(wmsInnerStocktakingService.update(wmsInnerStocktaking));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<WmsInnerStocktaking> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        WmsInnerStocktaking  wmsInnerStocktaking = wmsInnerStocktakingService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(wmsInnerStocktaking,StringUtils.isEmpty(wmsInnerStocktaking)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WmsInnerStocktakingDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInnerStocktaking searchWmsInnerStocktaking) {
        Page<Object> page = PageHelper.startPage(searchWmsInnerStocktaking.getStartPage(),searchWmsInnerStocktaking.getPageSize());
        List<WmsInnerStocktakingDto> list = wmsInnerStocktakingService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInnerStocktaking));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }


    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchWmsInnerStocktaking searchWmsInnerStocktaking){
    List<WmsInnerStocktakingDto> list = wmsInnerStocktakingService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInnerStocktaking));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "WmsInnerStocktaking信息", WmsInnerStocktakingDto.class, "WmsInnerStocktaking.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

    /**
     * 从excel导入数据
     * @return
     * @throws
     */
    @PostMapping(value = "/import")
    @ApiOperation(value = "从excel导入盘点作业信息",notes = "从excel导入盘点作业信息")
    public ResponseEntity importStocktaking(@ApiParam(value ="输入excel文件",required = true)
                                      @RequestPart(value="file") MultipartFile file){
        try {
            // 导入操作
            List<WmsInnerStocktakingDto> wmsInnerStocktakingDtos = EasyPoiUtils.importExcel(file,WmsInnerStocktakingDto.class);
            Map<String, Object> resultMap = wmsInnerStocktakingService.importStocktaking(wmsInnerStocktakingDtos);
            return ControllerUtil.returnDataSuccess("操作结果集",resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.OPT20012002.getCode());
        }
    }
}
