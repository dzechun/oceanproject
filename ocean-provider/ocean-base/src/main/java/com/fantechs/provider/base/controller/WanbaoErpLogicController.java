package com.fantechs.provider.base.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.WanbaoErpLogicDto;
import com.fantechs.common.base.general.entity.basic.WanbaoErpLogic;
import com.fantechs.common.base.general.entity.basic.search.SearchWanbaoErpLogic;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.WanbaoErpLogicService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2022/03/03.
 */
@RestController
@Api(tags = "万宝ERP逻辑仓")
@RequestMapping("/wanbaoErpLogic")
@Validated
public class WanbaoErpLogicController {

    @Resource
    private WanbaoErpLogicService wanbaoErpLogicService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WanbaoErpLogic wanbaoErpLogic) {
        return ControllerUtil.returnCRUD(wanbaoErpLogicService.save(wanbaoErpLogic));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(wanbaoErpLogicService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=WanbaoErpLogic.update.class) WanbaoErpLogic wanbaoErpLogic) {
        return ControllerUtil.returnCRUD(wanbaoErpLogicService.update(wanbaoErpLogic));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<WanbaoErpLogic> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        WanbaoErpLogic  wanbaoErpLogic = wanbaoErpLogicService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(wanbaoErpLogic,StringUtils.isEmpty(wanbaoErpLogic)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WanbaoErpLogicDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWanbaoErpLogic searchWanbaoErpLogic) {
        Page<Object> page = PageHelper.startPage(searchWanbaoErpLogic.getStartPage(),searchWanbaoErpLogic.getPageSize());
        List<WanbaoErpLogicDto> list = wanbaoErpLogicService.findList(ControllerUtil.dynamicConditionByEntity(searchWanbaoErpLogic));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<WanbaoErpLogicDto>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchWanbaoErpLogic searchWanbaoErpLogic) {
        List<WanbaoErpLogicDto> list = wanbaoErpLogicService.findList(ControllerUtil.dynamicConditionByEntity(searchWanbaoErpLogic));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchWanbaoErpLogic searchWanbaoErpLogic){
    List<WanbaoErpLogicDto> list = wanbaoErpLogicService.findList(ControllerUtil.dynamicConditionByEntity(searchWanbaoErpLogic));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "WanbaoErpLogic信息", WanbaoErpLogicDto.class, "WanbaoErpLogic.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

//    @PostMapping(value = "/import")
//    @ApiOperation(value = "从excel导入",notes = "从excel导入")
//    public ResponseEntity importExcel(@ApiParam(value ="输入excel文件",required = true) @RequestPart(value="file") MultipartFile file){
//        try {
//            // 导入操作
//            List<WanbaoErpLogic> baseAddressImports = EasyPoiUtils.importExcel(file, 0, 1, WanbaoErpLogic.class);
//            Map<String, Object> resultMap = wanbaoErpLogicService.importExcel(baseAddressImports);
//            return ControllerUtil.returnDataSuccess("操作结果集",resultMap);
//        } catch (Exception e) {
//            e.printStackTrace();
//            log.error(e.getMessage());
//            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.OPT20012002.getCode());
//        }
//    }
}
