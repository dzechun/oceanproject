package com.fantechs.provider.client.server.impl;

import com.alibaba.fastjson.JSONObject;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.electronic.dto.SmtElectronicTagStorageDto;
import com.fantechs.common.base.electronic.dto.SmtSortingDto;
import com.fantechs.common.base.electronic.entity.SmtPaddingMaterial;
import com.fantechs.common.base.electronic.entity.SmtSorting;
import com.fantechs.common.base.electronic.entity.search.SearchSmtElectronicTagStorage;
import com.fantechs.common.base.electronic.entity.search.SearchSmtSorting;
import com.fantechs.common.base.entity.basic.SmtMaterial;
import com.fantechs.common.base.entity.basic.SmtStorageMaterial;
import com.fantechs.common.base.entity.basic.search.SearchSmtMaterial;
import com.fantechs.common.base.entity.basic.search.SearchSmtStorageMaterial;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.MQResponseEntity;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.electronic.ElectronicTagFeignApi;
import com.fantechs.provider.api.imes.basic.BasicFeignApi;
import com.fantechs.provider.client.listener.ElectronicTagReceiver;
import com.fantechs.provider.client.server.ElectronicTagStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by lfz on 2020/12/9.
 */
@Service
public class ElectronicTagStorageServiceImpl implements ElectronicTagStorageService {
    private static final Logger log = LoggerFactory.getLogger(ElectronicTagReceiver.class);
    @Autowired
    private FanoutSender fanoutSender;
    @Autowired
    private ElectronicTagFeignApi electronicTagFeignApi;
    @Autowired
    private BasicFeignApi basicFeignApi;

    @Override
    public int sendElectronicTagStorage(List<SmtSorting> sortingList) {
        //是否有在做单据
        SearchSmtSorting searchSmtSorting = new SearchSmtSorting();
        searchSmtSorting.setStatus((byte) 1);
        List<SmtSortingDto> smtSortingList = electronicTagFeignApi.findSortingList(searchSmtSorting).getData();
        if (StringUtils.isNotEmpty(smtSortingList)) {
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "正在处理其他分拣单，请稍后在试");
        }
        List<SmtElectronicTagStorageDto> list = new LinkedList<>();
        for (SmtSorting sorting : sortingList) {
            searchSmtSorting = new SearchSmtSorting();
            searchSmtSorting.setSortingCode(sorting.getSortingCode());
            smtSortingList = electronicTagFeignApi.findSortingList(searchSmtSorting).getData();
            if (StringUtils.isNotEmpty(smtSortingList)) {
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "重复分拣单");
            }
            SearchSmtMaterial searchSmtMaterial = new SearchSmtMaterial();
            searchSmtMaterial.setMaterialCode(sorting.getMaterialCode());
            List<SmtMaterial> smtMaterials = basicFeignApi.findSmtMaterialList(searchSmtMaterial).getData();
            if (StringUtils.isEmpty(smtMaterials)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "没有找到对应物料信息");
            }
            SearchSmtElectronicTagStorage searchSmtElectronicTagStorage = new SearchSmtElectronicTagStorage();
            searchSmtElectronicTagStorage.setMaterialId(smtMaterials.get(0).getMaterialId().toString());
            List<SmtElectronicTagStorageDto> smtElectronicTagStorageDtoList = electronicTagFeignApi.findElectronicTagStorageList(searchSmtElectronicTagStorage).getData();

            if (StringUtils.isEmpty(smtElectronicTagStorageDtoList)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "请先维护储位对应的电子标签信息");
            }
            if (StringUtils.isEmpty(smtElectronicTagStorageDtoList.get(0).getStorageCode())) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "没有找到物料对应的储位信息");
            }
            smtElectronicTagStorageDtoList.get(0).setQuantity(sorting.getQuantity());
            list.add(smtElectronicTagStorageDtoList.get(0));
            sorting.setStatus((byte) 1);
        }
        electronicTagFeignApi.batchInsertSmtSorting(sortingList).getCount();
        //不同的标签可能对应的队列不一样，最终一条一条发给客户端
        for (SmtElectronicTagStorageDto smtElectronicTagStorageDto : list) {
            MQResponseEntity mQResponseEntity = new MQResponseEntity<>();
            mQResponseEntity.setCode(1001);
            mQResponseEntity.setData(smtElectronicTagStorageDto);
            log.info("===========开始发送消息给客户端===============");
            fanoutSender.send(smtElectronicTagStorageDto.getQueueName(),
                    JSONObject.toJSONString(mQResponseEntity));
            log.info("===========队列名称:" + smtElectronicTagStorageDto.getQueueName());
            log.info("===========消息内容:" + JSONObject.toJSONString(mQResponseEntity));
            log.info("===========发送消息给客户端完成===============");
        }
        return 1;
    }

    @Override
    public SmtElectronicTagStorageDto sendPlaceMaterials(String materialCode) {
        if (StringUtils.isEmpty(materialCode)) {
            throw new BizErrorException(ErrorCodeEnum.GL99990100);
        }
        SearchSmtStorageMaterial searchSmtStorageMaterial = new SearchSmtStorageMaterial();
        searchSmtStorageMaterial.setMaterialCode(materialCode);
        List<SmtStorageMaterial> storageMaterialList = basicFeignApi.findStorageMaterialList(searchSmtStorageMaterial).getData();
        if (StringUtils.isEmpty(storageMaterialList)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "没有找到对应物料信息");
        }

        SearchSmtElectronicTagStorage searchSmtElectronicTagStorage = new SearchSmtElectronicTagStorage();
        searchSmtElectronicTagStorage.setMaterialId(storageMaterialList.get(0).getMaterialId().toString());
        List<SmtElectronicTagStorageDto> smtElectronicTagStorageDtoList = electronicTagFeignApi.findElectronicTagStorageList(searchSmtElectronicTagStorage).getData();
        if (StringUtils.isEmpty(smtElectronicTagStorageDtoList)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "请先维护物料对应储位的信息");
        }
        SmtPaddingMaterial smtPaddingMaterial = new SmtPaddingMaterial();
        smtPaddingMaterial.setMaterialCode(materialCode);
        smtPaddingMaterial.setQuantity(BigDecimal.ZERO);
        smtPaddingMaterial.setStatus((byte) 2);
        smtPaddingMaterial.setPaddingMaterialCode(CodeUtils.getScheduleNo("PM-"));
        electronicTagFeignApi.addPaddingMaterial(smtPaddingMaterial);
//        MQResponseEntity mQResponseEntity =  new MQResponseEntity<>();
//        mQResponseEntity.setCode(1001);
//        smtElectronicTagStorageDtoList.get(0).setQuantity(BigDecimal.ZERO);
//        mQResponseEntity.setData(smtElectronicTagStorageDtoList.get(0));
//        fanoutSender.send(smtElectronicTagStorageDtoList.get(0).getQueueName(),
//                JSONObject.toJSONString(mQResponseEntity));
        return smtElectronicTagStorageDtoList.get(0);
    }

    @Override
    public int batchSortingDelete(List<String> sortingCodes) {
        electronicTagFeignApi.batchDeleteSorting(sortingCodes);
        return 1;
    }
}
