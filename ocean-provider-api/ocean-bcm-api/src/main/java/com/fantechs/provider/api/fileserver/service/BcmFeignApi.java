package com.fantechs.provider.api.fileserver.service;


import com.fantechs.common.base.general.dto.bcm.BcmBarCodeDto;
import com.fantechs.common.base.general.dto.bcm.BcmLabelCategoryDto;
import com.fantechs.common.base.general.dto.bcm.BcmLabelDto;
import com.fantechs.common.base.general.entity.bcm.BcmBarCode;
import com.fantechs.common.base.general.entity.bcm.BcmBarCodeDet;
import com.fantechs.common.base.general.entity.bcm.search.SearchBcmBarCode;
import com.fantechs.common.base.general.entity.bcm.search.SearchBcmLabel;
import com.fantechs.common.base.general.entity.bcm.search.SearchBcmLabelCategory;
import com.fantechs.common.base.response.ResponseEntity;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;

/**
 * Created by lfz on 2018/8/22.
 */
@FeignClient(value = "ocean-bcm")
public interface BcmFeignApi {

    @ApiOperation("列表")
    @PostMapping("/bcmBarCode/findList")
    ResponseEntity<List<BcmBarCodeDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBcmBarCode searchBcmBarCode);

    @ApiOperation("根据工单ID和条码内容修改条码状态")
    @PostMapping("/bcmBarCode/updateByContent")
    ResponseEntity updateByContent(@ApiParam(value = "查询对象")@RequestBody List<BcmBarCodeDet> bcmBarCodeDets);

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
    ResponseEntity<List<BcmLabelDto>> findLabelList(@ApiParam(value = "查询对象")@RequestBody SearchBcmLabel searchBcmLabel);

    @ApiOperation("获取标签类别信息列表")
    @PostMapping("/bcmLabelCategory/findList")
    ResponseEntity<List<BcmLabelCategoryDto>> findLabelCategoryList(@ApiParam(value = "查询对象")@RequestBody SearchBcmLabelCategory searchBcmLabelCategory);

    @PostMapping("/print")
    ResponseEntity print(byte[] bytes) throws IOException;

}
