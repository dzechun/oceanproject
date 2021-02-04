package com.fantechs.provider.qms.service;

import com.fantechs.common.base.general.dto.qms.QmsQualityConfirmationDto;
import com.fantechs.common.base.general.entity.qms.QmsQualityConfirmation;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/01/19.
 */

public interface QmsQualityConfirmationService extends IService<QmsQualityConfirmation> {
    List<QmsQualityConfirmationDto> findList(Map<String, Object> map);

    QmsQualityConfirmationDto analysis(String code,Byte type);

    int save(QmsQualityConfirmationDto qmsQualityConfirmationDto);

    Integer updateQuantity(Map<String, Object> map);

    QmsQualityConfirmation getQualityQuantity(Long workOrderCardPoolId,Long processId);

    Integer parentUpdateQuantity(Long workOrderCardPoolId,Long processId);
}
