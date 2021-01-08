package com.fantechs.provider.api.imes.storage;

import com.fantechs.common.base.dto.storage.SmtStorageInventoryDto;
import com.fantechs.common.base.entity.basic.SmtStorageMaterial;
import com.fantechs.common.base.entity.basic.search.SearchSmtStorageInventory;
import com.fantechs.common.base.entity.storage.SmtStorageInventory;
import com.fantechs.common.base.entity.storage.SmtStorageInventoryDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Date 2020/12/7 17:39
 */
@FeignClient(name = "ocean-imes-basic")
public interface StorageInventoryFeignApi {

    @PostMapping("/smtStorageMaterial/detail")
    ResponseEntity<SmtStorageMaterial> detail(@ApiParam(value = "ID",required = true)@RequestParam @NotNull(message = "id不能为空") Long id) ;

    @PostMapping("/smtStorageInventory/findList")
    ResponseEntity<List<SmtStorageInventoryDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSmtStorageInventory searchSmtStorageInventory);


    @PostMapping("/smtStorageInventory/add")
    ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody  SmtStorageInventory smtStorageInventory) ;


    @PostMapping("/smtStorageInventory/delete")
    ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) ;


    @PostMapping("/smtStorageInventory/update")
    ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody  SmtStorageInventory smtStorageInventory);


    @PostMapping("/smtStorageInventoryDet/add")
    ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody SmtStorageInventoryDet smtStorageInventoryDet);
}
