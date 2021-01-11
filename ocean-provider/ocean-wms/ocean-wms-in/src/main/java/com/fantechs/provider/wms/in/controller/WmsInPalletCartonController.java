package com.fantechs.provider.wms.in.controller;

import com.fantechs.common.base.entity.storage.SmtStoragePallet;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.wms.in.WmsInPalletCarton;
import com.fantechs.common.base.general.entity.wms.in.search.SearchWmsInPalletCarton;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;

import com.fantechs.provider.wms.in.service.WmsInPalletCartonService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/01/07.
 */
@RestController
@Api(tags = "栈板-包箱关系控制器")
@RequestMapping("/wmsInPalletCarton")
@Validated
public class WmsInPalletCartonController {

    @Autowired
    private WmsInPalletCartonService wmsInPalletCartonService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WmsInPalletCarton wmsInPalletCarton) {
        return ControllerUtil.returnCRUD(wmsInPalletCartonService.save(wmsInPalletCarton));
    }

//    @ApiOperation("删除")
//    @PostMapping("/delete")
//    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
//        return ControllerUtil.returnCRUD(wmsInPalletCartonService.batchDelete(ids));
//    }
//
//    @ApiOperation("修改")
//    @PostMapping("/update")
//    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=WmsInPalletCarton.update.class) WmsInPalletCarton wmsInPalletCarton) {
//        return ControllerUtil.returnCRUD(wmsInPalletCartonService.update(wmsInPalletCarton));
//    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<WmsInPalletCarton> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        WmsInPalletCarton  wmsInPalletCarton = wmsInPalletCartonService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(wmsInPalletCarton,StringUtils.isEmpty(wmsInPalletCarton)?0:1);
    }

//    @ApiOperation("列表")
//    @PostMapping("/findList")
//    public ResponseEntity<List<WmsInPalletCarton>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInPalletCarton searchWmsInPalletCarton) {
//        Page<Object> page = PageHelper.startPage(searchWmsInPalletCarton.getStartPage(),searchWmsInPalletCarton.getPageSize());
//        List<WmsInPalletCarton> list = wmsInPalletCartonService.findList(searchWmsInPalletCarton);
//        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
//    }
//
//    @ApiOperation("历史列表")
//    @PostMapping("/findHtList")
//    public ResponseEntity<List<WmsInPalletCarton>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInPalletCarton searchWmsInPalletCarton) {
//        Page<Object> page = PageHelper.startPage(searchWmsInPalletCarton.getStartPage(),searchWmsInPalletCarton.getPageSize());
//        List<WmsInPalletCarton> list = wmsInPalletCartonService.findList(searchWmsInPalletCarton);
//        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
//    }
//
//    @PostMapping(value = "/export")
//    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
//    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
//    @RequestBody(required = false) SearchWmsInPalletCarton searchWmsInPalletCarton){
//    List<WmsInPalletCarton> list = wmsInPalletCartonService.findList(searchWmsInPalletCarton);
//    try {
//        // 导出操作
//        EasyPoiUtils.exportExcel(list, "导出信息", "WmsInPalletCarton信息", WmsInPalletCarton.class, "WmsInPalletCarton.xls", response);
//        } catch (Exception e) {
//        throw new BizErrorException(e);
//        }
//    }

    @ApiOperation("PDA扫码判断栈板是否绑定储位")
    @PostMapping("/checkPallet")
    public ResponseEntity<String> checkPallet(@ApiParam(value = "palletCode:栈板编码 \b 返回值：true 可用，false 不可用",required = true)@RequestParam String palletCode) {
        return ControllerUtil.returnDataSuccess(wmsInPalletCartonService.checkPallet(palletCode),0);
    }
}
