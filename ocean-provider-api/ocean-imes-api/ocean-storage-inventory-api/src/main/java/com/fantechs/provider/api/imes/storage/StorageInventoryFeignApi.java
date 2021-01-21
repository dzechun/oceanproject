package com.fantechs.provider.api.imes.storage;

import com.fantechs.common.base.dto.storage.SmtStorageInventoryDetDto;
import com.fantechs.common.base.dto.storage.SmtStorageInventoryDto;
import com.fantechs.common.base.dto.storage.SmtStoragePalletDto;
import com.fantechs.common.base.entity.basic.SmtStorageMaterial;
import com.fantechs.common.base.entity.basic.search.SearchSmtStorageInventory;
import com.fantechs.common.base.entity.basic.search.SearchSmtStorageInventoryDet;
import com.fantechs.common.base.entity.storage.SmtStorageInventory;
import com.fantechs.common.base.entity.storage.SmtStorageInventoryDet;
import com.fantechs.common.base.entity.storage.SmtStoragePallet;
import com.fantechs.common.base.entity.storage.search.SearchSmtStoragePallet;
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
@FeignClient(name = "ocean-storage-inventory")
public interface StorageInventoryFeignApi {


    @ApiOperation("储位库存查询")
    @PostMapping("/smtStorageInventory/findList")
    ResponseEntity<List<SmtStorageInventoryDto>> findList(@ApiParam(value = "查询对象") @RequestBody SearchSmtStorageInventory searchSmtStorageInventory);

    @ApiOperation("储位库存新增")
    @PostMapping("/smtStorageInventory/add")
    ResponseEntity<SmtStorageInventory> add(@ApiParam(value = "必传：", required = true) @RequestBody SmtStorageInventory smtStorageInventory);

    @ApiOperation("储位库存删除")
    @PostMapping("/smtStorageInventory/delete")
    ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔", required = true) @RequestParam @NotBlank(message = "ids不能为空") String ids);

    @ApiOperation("储位库存更新")
    @PostMapping("/smtStorageInventory/update")
    ResponseEntity update(@ApiParam(value = "对象，Id必传", required = true) @RequestBody SmtStorageInventory smtStorageInventory);

    @ApiOperation("扣除储位库存")
    @PostMapping("/smtStorageInventory/out")
    ResponseEntity<SmtStorageInventory> out(@ApiParam(value = "必传：",required = true)@RequestBody @Validated SmtStorageInventory smtStorageInventory);

    @ApiOperation("储位库存明细新增")
    @PostMapping("/smtStorageInventoryDet/add")
    ResponseEntity add(@ApiParam(value = "必传：", required = true) @RequestBody SmtStorageInventoryDet smtStorageInventoryDet);

    @ApiOperation("储位库存明细列表")
    @PostMapping("/smtStorageInventoryDet/findList")
    ResponseEntity<List<SmtStorageInventoryDetDto>> findStorageInventoryDetList(@ApiParam(value = "查询对象") @RequestBody SearchSmtStorageInventoryDet searchSmtStorageInventoryDet);

    @ApiOperation("储位库存明细修改")
    @PostMapping("/smtStorageInventoryDet/update")
    ResponseEntity updateStorageInventoryDet(@ApiParam(value = "对象，Id必传", required = true) @RequestBody @Validated(value = SmtStorageInventoryDet.update.class) SmtStorageInventoryDet smtStorageInventoryDet);

    @ApiOperation("储位栈板关系表新增")
    @PostMapping("/smtStoragePallet/add")
    ResponseEntity add(@ApiParam(value = "必传：", required = true) @RequestBody SmtStoragePallet smtStoragePallet);

    @ApiOperation("储位与栈板列表")
    @PostMapping("/smtStoragePallet/findList")
    ResponseEntity<List<SmtStoragePalletDto>> findList(@ApiParam(value = "查询对象") @RequestBody SearchSmtStoragePallet searchSmtStoragePallet);

    @ApiOperation("储位栈板关系表修改")
    @PostMapping("/smtStoragePallet/update")
    ResponseEntity update(@ApiParam(value = "必传：", required = true) @RequestBody SmtStoragePallet smtStoragePallet);

    @ApiOperation("储位栈板关系表删除")
    @PostMapping("/smtStoragePallet/delete")
    ResponseEntity deleteSmtStoragePallet(@ApiParam(value = "对象ID列表，多个逗号分隔", required = true) @RequestParam @NotBlank(message = "ids不能为空") String ids);
}
