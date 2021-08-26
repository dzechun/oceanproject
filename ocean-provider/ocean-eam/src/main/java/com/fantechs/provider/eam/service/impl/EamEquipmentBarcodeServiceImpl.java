package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.eam.EamEquipmentBarcode;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.mapper.EamEquipmentBarcodeMapper;
import com.fantechs.provider.eam.service.EamEquipmentBarcodeService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/21.
 */
@Service
public class EamEquipmentBarcodeServiceImpl extends BaseService<EamEquipmentBarcode> implements EamEquipmentBarcodeService {

    @Resource
    private EamEquipmentBarcodeMapper eamEquipmentBarcodeMapper;

    @Override
    public List<EamEquipmentBarcode> findList(Map<String, Object> map) {
        if(StringUtils.isEmpty(map.get("orgId"))) {
            SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
            if (StringUtils.isEmpty(user)) {
                throw new BizErrorException(ErrorCodeEnum.UAC10011039);
            }
            map.put("orgId", user.getOrganizationId());
        }

        return eamEquipmentBarcodeMapper.findList(map);
    }

    @Override
    public int plusCurrentUsageTime(Long equipmentBarCodeId, Integer num) {
        EamEquipmentBarcode eamEquipmentBarcode = eamEquipmentBarcodeMapper.selectByPrimaryKey(equipmentBarCodeId);
        if (StringUtils.isEmpty(eamEquipmentBarcode)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003);
        }
        eamEquipmentBarcode.setCurrentUsageTime(eamEquipmentBarcode.getCurrentUsageTime()+num);
        return eamEquipmentBarcodeMapper.updateByPrimaryKeySelective(eamEquipmentBarcode);
    }
}
