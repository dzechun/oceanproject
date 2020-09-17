package com.fantechs.provider.api.imes.basic.service;

import com.fantechs.common.base.entity.basic.SmtDept;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Created by wcz on 2020/9/17.
 */
@FeignClient(value = "ocean-imes-basic")
public interface SmtDeptFeignApi {

    @PostMapping(value = "/smtDept/add")
    ResponseEntity add(@RequestBody SmtDept smtDept);
}
