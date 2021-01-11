package com.fantechs.provider.api.om;

import com.fantechs.common.base.general.entity.om.SmtOrder;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Auther: bingo.ren
 * @Date: 2021/1/9 16:27
 * @Description:
 * @Version: 1.0
 */
@FeignClient(name = "ocean-om")
public interface OMFeignApi {
    @ApiOperation("修改")
    @PostMapping("/update")
    ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody SmtOrder smtOrder);
}
