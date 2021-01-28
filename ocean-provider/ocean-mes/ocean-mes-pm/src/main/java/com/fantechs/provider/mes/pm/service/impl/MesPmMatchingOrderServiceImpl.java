package com.fantechs.provider.mes.pm.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.basic.SmtStorageMaterial;
import com.fantechs.common.base.entity.basic.search.SearchSmtStorageMaterial;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.mes.pm.MesPmMatchingOrderDto;
import com.fantechs.common.base.general.dto.mes.pm.ProcessListWorkOrderDTO;
import com.fantechs.common.base.general.dto.mes.pm.SaveMesPmMatchingOrderDto;
import com.fantechs.common.base.general.dto.mes.pm.SmtWorkOrderCardPoolDto;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtWorkOrder;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtWorkOrderCardPool;
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
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
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

        //通过流转卡号获取流转卡信息
        ProcessListWorkOrderDTO processListWorkOrderDTO = pmFeignApi.selectWorkOrderDtoByWorkOrderCardId(workOrderCardId).getData();
        if (StringUtils.isEmpty(processListWorkOrderDTO)){
            throw new BizErrorException("无法获取该流转卡的相应信息");
        }

        //父id为0则该流转卡为工单流转卡
        if (0 == processListWorkOrderDTO.getParentId()){

            List<BigDecimal> minMatchingQuantitys = new ArrayList<>();//保存最小齐套数量集合

            //1.通过工单流转卡id获取部件流转卡id
            SearchSmtWorkOrderCardPool searchSmtWorkOrderCardPool = new SearchSmtWorkOrderCardPool();
            searchSmtWorkOrderCardPool.setParentId(processListWorkOrderDTO.getWorkOrderCardPoolId());
            List<SmtWorkOrderCardPoolDto> smtWorkOrderCardPoolDtos = pmFeignApi.findWorkOrderCardPoolList(searchSmtWorkOrderCardPool).getData();

            for (SmtWorkOrderCardPoolDto smtWorkOrderCardPoolDto : smtWorkOrderCardPoolDtos) {
                //通过部件流转卡ID获取部件配套单
                MesPmMatchingOrder mesPmMatchingOrder = mesPmMatchingOrderMapper.selectByPrimaryKey(smtWorkOrderCardPoolDto.getWorkOrderCardPoolId());
                minMatchingQuantitys.add(mesPmMatchingOrder.getMinMatchingQuantity());

            }
            //获取最小齐套数
            BigDecimal min = Collections.min(minMatchingQuantitys);
            MesPmMatchingOrderDto mesPmMatchingOrderDto = new MesPmMatchingOrderDto();//保存成品的配套单
            BeanUtils.copyProperties(processListWorkOrderDTO, mesPmMatchingOrderDto);
            mesPmMatchingOrderDto.setMinMatchingQuantity(min);
            return mesPmMatchingOrderDto;
        }else {
            //获取部件的流转卡
            SearchQmsQualityConfirmation searchQmsQualityConfirmation = new SearchQmsQualityConfirmation();
            searchQmsQualityConfirmation.setParentWorkOrderCardPoolCode(workOrderCardId);
            List<QmsQualityConfirmationDto> qmsQualityConfirmationDtos = qmsFeignApi.findQualityConfirmationList(searchQmsQualityConfirmation).getData();
            QmsQualityConfirmationDto qmsQualityConfirmationDto1 = qmsQualityConfirmationDtos.get(0);
            if (StringUtils.isEmpty(qmsQualityConfirmationDto1)){
                throw new BizErrorException("流转卡号对应的流转卡不存在");
            }

            MesPmMatchingOrderDto mesPmMatchingOrderDto = new MesPmMatchingOrderDto();//部件的配套单
            BeanUtils.copyProperties(qmsQualityConfirmationDto1, mesPmMatchingOrderDto);

            for (QmsQualityConfirmationDto qmsQualityConfirmationDto : qmsQualityConfirmationDtos) {
                //获取部件用量
                BigDecimal dosage = qmsQualityConfirmationDto.getDosage();

                //部件用量必须大于0
                if (dosage == null || BigDecimal.valueOf(0).compareTo(dosage) == 0 || BigDecimal.valueOf(0).compareTo(dosage) == 1) {
                    throw new BizErrorException("部件用量必须大于0");
                }

                //合格数量
                BigDecimal qualifiedQuantity = qmsQualityConfirmationDto.getQualifiedQuantity();
                if (qualifiedQuantity.compareTo(dosage) == -1) {
                    throw new BizErrorException("质检合格数不足一套");
                }
                if (dosage.equals(BigDecimal.ZERO)) {
                    throw new BizErrorException("部件用量不能为0");
                }

                BigDecimal minMatchingQuantity = qualifiedQuantity.divide(qualifiedQuantity, 0, RoundingMode.HALF_UP); //最小齐套数

                mesPmMatchingOrderDto.setMinMatchingQuantity(minMatchingQuantity);
            }

            return mesPmMatchingOrderDto;
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(SaveMesPmMatchingOrderDto saveMesPmMatchingOrderDto) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        int i = 0;
        //判断配套数量是否大于最小齐套数量
        if (saveMesPmMatchingOrderDto.getMatchingQuantity().compareTo(saveMesPmMatchingOrderDto.getMinMatchingQuantity()) == 1) {
            throw new BizErrorException("配套数量不能大于最小齐套数");
        }

        Example example = new Example(MesPmMatchingOrder.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("workOrderCardPoolId", saveMesPmMatchingOrderDto.getWorkOrderCardPoolId());
        MesPmMatchingOrder mesPmMatchingOrder1 = mesPmMatchingOrderMapper.selectOneByExample(example);
        MesPmMatchingOrder mesPmMatchingOrder = new MesPmMatchingOrder();

        //该配套单存在
        if (StringUtils.isNotEmpty(mesPmMatchingOrder1)) {
            //该配套单还未提交
            if (mesPmMatchingOrder1.getStatus() == 1) {
                BigDecimal minMatchingQuantity = mesPmMatchingOrder1.getMinMatchingQuantity();//获取原来的最小齐套数
                BigDecimal matchingQuantity = mesPmMatchingOrder1.getMatchingQuantity();//获取原配套数

                //更新最小齐套数和原配套数
                BigDecimal newMinMatchingQuantity = minMatchingQuantity.subtract(saveMesPmMatchingOrderDto.getMatchingQuantity());
                BigDecimal newMatchingQuantity = matchingQuantity.add(saveMesPmMatchingOrderDto.getMatchingQuantity());

                mesPmMatchingOrder1.setMinMatchingQuantity(newMinMatchingQuantity);
                mesPmMatchingOrder1.setMatchingQuantity(newMatchingQuantity);
                mesPmMatchingOrder1.setModifiedUserId(currentUser.getUserId());
                mesPmMatchingOrder1.setModifiedTime(new Date());
                if (saveMesPmMatchingOrderDto.getStatus() == 1){
                    mesPmMatchingOrder1.setStatus((byte) 1);
                }else {
                    mesPmMatchingOrder1.setStatus((byte) 2);
                }
                return mesPmMatchingOrderMapper.updateByPrimaryKeySelective(mesPmMatchingOrder1);
            }else {
                throw new BizErrorException("该配套单已经配套完成");
            }
        }

        mesPmMatchingOrder.setMatchingOrderCode(CodeUtils.getId("PT"));
        mesPmMatchingOrder.setCreateTime(new Date());
        mesPmMatchingOrder.setCreateUserId(currentUser.getUserId());
        mesPmMatchingOrder.setModifiedTime(new Date());
        mesPmMatchingOrder.setModifiedUserId(currentUser.getUserId());
        mesPmMatchingOrder.setOrganizationId(currentUser.getOrganizationId());
        if (saveMesPmMatchingOrderDto.getStatus() == 1){
            mesPmMatchingOrder.setStatus((byte) 1);
        }else {
            mesPmMatchingOrder.setStatus((byte) 2);
        }
        mesPmMatchingOrder.setWorkOrderCardPoolId(saveMesPmMatchingOrderDto.getWorkOrderCardPoolId());
        mesPmMatchingOrder.setWorkOrderQuantity(saveMesPmMatchingOrderDto.getWorkOrderQuantity());
        mesPmMatchingOrder.setProductionQuantity(saveMesPmMatchingOrderDto.getProductionQuantity());
        mesPmMatchingOrder.setMinMatchingQuantity(saveMesPmMatchingOrderDto.getMinMatchingQuantity().subtract(saveMesPmMatchingOrderDto.getMatchingQuantity()));
        mesPmMatchingOrder.setMatchingQuantity(saveMesPmMatchingOrderDto.getMatchingQuantity());

        i = mesPmMatchingOrderMapper.insertUseGeneratedKeys(mesPmMatchingOrder);

        //新增完工入库单
        if (StringUtils.isNotEmpty(saveMesPmMatchingOrderDto.getStatus()) && saveMesPmMatchingOrderDto.getStatus() == 2) {
            WmsInFinishedProduct wmsInFinishedProduct = new WmsInFinishedProduct();//入库单
            WmsInFinishedProductDet wmsInFinishedProductDet = new WmsInFinishedProductDet();//入库单明细
            List<WmsInFinishedProductDet> wmsInFinishedProductDets = new ArrayList<>();

            wmsInFinishedProduct.setWorkOrderId(saveMesPmMatchingOrderDto.getWorkOrderId());
            wmsInFinishedProduct.setOperatorUserId(currentUser.getUserId());
            wmsInFinishedProduct.setInType((byte) 0);
            wmsInFinishedProduct.setStatus((byte) 1);
            wmsInFinishedProduct.setCreateTime(new Date());
            wmsInFinishedProduct.setCreateUserId(currentUser.getUserId());
            wmsInFinishedProduct.setModifiedTime(new Date());
            wmsInFinishedProduct.setOrganizationId(currentUser.getOrganizationId());
            wmsInFinishedProduct.setModifiedUserId(currentUser.getUserId());
            wmsInFinishedProduct.setProjectType("dp");
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
