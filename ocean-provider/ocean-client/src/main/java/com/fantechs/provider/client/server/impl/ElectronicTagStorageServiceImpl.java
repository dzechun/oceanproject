package com.fantechs.provider.client.server.impl;

import com.alibaba.fastjson.JSONObject;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.electronic.dto.SmtElectronicTagStorageDto;
import com.fantechs.common.base.electronic.entity.SmtSorting;
import com.fantechs.common.base.electronic.entity.search.SearchSmtElectronicTagStorage;
import com.fantechs.common.base.entity.basic.SmtMaterial;
import com.fantechs.common.base.entity.basic.search.SearchSmtMaterial;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.MQResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.electronic.ElectronicTagFeignApi;
import com.fantechs.provider.api.imes.basic.BasicFeignApi;
import com.fantechs.provider.client.server.ElectronicTagStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by lfz on 2020/12/9.
 */
@Service
public class ElectronicTagStorageServiceImpl implements ElectronicTagStorageService {
    @Autowired
    private  FanoutSender fanoutSender;
    @Autowired
    private ElectronicTagFeignApi electronicTagFeignApi;
    @Autowired
    private BasicFeignApi basicFeignApi;
    @Override
    public int sendElectronicTagStorage(List<SmtSorting> sortingList) {
        MQResponseEntity mQResponseEntity =  new MQResponseEntity<>();
        int i=0;
        for(SmtSorting sorting: sortingList){
            SearchSmtMaterial searchSmtMaterial = new SearchSmtMaterial();
            searchSmtMaterial.setMaterialCode(sorting.getMaterialCode());
           List<SmtMaterial>   smtMaterials=   basicFeignApi.findSmtMaterialList(searchSmtMaterial).getData();
            if(StringUtils.isEmpty(smtMaterials)){
                throw  new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"没有找到对应物料信息");
            }
            SearchSmtElectronicTagStorage searchSmtElectronicTagStorage = new SearchSmtElectronicTagStorage();
            searchSmtElectronicTagStorage.setMaterialId(smtMaterials.get(0).getMaterialId().toString());
            List<SmtElectronicTagStorageDto> smtElectronicTagStorageDtoList=electronicTagFeignApi.findElectronicTagStorageList(searchSmtElectronicTagStorage).getData();
            if(StringUtils.isEmpty(smtElectronicTagStorageDtoList)){
                throw  new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"请先维护储位对应的电子标签信息");
            }
            mQResponseEntity.setData(smtElectronicTagStorageDtoList.get(0));
            fanoutSender.send(smtElectronicTagStorageDtoList.get(0).getQueueName(),
                    JSONObject.toJSONString(mQResponseEntity));
        }
        return i;
    }
}
