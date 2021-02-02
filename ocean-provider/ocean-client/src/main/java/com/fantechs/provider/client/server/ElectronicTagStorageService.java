package com.fantechs.provider.client.server;

import com.fantechs.common.base.electronic.dto.SmtElectronicTagStorageDto;
import com.fantechs.common.base.electronic.dto.SmtLoadingDetDto;
import com.fantechs.common.base.electronic.dto.SmtSortingDto;
import com.fantechs.common.base.electronic.entity.SmtLoading;
import com.fantechs.common.base.electronic.entity.SmtLoadingDet;
import com.fantechs.common.base.electronic.entity.SmtSorting;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by lfz on 2020/12/9.
 */
public interface ElectronicTagStorageService {

    List<SmtSortingDto> sendElectronicTagStorage(String sortingCode) throws Exception;
    SmtElectronicTagStorageDto sendPlaceMaterials(String materialCode);
    int batchSortingDelete(List<String> sortingList) throws Exception;
    int createSorting(List<SmtSorting> sortingList) throws Exception;
    int createLoading(List<SmtLoading> smtLoadingList)throws Exception;
    List<SmtLoadingDetDto> sendLoadingElectronicTagStorage(String loadingCode) throws Exception;
    int submitLoadingDet(List<SmtLoadingDetDto> smtLoadingDetDtoList) throws Exception;
    int revokeLoading(String loadingCode) throws Exception;
    int comfirmLoadingDet(SmtLoadingDetDto smtLoadingDetDto) throws Exception;
    List<SmtSortingDto> sendElectronicTagStorageTest(String sortingCode) throws Exception;
}
