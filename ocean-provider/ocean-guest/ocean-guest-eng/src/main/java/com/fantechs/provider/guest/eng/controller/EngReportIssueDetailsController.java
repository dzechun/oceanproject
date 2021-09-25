package com.fantechs.provider.guest.eng.controller;

import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.guest.eng.service.EngReportIssueDetailsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 *
 * Created by leifengzhi on 2021/09/01.
 */
@RestController
@Api(tags = "出库回传接口")
@RequestMapping("/reportIssueDetails")
@Validated
public class EngReportIssueDetailsController {

    @Resource
    private EngReportIssueDetailsService engReportIssueDetailsService;

//    @ApiOperation("返写领料出库")
//    @PostMapping("/writeQty")
//    public ResponseEntity<String> writePackingLists(WmsInnerJobOrder wmsInnerJobOrder) {
//        String result = engReportIssueDetailsService.reportIssueDetails(wmsInnerJobOrder);
//        return ControllerUtil.returnDataSuccess(result,1);
//    }

    @ApiOperation("返写领料出库")
    @PostMapping("/writeQty")
    public ResponseEntity<String> writePackingLists(@ApiParam(value = "对象，jobOrderId 必传",required = true)@RequestBody @Validated WmsInnerJobOrder wmsInnerJobOrder) {
        String result = engReportIssueDetailsService.reportIssueDetails(wmsInnerJobOrder);
        return ControllerUtil.returnDataSuccess(result,1);
    }
}
