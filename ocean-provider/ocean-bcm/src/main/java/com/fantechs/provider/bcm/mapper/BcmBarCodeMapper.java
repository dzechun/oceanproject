package com.fantechs.provider.bcm.mapper;

import com.fantechs.common.base.general.dto.bcm.BcmBarCodeDto;
import com.fantechs.common.base.general.dto.bcm.BcmBarCodeWorkDto;
import com.fantechs.common.base.general.entity.bcm.BcmBarCode;
import com.fantechs.common.base.general.entity.bcm.search.SearchBcmBarCode;
import com.fantechs.common.base.general.entity.mes.pm.SmtBarcodeRule;
import com.fantechs.common.base.general.entity.mes.pm.SmtBarcodeRuleSpec;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BcmBarCodeMapper extends MyMapper<BcmBarCode> {
    List<BcmBarCodeDto> findList(SearchBcmBarCode searchBcmBarCode);

    BcmBarCodeWorkDto sel_work_order(SearchBcmBarCode searchBcmBarCode);

    String selMaxCode(@Param("workOrderId")Long workOrderId);

    List<BcmBarCodeDto> reprintList(@Param("workOrderId")String workOrderId);
}