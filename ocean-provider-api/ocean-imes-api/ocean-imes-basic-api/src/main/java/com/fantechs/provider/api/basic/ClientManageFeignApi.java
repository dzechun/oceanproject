package com.fantechs.provider.api.basic;

import com.fantechs.common.base.dto.basic.SmtClientManageDto;
import com.fantechs.common.base.entity.basic.SmtClientManage;
import com.fantechs.common.base.entity.basic.search.SearchSmtClientManage;
import com.fantechs.common.base.response.ResponseEntity;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value="ocean-imes-basic")
public interface ClientManageFeignApi {
    @PostMapping("/smtClientManage/update")
    ResponseEntity update(@ApiParam(value = "对象，Id必传")@RequestBody  SmtClientManage smtClientManage);

    @PostMapping("/smtClientManage/findList")
    ResponseEntity<List<SmtClientManageDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSmtClientManage searchSmtClientManage);
}
