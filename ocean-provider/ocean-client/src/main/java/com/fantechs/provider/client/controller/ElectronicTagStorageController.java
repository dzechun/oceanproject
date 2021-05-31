package com.fantechs.provider.client.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.electronic.dto.PtlLoadingDetDto;
import com.fantechs.common.base.electronic.dto.PtlSortingDto;
import com.fantechs.common.base.electronic.entity.PtlLoading;
import com.fantechs.common.base.electronic.entity.PtlSorting;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.client.dto.PtlSortingDTO;
import com.fantechs.provider.client.server.ElectronicTagStorageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity createSorting(@RequestBody @Validated List<PtlSortingDTO> ptlSortingDTOList) {
        try {
            return ControllerUtil.returnCRUD(electronicTagStorageService.createSorting(ptlSortingDTOList));
        } catch (Exception e) {
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.GL99990500.getCode());
        }
    }

    /**
     * 生成上料单
     */
    @PostMapping(value = "/createLoading")
    @ApiOperation(value = "生成上料单", notes = "生成上料单")
    public ResponseEntity createLoading(@RequestBody @Validated List<PtlLoading> ptlLoadingList) {
        try {
            return ControllerUtil.returnCRUD(electronicTagStorageService.createLoading(ptlLoadingList));
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
    @ApiOperation(value = "激活（拣取发送亮灯）", notes = "激活（拣取发送亮灯）")
    public ResponseEntity<List<PtlSortingDto>> sendElectronicTagStorage(
            @RequestParam(value = "任务号") String sortingCode,
            @RequestParam(value = "仓库区域Id") Long warehouseAreaId) {
        try {
            List<PtlSortingDto> ptlSortingDtoList = electronicTagStorageService.sendElectronicTagStorage(sortingCode, warehouseAreaId);
            return ControllerUtil.returnDataSuccess(ptlSortingDtoList, ptlSortingDtoList.size());
        } catch (Exception e) {
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.GL99990500.getCode());
        }
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
    public ResponseEntity<List<PtlLoadingDetDto>> sendLoadingElectronicTagStorage(@RequestParam(value = "loadingCode") String loadingCode) {
        try {
            List<PtlLoadingDetDto> ptlLoadingDetDtoList =electronicTagStorageService.sendLoadingElectronicTagStorage(loadingCode);
            return ControllerUtil.returnDataSuccess(ptlLoadingDetDtoList, ptlLoadingDetDtoList.size());
        } catch (Exception e) {
            e.printStackTrace();
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.GL99990500.getCode());
        }
    }

    @PostMapping("/submitLoadingDet")
    @ApiOperation(value = "提交上料单发送灭灯", notes = "提交上料单发送灭灯")
    public ResponseEntity submitLoadingDet(@RequestBody @Validated(value = PtlLoadingDetDto.submit.class) List<PtlLoadingDetDto> ptlLoadingDetDtoList) {
        try {
            return ControllerUtil.returnCRUD(electronicTagStorageService.submitLoadingDet(ptlLoadingDetDtoList));
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
    public ResponseEntity comfirmLoadingDet(@RequestBody @Validated(value = PtlLoadingDetDto.submit.class) PtlLoadingDetDto ptlLoadingDetDto) {
        try {
            return ControllerUtil.returnCRUD(electronicTagStorageService.comfirmLoadingDet(ptlLoadingDetDto));
        } catch (Exception e) {
            e.printStackTrace();
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.GL99990500.getCode());
        }
    }

    @GetMapping(value = "/sendElectronicTagStorageTest")
    @ApiOperation(value = "拣取发送灭灯测试", notes = "拣取发送灭灯测试")
    public ResponseEntity<List<PtlSortingDto>> sendElectronicTagStorageTest(@RequestParam(value = "sortingCode") String sortingCode) {
        try {
            List<PtlSortingDto> ptlSortingDtoList = electronicTagStorageService.sendElectronicTagStorageTest(sortingCode);
            return ControllerUtil.returnDataSuccess(ptlSortingDtoList, ptlSortingDtoList.size());
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
