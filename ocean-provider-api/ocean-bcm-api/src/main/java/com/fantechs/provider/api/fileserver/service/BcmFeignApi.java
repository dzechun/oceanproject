package com.fantechs.provider.api.fileserver.service;


import com.fantechs.common.base.general.dto.basic.BaseBarCodeDto;
import com.fantechs.common.base.general.dto.basic.BaseLabelCategoryDto;
import com.fantechs.common.base.general.dto.basic.BaseLabelDto;
import com.fantechs.common.base.general.dto.mes.sfc.PrintDto;
import com.fantechs.common.base.general.entity.basic.BaseBarCodeDet;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseBarCode;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseLabel;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseLabelCategory;
import com.fantechs.common.base.response.ResponseEntity;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by lfz on 2018/8/22.
 */
@FeignClient(value = "ocean-bcm")
public interface BcmFeignApi {

    @ApiOperation("列表")
    @PostMapping("/bcmBarCode/findList")
    ResponseEntity<List<BaseBarCodeDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseBarCode searchBaseBarCode);

    @ApiOperation("根据工单ID和条码内容修改条码状态")
    @PostMapping("/bcmBarCode/updateByContent")
    ResponseEntity updateByContent(@ApiParam(value = "查询对象")@RequestBody List<BaseBarCodeDet> baseBarCodeDets);

    @ApiOperation("简单文本邮件")
    @GetMapping("/mail/sendSimpleMail")
    ResponseEntity sendSimpleMail(@ApiParam(value = "接收者邮箱",required = true)@RequestParam @NotNull(message = "接收邮件不能为空") String to,
                               @ApiParam(value = "标题",required = true)@RequestParam String subject,
                               @ApiParam(value = "内容",required = true)@RequestParam String content);
    @ApiOperation("HTML 文本邮件")
    @GetMapping("/mail/sendHtmlMail")
    ResponseEntity sendHtmlMail(@ApiParam(value = "接收者邮箱",required = true)@RequestParam @NotNull(message = "接收邮件不能为空") String to,
                             @ApiParam(value = "标题",required = true)@RequestParam String subject,
                             @ApiParam(value = "内容",required = true)@RequestParam String content);

    @ApiOperation("获取标签信息列表")
    @PostMapping("/bcmLabel/findList")
    ResponseEntity<List<BaseLabelDto>> findLabelList(@ApiParam(value = "查询对象")@RequestBody SearchBaseLabel searchBaseLabel);

    @ApiOperation("获取标签类别信息列表")
    @PostMapping("/bcmLabelCategory/findList")
    ResponseEntity<List<BaseLabelCategoryDto>> findLabelCategoryList(@ApiParam(value = "查询对象")@RequestBody SearchBaseLabelCategory searchBaseLabelCategory);

    @ApiOperation("打印")
    @PostMapping("/rabbit/print")
    ResponseEntity print(@RequestBody PrintDto printDto);

}
