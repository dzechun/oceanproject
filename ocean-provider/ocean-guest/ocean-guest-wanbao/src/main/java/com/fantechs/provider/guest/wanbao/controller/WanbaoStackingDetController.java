package com.fantechs.provider.guest.wanbao.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wanbao.WanbaoStackingDetDto;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.entity.wanbao.WanbaoStackingDet;
import com.fantechs.common.base.general.entity.wanbao.search.SearchWanbaoStackingDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.guest.wanbao.service.WanbaoStackingDetService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2022/01/21.
 */
@RestController
@Api(tags = "堆垛明细控制器")
@RequestMapping("/wanbaoStackingDet")
@Validated
public class WanbaoStackingDetController {

    @Resource
    private WanbaoStackingDetService wanbaoStackingDetService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WanbaoStackingDet wanbaoStackingDet) {
        return ControllerUtil.returnCRUD(wanbaoStackingDetService.save(wanbaoStackingDet));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(wanbaoStackingDetService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=WanbaoStackingDet.update.class) WanbaoStackingDet wanbaoStackingDet) {
        return ControllerUtil.returnCRUD(wanbaoStackingDetService.update(wanbaoStackingDet));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<WanbaoStackingDet> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        WanbaoStackingDet  wanbaoStackingDet = wanbaoStackingDetService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(wanbaoStackingDet,StringUtils.isEmpty(wanbaoStackingDet)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WanbaoStackingDetDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWanbaoStackingDet searchWanbaoStackingDet) {
        Page<Object> page = PageHelper.startPage(searchWanbaoStackingDet.getStartPage(),searchWanbaoStackingDet.getPageSize());
        List<WanbaoStackingDetDto> list = wanbaoStackingDetService.findList(ControllerUtil.dynamicConditionByEntity(searchWanbaoStackingDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<WanbaoStackingDetDto>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchWanbaoStackingDet searchWanbaoStackingDet) {
        List<WanbaoStackingDetDto> list = wanbaoStackingDetService.findList(ControllerUtil.dynamicConditionByEntity(searchWanbaoStackingDet));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }


    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchWanbaoStackingDet searchWanbaoStackingDet){
    List<WanbaoStackingDetDto> list = wanbaoStackingDetService.findList(ControllerUtil.dynamicConditionByEntity(searchWanbaoStackingDet));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "WanbaoStackingDet信息", WanbaoStackingDetDto.class, "WanbaoStackingDet.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

    @PostMapping(value = "/import")
    @ApiOperation(value = "从excel导入",notes = "从excel导入")
    public ResponseEntity importExcel(@ApiParam(value ="输入excel文件",required = true) @RequestPart(value="file") MultipartFile file){
        try {
            // 导入操作
            List<WanbaoStackingDet> baseAddressImports = EasyPoiUtils.importExcel(file, 0, 1, WanbaoStackingDet.class);
            Map<String, Object> resultMap = wanbaoStackingDetService.importExcel(baseAddressImports);
            return ControllerUtil.returnDataSuccess("操作结果集",resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.OPT20012002.getCode());
        }
    }
}
