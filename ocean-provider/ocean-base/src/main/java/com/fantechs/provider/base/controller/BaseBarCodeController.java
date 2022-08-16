package com.fantechs.provider.base.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.basic.BaseBarCodeDto;
import com.fantechs.common.base.general.dto.basic.BaseBarCodeWorkDto;
import com.fantechs.common.base.general.entity.basic.BaseBarCodeDet;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseBarCode;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseBarCodeService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
* @author Mr.Lei
* @create 2020/12/22.
*/
@RestController
@Api(tags = "条码管理")
@RequestMapping("/baseBarCode")
@Validated
public class BaseBarCodeController {

    @Resource
    private BaseBarCodeService baseBarCodeService;

    @ApiOperation("生成条码")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated BaseBarCodeWorkDto bcmBarCode) {
        return ControllerUtil.returnCRUD(baseBarCodeService.saveCode(bcmBarCode));
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseBarCodeDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseBarCode searchBaseBarCode) {
        Page<Object> page = PageHelper.startPage(searchBaseBarCode.getStartPage(), searchBaseBarCode.getPageSize());
        List<BaseBarCodeDto> list = baseBarCodeService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseBarCode));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("单号查询工单信息")
    @PostMapping("/work")
    public ResponseEntity work(@RequestBody(required = true) SearchBaseBarCode searchBaseBarCode){
        if(StringUtils.isEmpty(searchBaseBarCode.getWorkOrderCode())){
            return ControllerUtil.returnFail(ErrorCodeEnum.valueOf("工单号不能为空"));
        }
        BaseBarCodeWorkDto baseBarCodeWorkDto = baseBarCodeService.work(searchBaseBarCode);

        return ControllerUtil.returnDataSuccess(baseBarCodeWorkDto,1);
    }

    @ApiOperation("标签下载打印 ")
    @PostMapping("/download")
    public void downExcel(@RequestBody List<String> savePath, HttpServletResponse response) throws UnsupportedEncodingException {
        baseBarCodeService.download(savePath,response);
    }

    @ApiOperation("/print")
    @GetMapping("/print")
    public ResponseEntity print(@ApiParam(value = "必传：workdOrderId",required = true)@RequestParam Long workOrderId){
        return ControllerUtil.returnCRUD(baseBarCodeService.print(workOrderId));
    }

    @ApiOperation("Pda条码对比")
    @GetMapping("/verifyQrCode")
    public ResponseEntity<BaseBarCodeDet> verifyQrCode(@RequestParam String QrCode, @RequestParam Long workOrderId){
        BaseBarCodeDet baseBarCodeDet = baseBarCodeService.verifyQrCode(QrCode,workOrderId);
        return ControllerUtil.returnDataSuccess(baseBarCodeDet,StringUtils.isEmpty(baseBarCodeDet)?0:1);
    }

    @ApiOperation("条码内容修改条码状态")
    @PostMapping("/updateByContent")
    public ResponseEntity updateByContent(@RequestBody(required = true)List<BaseBarCodeDet> baseBarCodeDets){
        return ControllerUtil.returnCRUD(baseBarCodeService.updateByContent(baseBarCodeDets));
    }

    @ApiOperation("补打列表")
    @GetMapping("/reprintList")
    public ResponseEntity<List<BaseBarCodeDto>> reprintList(@ApiParam("工单id")@RequestParam(required = false)String workOrderId){
        List<BaseBarCodeDto> list = baseBarCodeService.reprintList(workOrderId);
        return ControllerUtil.returnDataSuccess(list,list.size());
    }

    @ApiOperation("补打")
    @PostMapping("/reprint")
    public ResponseEntity<List<BaseBarCodeDto>> reprint(@ApiParam("条码id")@RequestBody(required = true)List<String> barCodeId){
        return ControllerUtil.returnCRUD(baseBarCodeService.reprint(barCodeId));
    }
}
