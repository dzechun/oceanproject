package com.fantechs.provider.client.server;

import com.fantechs.common.base.electronic.dto.PtlLoadingDetDto;
import com.fantechs.common.base.electronic.dto.PtlSortingDto;
import com.fantechs.common.base.electronic.entity.PtlLoading;
import com.fantechs.common.base.electronic.entity.PtlSorting;
import com.fantechs.provider.client.dto.PtlSortingDTO;

import java.util.List;

/**
 * Created by lfz on 2020/12/9.
 */
public interface ElectronicTagStorageService {

    List<PtlSortingDto> sendElectronicTagStorage(String sortingCode, Long warehouseAreaId) throws Exception;
    int batchSortingDelete(List<String> sortingList) throws Exception;
    int createSorting(List<PtlSortingDTO> ptlSortingDTOList) throws Exception;
    int createLoading(List<PtlLoading> ptlLoadingList)throws Exception;
    List<PtlLoadingDetDto> sendLoadingElectronicTagStorage(String loadingCode) throws Exception;
    int submitLoadingDet(List<PtlLoadingDetDto> ptlLoadingDetDtoList) throws Exception;
    int revokeLoading(String loadingCode) throws Exception;
    int comfirmLoadingDet(PtlLoadingDetDto ptlLoadingDetDto) throws Exception;
    List<PtlSortingDto> sendElectronicTagStorageTest(String sortingCode) throws Exception;
    String sendElectronicTagStorageLightTest(String materialCode, Integer code) throws Exception;
}
