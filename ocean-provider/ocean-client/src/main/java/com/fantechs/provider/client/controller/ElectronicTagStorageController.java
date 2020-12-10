package com.fantechs.provider.client.controller;

import com.fantechs.common.base.electronic.dto.SmtElectronicTagStorageDto;
import com.fantechs.common.base.electronic.entity.SmtSorting;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.client.server.ElectronicTagStorageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by lfz on 2020/11/27.
 */
@RestController
@Api(tags = "电子标签控制器")
public class ElectronicTagStorageController {
    @Autowired
    private ElectronicTagStorageService electronicTagStorageService;
    /**
     * 发送需要亮灯的储位、标签信息
     * @param Sortings
     * @throws Exception
     */
    @PostMapping(value="/sendElectronicTagStorage")
    @ApiOperation(value = "拣取发送亮灯",notes = "拣取发送亮灯")
    public ResponseEntity sendElectronicTagStorage(@RequestBody  @Validated List<SmtSorting>  Sortings ) {
        return ControllerUtil.returnCRUD(electronicTagStorageService.sendElectronicTagStorage(Sortings));
    }

    /**
     * 上料
     * @param materialCode
     * @param
     * @return
     */
    @PostMapping(value="/sendPlaceMaterials")
    @ApiOperation(value = "上料发送亮灯",notes = "上料发送亮灯")
    public ResponseEntity<SmtElectronicTagStorageDto> sendPlaceMaterials(@RequestParam(value = "materialCode") String materialCode) {
        SmtElectronicTagStorageDto smtElectronicTagStorageDto = electronicTagStorageService.sendPlaceMaterials(materialCode);
        return ControllerUtil.returnDataSuccess(smtElectronicTagStorageDto, StringUtils.isEmpty(smtElectronicTagStorageDto) ? 0 : 1);
           }

    /**
     * 批量删除分拣单
     * @param sortingCodes
     * @throws Exception
     */
    @PostMapping(value="/batchDeleteSorting")
    @ApiOperation(value = "批量删除分拣单",notes = "批量删除分拣单")
    public ResponseEntity batchSortingDelete(@RequestBody List<String>  sortingCodes ) {
        return ControllerUtil.returnCRUD(electronicTagStorageService.batchSortingDelete(sortingCodes));
    }
}
