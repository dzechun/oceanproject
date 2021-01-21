package com.fantechs.provider.mes.pm.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.basic.SmtStorageMaterial;
import com.fantechs.common.base.entity.basic.search.SearchSmtStorageMaterial;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.mes.pm.MesPmMatchingOrderDto;
import com.fantechs.common.base.general.dto.mes.pm.SaveMesPmMatchingOrderDto;
import com.fantechs.common.base.general.dto.qms.QmsQualityConfirmationDto;
import com.fantechs.common.base.general.entity.mes.pm.MesPmMatchingOrder;
import com.fantechs.common.base.general.entity.qms.search.SearchQmsQualityConfirmation;
import com.fantechs.common.base.general.entity.wms.in.WmsInFinishedProduct;
import com.fantechs.common.base.general.entity.wms.in.WmsInFinishedProductDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.imes.basic.BasicFeignApi;
import com.fantechs.provider.api.mes.pm.PMFeignApi;
import com.fantechs.provider.api.qms.QmsFeignApi;
import com.fantechs.provider.api.wms.in.InFeignApi;
import com.fantechs.provider.mes.pm.mapper.MesPmMatchingOrderMapper;
import com.fantechs.provider.mes.pm.service.MesPmMatchingOrderService;
import io.swagger.models.auth.In;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by leifengzhi on 2021/01/19.
 */
@Service
public class MesPmMatchingOrderServiceImpl extends BaseService<MesPmMatchingOrder> implements MesPmMatchingOrderService {

    @Resource
    private MesPmMatchingOrderMapper mesPmMatchingOrderMapper;
    @Resource
    private PMFeignApi pmFeignApi;
    @Resource
    private InFeignApi inFeignApi;
    @Resource
    private BasicFeignApi basicFeignApi;
    @Resource
    private QmsFeignApi qmsFeignApi;

    @Override
    public List<MesPmMatchingOrderDto> findList(Map<String, Object> map) {
        return mesPmMatchingOrderMapper.findList(map);
    }

    @Override
    public MesPmMatchingOrderDto findMinMatchingQuantity(String workOrderCardId) {

        //获取工单流转卡和部件的流转卡
        SearchQmsQualityConfirmation searchQmsQualityConfirmation = new SearchQmsQualityConfirmation();
        searchQmsQualityConfirmation.setParentWorkOrderCardPoolCode(workOrderCardId);
        List<QmsQualityConfirmationDto> qmsQualityConfirmationDtos = qmsFeignApi.findQualityConfirmationList(searchQmsQualityConfirmation).getData();
        MesPmMatchingOrderDto mesPmMatchingOrderDto = new MesPmMatchingOrderDto();
        BeanUtils.copyProperties(qmsQualityConfirmationDtos.get(0), mesPmMatchingOrderDto);

        List<BigDecimal> minMatchingQuantitys = new ArrayList<>();//保存最小齐套数量集合

        for (QmsQualityConfirmationDto qmsQualityConfirmationDto : qmsQualityConfirmationDtos) {
            //获取部件用量
            BigDecimal dosage = qmsQualityConfirmationDto.getDosage();
            //报工数量
            BigDecimal quantity = qmsQualityConfirmationDto.getQuantity();
            if (dosage.equals(BigDecimal.ZERO)) {
                throw new BizErrorException("部件用量不能为0");
            }
            BigDecimal minMatchingQuantity = quantity.divide(dosage); //最小齐套数
            minMatchingQuantitys.add(minMatchingQuantity);
        }
        //获取最小齐套数
        BigDecimal min = Collections.min(minMatchingQuantitys);
        mesPmMatchingOrderDto.setMinMatchingQuantity(min);
        return mesPmMatchingOrderDto;
    }

    @Override
    public int save(SaveMesPmMatchingOrderDto saveMesPmMatchingOrderDto) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        //判断配套数量是否大于最小齐套数量
        if (saveMesPmMatchingOrderDto.getMatchingQuantity().compareTo(saveMesPmMatchingOrderDto.getMinMatchingQuantity()) == 1) {
            throw new BizErrorException("配套数量不能大于最小齐套数");
        }

