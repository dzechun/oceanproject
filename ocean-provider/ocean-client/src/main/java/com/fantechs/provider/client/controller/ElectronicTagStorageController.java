package com.fantechs.provider.client.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.electronic.dto.SmtElectronicTagStorageDto;
import com.fantechs.common.base.electronic.dto.SmtLoadingDetDto;
import com.fantechs.common.base.electronic.dto.SmtSortingDto;
import com.fantechs.common.base.electronic.entity.SmtLoading;
import com.fantechs.common.base.electronic.entity.SmtLoadingDet;
import com.fantechs.common.base.electronic.entity.SmtSorting;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.client.server.ElectronicTagStorageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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
     * 生成分拣单
     */
    @PostMapping(value = "/createSorting")
    @ApiOperation(value = "生成分拣单", notes = "生成分拣单")
    public ResponseEntity createSorting(@RequestBody @Validated List<SmtSorting> sortingList) {
        try {
            return ControllerUtil.returnCRUD(electronicTagStorageService.createSorting(sortingList));
        } catch (Exception e) {
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.GL99990500.getCode());
        }
    }

    /**
     * 生成上料单
     */
    @PostMapping(value = "/createLoading")
    @ApiOperation(value = "生成上料单", notes = "生成上料单")
    public ResponseEntity createLoading(@RequestBody @Validated List<SmtLoading> smtLoadingList) {
        try {
            return ControllerUtil.returnCRUD(electronicTagStorageService.createLoading(smtLoadingList));
        } catch (Exception e) {
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.GL99990500.getCode());
        }
    }

    /**
     * 发送需要亮灯的储位、标签信息
     *
     * @param sortingCode
     * @throws Exception
     */
    @PostMapping(value = "/sendElectronicTagStorage")
    @ApiOperation(value = "拣取发送亮灯", notes = "拣取发送亮灯")
    public ResponseEntity<List<SmtSortingDto>> sendElectronicTagStorage(@RequestParam(value = "sortingCode") String sortingCode) {
        try {
            List<SmtSortingDto> smtSortingDtoList = electronicTagStorageService.sendElectronicTagStorage(sortingCode);
            return ControllerUtil.returnDataSuccess(smtSortingDtoList, smtSortingDtoList.size());
        } catch (Exception e) {
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.GL99990500.getCode());
        }
    }

    /**
     * 上料
     *
     * @param materialCode
     * @param
     * @return
     */
    @PostMapping(value="/sendPlaceMaterials")
    @ApiOperation(value = "上料发送亮灯(作废)",notes = "上料发送亮灯(作废)")
    public ResponseEntity<SmtElectronicTagStorageDto> sendPlaceMaterials(@RequestParam(value = "materialCode") String materialCode) {
        SmtElectronicTagStorageDto smtElectronicTagStorageDto = electronicTagStorageService.sendPlaceMaterials(materialCode);
        return ControllerUtil.returnDataSuccess(smtElectronicTagStorageDto, StringUtils.isEmpty(smtElectronicTagStorageDto) ? 0 : 1);
           }

    /**
     * 批量删除分拣单
     *
     * @param sortingCodes
     * @throws Exception
     */
    @PostMapping(value = "/batchDeleteSorting")
    @ApiOperation(value = "批量删除分拣单", notes = "批量删除分拣单")
    public ResponseEntity batchSortingDelete(@RequestBody List<String> sortingCodes) {
        try {
            return ControllerUtil.returnCRUD(electronicTagStorageService.batchSortingDelete(sortingCodes));
        } catch (Exception e) {
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.GL99990500.getCode());
        }
    }

    @PostMapping("/sendLoadingElectronicTagStorage")
    @ApiOperation(value = "上料发送亮灯", notes = "上料发送亮灯")
    public ResponseEntity<List<SmtLoadingDetDto>> sendLoadingElectronicTagStorage(@RequestParam(value = "loadingCode") String loadingCode) {
        try {
            List<SmtLoadingDetDto> smtLoadingDetDtoList =electronicTagStorageService.sendLoadingElectronicTagStorage(loadingCode);
            return ControllerUtil.returnDataSuccess(smtLoadingDetDtoList, smtLoadingDetDtoList.size());
        } catch (Exception e) {
            e.printStackTrace();
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.GL99990500.getCode());
        }
    }

    @PostMapping("/submitLoadingDet")
    @ApiOperation(value = "提交上料单发送灭灯", notes = "提交上料单发送灭灯")
    public ResponseEntity submitLoadingDet(@RequestBody @Validated(value = SmtLoadingDetDto.submit.class) List<SmtLoadingDetDto> smtLoadingDetDtoList) {
        try {
            return ControllerUtil.returnCRUD(electronicTagStorageService.submitLoadingDet(smtLoadingDetDtoList));
        } catch (Exception e) {
            e.printStackTrace();
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.GL99990500.getCode());
        }
    }

    @PostMapping("/revokeLoading")
    @ApiOperation(value = "撤销上料发送灭灯", notes = "撤销上料发送灭灯")
    public ResponseEntity revokeLoading(@RequestParam(value = "loadingCode") String loadingCode) {
        try {
            return ControllerUtil.returnCRUD(electronicTagStorageService.revokeLoading(loadingCode));
        } catch (Exception e) {
            e.printStackTrace();
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.GL99990500.getCode());
        }
    }

    @PostMapping("/comfirmLoadingDet")
    @ApiOperation(value = "确认单个物料上料发送灭灯", notes = "确认单个物料上料发送灭灯")
    public ResponseEntity comfirmLoadingDet(@RequestBody @Validated(value = SmtLoadingDetDto.submit.class) SmtLoadingDetDto smtLoadingDetDto) {
        try {
            return ControllerUtil.returnCRUD(electronicTagStorageService.comfirmLoadingDet(smtLoadingDetDto));
        } catch (Exception e) {
            e.printStackTrace();
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.GL99990500.getCode());
        }
    }

    @GetMapping(value = "/sendElectronicTagStorageTest")
    @ApiOperation(value = "拣取发送灭灯测试", notes = "拣取发送灭灯测试")
    public ResponseEntity<List<SmtSortingDto>> sendElectronicTagStorageTest(@RequestParam(value = "sortingCode") String sortingCode) {
        try {
            List<SmtSortingDto> smtSortingDtoList = electronicTagStorageService.sendElectronicTagStorageTest(sortingCode);
            return ControllerUtil.returnDataSuccess(smtSortingDtoList, smtSortingDtoList.size());
        } catch (Exception e) {
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.GL99990500.getCode());
        }
    }

    @GetMapping(value = "/sendElectronicTagStorageLightTest")
    @ApiOperation(value = "发送电子标签亮灯/灭灯测试", notes = "发送电子标签亮灯/灭灯测试")
    public ResponseEntity<String> sendElectronicTagStorageLightTest(
            @RequestParam(value = "materialCode") String materialCode,
            @RequestParam(value = "code(1001-亮灯 1003-灭灯)") Integer code) {
        try {
            String result = electronicTagStorageService.sendElectronicTagStorageLightTest(materialCode, code);
            return ControllerUtil.returnSuccess();
        } catch (Exception e) {
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.GL99990500.getCode());
        }
    }
}
