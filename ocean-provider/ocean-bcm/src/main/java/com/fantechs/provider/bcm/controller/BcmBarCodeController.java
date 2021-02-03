package com.fantechs.provider.bcm.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.bcm.BcmBarCodeDto;
import com.fantechs.common.base.general.dto.bcm.BcmBarCodeWorkDto;
import com.fantechs.common.base.general.entity.bcm.BcmBarCode;
import com.fantechs.common.base.general.entity.bcm.BcmBarCodeDet;
import com.fantechs.common.base.general.entity.bcm.search.SearchBcmBarCode;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.bcm.service.BcmBarCodeService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
* @author Mr.Lei
* @create 2020/12/22.
*/
@RestController
@Api(tags = "条码管理")
@RequestMapping("/bcmBarCode")
@Validated
public class BcmBarCodeController {

    @Autowired
    private BcmBarCodeService bcmBarCodeService;

    @ApiOperation("生成条码")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated BcmBarCodeWorkDto bcmBarCode) {
        return ControllerUtil.returnCRUD(bcmBarCodeService.saveCode(bcmBarCode));
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BcmBarCodeDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBcmBarCode searchBcmBarCode) {
        Page<Object> page = PageHelper.startPage(searchBcmBarCode.getStartPage(),searchBcmBarCode.getPageSize());
        List<BcmBarCodeDto> list = bcmBarCodeService.findList(searchBcmBarCode);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("单号查询工单信息")
    @PostMapping("/work")
    public ResponseEntity work(@RequestBody(required = true)SearchBcmBarCode searchBcmBarCode){
        if(StringUtils.isEmpty(searchBcmBarCode.getWorkOrderCode())){
            return ControllerUtil.returnFail(ErrorCodeEnum.valueOf("工单号不能为空"));
        }
        BcmBarCodeWorkDto bcmBarCodeWorkDto = bcmBarCodeService.work(searchBcmBarCode);

        return ControllerUtil.returnDataSuccess(bcmBarCodeWorkDto,1);
    }

    @ApiOperation("标签下载打印 ")
    @PostMapping("/download")
    public void downExcel(@RequestBody List<String> savePath, HttpServletResponse response) throws UnsupportedEncodingException {
        bcmBarCodeService.download(savePath,response);
    }

    @ApiOperation("/print")
    @GetMapping("/print")
    public ResponseEntity print(@ApiParam(value = "必传：workdOrderId",required = true)@RequestParam Long workOrderId){
        return ControllerUtil.returnCRUD(bcmBarCodeService.print(workOrderId));
    }

    @ApiOperation("Pda条码对比")
    @GetMapping("/verifyQrCode")
    public ResponseEntity<BcmBarCodeDet> verifyQrCode(@RequestParam String QrCode, @RequestParam Long workOrderId){
        BcmBarCodeDet bcmBarCodeDet = bcmBarCodeService.verifyQrCode(QrCode,workOrderId);
        return ControllerUtil.returnDataSuccess(bcmBarCodeDet,StringUtils.isEmpty(bcmBarCodeDet)?0:1);
    }

    @ApiOperation("条码内容修改条码状态")
    @PostMapping("/updateByContent")
    public ResponseEntity updateByContent(@RequestBody(required = true)List<BcmBarCodeDet> bcmBarCodeDets){
        return ControllerUtil.returnCRUD(bcmBarCodeService.updateByContent(bcmBarCodeDets));
    }

    @ApiOperation("补打列表")
    @GetMapping("/reprintList")
    public ResponseEntity<List<BcmBarCodeDto>> reprintList(@ApiParam("工单id")@RequestParam(required = false)String workOrderId){
        List<BcmBarCodeDto> list = bcmBarCodeService.reprintList(workOrderId);
        return ControllerUtil.returnDataSuccess(list,list.size());
    }

    @ApiOperation("补打")
    @PostMapping("/reprint")
    public ResponseEntity<List<BcmBarCodeDto>> reprint(@ApiParam("条码id")@RequestBody(required = true)List<String> barCodeId){
        return ControllerUtil.returnCRUD(bcmBarCodeService.reprint(barCodeId));
    }
}