        //判断该配套单是否存在
        Example example = new Example(MesPmMatchingOrder.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("workOrderCardPoolId", saveMesPmMatchingOrderDto.getWorkOrderCardPoolId());
        MesPmMatchingOrder mesPmMatchingOrder1 = mesPmMatchingOrderMapper.selectOneByExample(example);

        MesPmMatchingOrder mesPmMatchingOrder = new MesPmMatchingOrder();
        BeanUtils.copyProperties(saveMesPmMatchingOrderDto,mesPmMatchingOrder);

        if (StringUtils.isNotEmpty(mesPmMatchingOrder1)) {
            mesPmMatchingOrder.setModifiedUserId(currentUser.getUserId());
            mesPmMatchingOrder.setModifiedTime(new Date());
            return mesPmMatchingOrderMapper.updateByPrimaryKeySelective(mesPmMatchingOrder);
        }
        mesPmMatchingOrder.setMatchingOrderCode(CodeUtils.getId("PT"));
        mesPmMatchingOrder.setCreateTime(new Date());
        mesPmMatchingOrder.setCreateUserId(currentUser.getUserId());
        mesPmMatchingOrder.setModifiedTime(new Date());
        mesPmMatchingOrder.setModifiedUserId(currentUser.getUserId());
        mesPmMatchingOrder.setOrganizationId(currentUser.getOrganizationId());
        int i = mesPmMatchingOrderMapper.insertUseGeneratedKeys(mesPmMatchingOrder);

        //新增完工入库单
        if (StringUtils.isNotEmpty(saveMesPmMatchingOrderDto.getStatus()) && saveMesPmMatchingOrderDto.getStatus() == 3){
            WmsInFinishedProduct wmsInFinishedProduct = new WmsInFinishedProduct();//入库单
            WmsInFinishedProductDet wmsInFinishedProductDet = new WmsInFinishedProductDet();//入库单明细
            List<WmsInFinishedProductDet> wmsInFinishedProductDets = new ArrayList<>();

            wmsInFinishedProduct.setWorkOrderId(saveMesPmMatchingOrderDto.getWorkOrderId());
            wmsInFinishedProduct.setOperatorUserId(currentUser.getUserId());
            wmsInFinishedProduct.setInTime(new Date());
            wmsInFinishedProduct.setInType((byte) 1);
            wmsInFinishedProduct.setInType((byte) 2);
            wmsInFinishedProduct.setStatus((byte) 1);
            wmsInFinishedProduct.setCreateTime(new Date());
            wmsInFinishedProduct.setCreateUserId(currentUser.getUserId());
            wmsInFinishedProduct.setModifiedTime(new Date());
            wmsInFinishedProduct.setOrganizationId(currentUser.getOrganizationId());
            wmsInFinishedProduct.setModifiedUserId(currentUser.getUserId());
            wmsInFinishedProductDet.setMaterialId(saveMesPmMatchingOrderDto.getMaterialId());
            //获取储位ID
            SearchSmtStorageMaterial searchSmtStorageMaterial = new SearchSmtStorageMaterial();
            searchSmtStorageMaterial.setMaterialId(saveMesPmMatchingOrderDto.getMaterialId());
            SmtStorageMaterial smtStorageMaterial = basicFeignApi.findStorageMaterialList(searchSmtStorageMaterial).getData().get(0);

            wmsInFinishedProductDet.setStorageId(smtStorageMaterial.getStorageId());
            wmsInFinishedProductDet.setPlanInQuantity(saveMesPmMatchingOrderDto.getMatchingQuantity());
            wmsInFinishedProductDet.setInQuantity(saveMesPmMatchingOrderDto.getMatchingQuantity());
            wmsInFinishedProductDet.setInTime(new Date());
            wmsInFinishedProductDet.setDeptId(currentUser.getDeptId());
            wmsInFinishedProductDet.setInStatus((byte) 2);
            wmsInFinishedProductDet.setOrganizationId(currentUser.getOrganizationId());
            wmsInFinishedProductDets.add(wmsInFinishedProductDet);

            wmsInFinishedProduct.setWmsInFinishedProductDetList(wmsInFinishedProductDets);
            inFeignApi.inFinishedProductAdd(wmsInFinishedProduct);
        }
        return i;
    }
}
