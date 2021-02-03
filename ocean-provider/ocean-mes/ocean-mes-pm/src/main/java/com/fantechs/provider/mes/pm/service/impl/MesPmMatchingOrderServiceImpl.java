package com.fantechs.provider.mes.pm.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.basic.SmtStorageMaterial;
import com.fantechs.common.base.entity.basic.search.SearchSmtStorageMaterial;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BasePlatePartsDetDto;
import com.fantechs.common.base.general.dto.mes.pm.*;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtWorkOrderCardPool;
import com.fantechs.common.base.general.entity.basic.search.SearchBasePlatePartsDet;
import com.fantechs.common.base.general.entity.mes.pm.MesPmMatching;
import com.fantechs.common.base.general.entity.mes.pm.MesPmMatchingDet;
import com.fantechs.common.base.general.entity.mes.pm.MesPmMatchingOrder;
import com.fantechs.common.base.general.entity.qms.QmsQualityConfirmation;
import com.fantechs.common.base.general.entity.wms.in.WmsInFinishedProduct;
import com.fantechs.common.base.general.entity.wms.in.WmsInFinishedProductDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.imes.basic.BasicFeignApi;
import com.fantechs.provider.api.qms.QmsFeignApi;
import com.fantechs.provider.api.wms.in.InFeignApi;
import com.fantechs.provider.mes.pm.mapper.MesPmMatchingDetMapper;
import com.fantechs.provider.mes.pm.mapper.MesPmMatchingMapper;
import com.fantechs.provider.mes.pm.mapper.MesPmMatchingOrderMapper;
import com.fantechs.provider.mes.pm.service.MesPmMatchingOrderService;
import com.fantechs.provider.mes.pm.service.SmtWorkOrderCardPoolService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by leifengzhi on 2021/01/19.
 */
@Service
public class MesPmMatchingOrderServiceImpl extends BaseService<MesPmMatchingOrder> implements MesPmMatchingOrderService {

    @Resource
    private MesPmMatchingOrderMapper mesPmMatchingOrderMapper;
    @Resource
    private InFeignApi inFeignApi;
    @Resource
    private BasicFeignApi basicFeignApi;
    @Resource
    private QmsFeignApi qmsFeignApi;
    @Resource
    private SmtWorkOrderCardPoolService smtWorkOrderCardPoolService;
    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private MesPmMatchingMapper mesPmMatchingMapper;
    @Resource
    private MesPmMatchingDetMapper mesPmMatchingDetMapper;

    @Override
    public List<MesPmMatchingOrderDto> findList(Map<String, Object> map) {
        return mesPmMatchingOrderMapper.findList(map);
    }

    @Override
    public MesPmMatchingDto findMinMatchingQuantity(String workOrderCardId) {

        //通过流转卡号获取流转卡信息
        ProcessListWorkOrderDTO processListWorkOrderDTO = smtWorkOrderCardPoolService.selectWorkOrderDtoByWorkOrderCardId(workOrderCardId);
        if (StringUtils.isEmpty(processListWorkOrderDTO)) {
            throw new BizErrorException("无法获取该流转卡的相应信息");
        }


        //父id为0则该流转卡为工单流转卡
        if (0 == processListWorkOrderDTO.getParentId()) {

            //通过工单流转卡id获取部件流转卡信息
            SearchSmtWorkOrderCardPool searchSmtWorkOrderCardPool = new SearchSmtWorkOrderCardPool();
            searchSmtWorkOrderCardPool.setParentId(processListWorkOrderDTO.getWorkOrderCardPoolId());
            List<SmtWorkOrderCardPoolDto> smtWorkOrderCardPoolDtos = smtWorkOrderCardPoolService.findList(searchSmtWorkOrderCardPool);

            //根据部件工单ID分组，一个部件工单对应一个部件
            Map<Long, List<SmtWorkOrderCardPoolDto>> collect = smtWorkOrderCardPoolDtos.stream().collect(Collectors.groupingBy(SmtWorkOrderCardPoolDto::getWorkOrderId));
            Set<Long> keySet = collect.keySet();

            List<BigDecimal> minMatchingQuantitys = new ArrayList<>();//保存最小齐套数量集合
            for (Long workOrderId : keySet) {
                //同属一个部件的流转卡
                List<SmtWorkOrderCardPoolDto> smtWorkOrderCardPoolDtos1 = collect.get(workOrderId);

                BigDecimal qualifiedQuantity = BigDecimal.valueOf(0);//保存同一部件的质检合格数
                for (SmtWorkOrderCardPoolDto smtWorkOrderCardPoolDto : smtWorkOrderCardPoolDtos1) {
                    //通过部件流转卡ID获取质检单
                    QmsQualityConfirmation qmsQualityConfirmation = qmsFeignApi.getQualityQuantity(smtWorkOrderCardPoolDto.getWorkOrderCardPoolId(), (long) 0).getData();
                    if (StringUtils.isNotEmpty(qmsQualityConfirmation)) {
                        qualifiedQuantity = qualifiedQuantity.add(qmsQualityConfirmation.getQualifiedQuantity());
                    }
                }

                //判断该部件的配套明细是否存在
                Example example = new Example(MesPmMatchingDet.class);
                Example.Criteria criteria = example.createCriteria();
                criteria.andEqualTo("workOrderId", workOrderId);
                MesPmMatchingDet mesPmMatchingDet = mesPmMatchingDetMapper.selectOneByExample(example);
                if (StringUtils.isNotEmpty(mesPmMatchingDet)) {
                    //获取余料
                    BigDecimal remainingQuantity = mesPmMatchingDet.getQualifiedQuantity().subtract(mesPmMatchingDet.getUsedQuantity());
                    qualifiedQuantity = qualifiedQuantity.add(remainingQuantity);
                }

                //获取部件信息
                SmtWorkOrderCardPoolDto smtWorkOrderCardPoolDto = smtWorkOrderCardPoolDtos1.get(0);
                SearchBasePlatePartsDet searchBasePlatePartsDet = new SearchBasePlatePartsDet();
                searchBasePlatePartsDet.setPlatePartsDetId(smtWorkOrderCardPoolDto.getMaterialId());
                List<BasePlatePartsDetDto> basePlatePartsDetDtos = baseFeignApi.findPlatePartsDetList(searchBasePlatePartsDet).getData();
                if (StringUtils.isEmpty(basePlatePartsDetDtos)) {
                    throw new BizErrorException("无法获取到部件信息");
                }

                BigDecimal quantity = basePlatePartsDetDtos.get(0).getQuantity();//部件用量
                if (quantity == null || BigDecimal.valueOf(0).compareTo(quantity) == 0 || BigDecimal.valueOf(0).compareTo(quantity) == 1) {
                    throw new BizErrorException("部件用量必须大于0");
                }

                if (qualifiedQuantity.compareTo(quantity) == -1) {
                    BigDecimal minMatchingQuantity = BigDecimal.valueOf(0);
                    minMatchingQuantitys.add(minMatchingQuantity);
                    continue;
                } else {
                    BigDecimal minMatchingQuantity = qualifiedQuantity.divide(quantity, 0, RoundingMode.HALF_UP); //最小齐套数
                    minMatchingQuantitys.add(minMatchingQuantity);
                }

            }

            MesPmMatchingDto mesPmMatchingDto = new MesPmMatchingDto();//配套信息ID

            //筛选最小齐套数
            BigDecimal min = BigDecimal.valueOf(0);
            if (StringUtils.isNotEmpty(minMatchingQuantitys)) {
                min = Collections.min(minMatchingQuantitys);
            }

            //获取已配套数量
            Example example = new Example(MesPmMatching.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("workOrderId", processListWorkOrderDTO.getWorkOrderId());
            MesPmMatching mesPmMatching = mesPmMatchingMapper.selectOneByExample(example);
            if (StringUtils.isNotEmpty(mesPmMatching)) {
                BigDecimal alreadyMatchingQuantity = mesPmMatching.getAlreadyMatchingQuantity();
                mesPmMatchingDto.setAlreadyMatchingQuantity(alreadyMatchingQuantity);
            }
            mesPmMatchingDto.setMinMatchingQuantity(min);
            mesPmMatchingDto.setMaterialId(processListWorkOrderDTO.getMaterialId());
            mesPmMatchingDto.setProductModuleName(processListWorkOrderDTO.getProductModuleName());
            mesPmMatchingDto.setWorkOrderId(processListWorkOrderDTO.getWorkOrderId());
            mesPmMatchingDto.setMaterialCode(processListWorkOrderDTO.getMaterialCode());
            mesPmMatchingDto.setMaterialDesc(processListWorkOrderDTO.getMaterialDesc());
            mesPmMatchingDto.setWorkOrderCardPoolId(processListWorkOrderDTO.getWorkOrderCardPoolId());
            mesPmMatchingDto.setWorkOrderId(processListWorkOrderDTO.getWorkOrderId());
            mesPmMatchingDto.setProductionQuantity(processListWorkOrderDTO.getProductionQuantity());
            mesPmMatchingDto.setWorkOrderQuantity(processListWorkOrderDTO.getWorkOrderQuantity());
            return mesPmMatchingDto;
        } else {
            throw new BizErrorException("请输入工单流转卡号");
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
            throw new BizErrorException("配套数量大于最小齐套数");
        }

        //判断配套信息是否存在
        Example example = new Example(MesPmMatching.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("workOrderId", saveMesPmMatchingOrderDto.getWorkOrderId());
        MesPmMatching mesPmMatching = mesPmMatchingMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(mesPmMatching)) {
            //判断配套数量和已配套数量是否大于最小齐套数量
            BigDecimal alreadyMatchingQuantity1 = mesPmMatching.getAlreadyMatchingQuantity();//已配套数量
            if (saveMesPmMatchingOrderDto
                    .getMatchingQuantity()
                    .add(alreadyMatchingQuantity1)
                    .compareTo(saveMesPmMatchingOrderDto.getMinMatchingQuantity()) == 1
            ) {
                throw new BizErrorException("配套数量大于最小齐套数量");
            }
            mesPmMatching.setMinMatchingQuantity(saveMesPmMatchingOrderDto.getMinMatchingQuantity());//更新最小齐套数
            //如果执行的是提交操作，则更新已配套数
            if (saveMesPmMatchingOrderDto.getStatus() == 2) {
                BigDecimal alreadyMatchingQuantity = mesPmMatching.getAlreadyMatchingQuantity();
                mesPmMatching.setAlreadyMatchingQuantity(alreadyMatchingQuantity.add(saveMesPmMatchingOrderDto.getMatchingQuantity()));
            }
            mesPmMatchingMapper.updateByPrimaryKeySelective(mesPmMatching);
        } else {
            mesPmMatching = new MesPmMatching();
            mesPmMatching.setWorkOrderId(saveMesPmMatchingOrderDto.getWorkOrderId());
            mesPmMatching.setWorkOrderQuantity(saveMesPmMatchingOrderDto.getWorkOrderQuantity());
            mesPmMatching.setProductionQuantity(saveMesPmMatchingOrderDto.getProductionQuantity());
            //如果执行的是提交操作，则更新已配套数
            if (saveMesPmMatchingOrderDto.getStatus() == 2) {
                mesPmMatching.setAlreadyMatchingQuantity(saveMesPmMatchingOrderDto.getMatchingQuantity());
            }
            mesPmMatching.setMinMatchingQuantity(saveMesPmMatchingOrderDto.getMinMatchingQuantity());
            mesPmMatching.setCreateTime(new Date());
            mesPmMatching.setCreateUserId(currentUser.getUserId());
            mesPmMatching.setModifiedTime(new Date());
            mesPmMatching.setMatchingId(currentUser.getUserId());
            mesPmMatchingMapper.insertUseGeneratedKeys(mesPmMatching);
        }

        //通过工单流转卡id获取部件流转卡信息
        SearchSmtWorkOrderCardPool searchSmtWorkOrderCardPool = new SearchSmtWorkOrderCardPool();
        searchSmtWorkOrderCardPool.setParentId(saveMesPmMatchingOrderDto.getWorkOrderCardPoolId());
        List<SmtWorkOrderCardPoolDto> smtWorkOrderCardPoolDtos = smtWorkOrderCardPoolService.findList(searchSmtWorkOrderCardPool);

        //根据部件工单ID分组，一个部件工单对应一个部件
        Map<Long, List<SmtWorkOrderCardPoolDto>> collect = smtWorkOrderCardPoolDtos.stream().collect(Collectors.groupingBy(SmtWorkOrderCardPoolDto::getWorkOrderId));
        Set<Long> keySet = collect.keySet();


        for (Long workOrderId : keySet) {
            //同属一个部件的流转卡
            List<SmtWorkOrderCardPoolDto> smtWorkOrderCardPoolDtos1 = collect.get(workOrderId);

            BigDecimal qualifiedQuantity = BigDecimal.valueOf(0);//保存同一部件的质检合格数
            for (SmtWorkOrderCardPoolDto smtWorkOrderCardPoolDto : smtWorkOrderCardPoolDtos1) {
                //通过部件流转卡ID获取质检单
                QmsQualityConfirmation qmsQualityConfirmation = qmsFeignApi.getQualityQuantity(smtWorkOrderCardPoolDto.getWorkOrderCardPoolId(),null).getData();
                if (StringUtils.isNotEmpty(qmsQualityConfirmation)) {
                    qualifiedQuantity = qualifiedQuantity.add(qmsQualityConfirmation.getQualifiedQuantity());
                }

                //获取部件信息
                SmtWorkOrderCardPoolDto smtWorkOrderCardPoolDto1 = smtWorkOrderCardPoolDtos1.get(0);
                SearchBasePlatePartsDet searchBasePlatePartsDet = new SearchBasePlatePartsDet();
                searchBasePlatePartsDet.setPlatePartsDetId(smtWorkOrderCardPoolDto1.getMaterialId());
                List<BasePlatePartsDetDto> basePlatePartsDetDtos = baseFeignApi.findPlatePartsDetList(searchBasePlatePartsDet).getData();
                if (StringUtils.isEmpty(basePlatePartsDetDtos)) {
                    throw new BizErrorException("无法获取到部件信息");
                }

                BigDecimal quantity = basePlatePartsDetDtos.get(0).getQuantity();//部件用量
                if (quantity == null || BigDecimal.valueOf(0).compareTo(quantity) == 0 || BigDecimal.valueOf(0).compareTo(quantity) == 1) {
                    throw new BizErrorException("部件用量必须大于0");
                }

                //判断配套信息明细是否存在
                Example example1 = new Example(MesPmMatchingDet.class);
                Example.Criteria criteria1 = example1.createCriteria();
                criteria1.andEqualTo("workOrderId", workOrderId);
                MesPmMatchingDet mesPmMatchingDet = mesPmMatchingDetMapper.selectOneByExample(example1);
                if (StringUtils.isNotEmpty(mesPmMatchingDet)) {
                    mesPmMatchingDet.setMatchingId(mesPmMatching.getMatchingId());
                    mesPmMatchingDet.setQualifiedQuantity(qualifiedQuantity);//更新部件质检合格数
                    //根据配套数量更新合格部件使用数量
                    BigDecimal matchingQuantity = saveMesPmMatchingOrderDto.getMatchingQuantity();//配套数量
                    BigDecimal usedQuantity = mesPmMatchingDet.getUsedQuantity();//已使用合格部件数量
                    usedQuantity = usedQuantity.add(quantity.multiply(matchingQuantity));
                    mesPmMatchingDet.setUsedQuantity(usedQuantity);
                    mesPmMatchingDet.setWorkOrderId(workOrderId);
                    mesPmMatchingDet.setCreateTime(new Date());
                    mesPmMatchingDet.setCreateUserId(currentUser.getUserId());
                    mesPmMatchingDet.setModifiedTime(new Date());
                    mesPmMatchingDet.setModifiedUserId(currentUser.getUserId());
                    mesPmMatchingDetMapper.updateByPrimaryKeySelective(mesPmMatchingDet);
                } else {
                    mesPmMatchingDet = new MesPmMatchingDet();
                    mesPmMatchingDet.setMatchingId(mesPmMatching.getMatchingId());
                    mesPmMatchingDet.setQualifiedQuantity(qualifiedQuantity);
                    //根据配套数量更新合格部件使用数量
                    BigDecimal matchingQuantity = saveMesPmMatchingOrderDto.getMatchingQuantity();//配套数量
                    mesPmMatchingDet.setUsedQuantity(quantity.multiply(matchingQuantity));
                    mesPmMatchingDet.setWorkOrderId(workOrderId);
                    mesPmMatchingDet.setCreateTime(new Date());
                    mesPmMatchingDet.setCreateUserId(currentUser.getUserId());
                    mesPmMatchingDet.setModifiedTime(new Date());
                    mesPmMatchingDet.setModifiedUserId(currentUser.getUserId());
                    mesPmMatchingDetMapper.insertUseGeneratedKeys(mesPmMatchingDet);
                }
            }


            //判断配套单是否存在
            Example example2 = new Example(MesPmMatchingOrder.class);
            Example.Criteria criteria2 = example2.createCriteria();
            criteria2.andEqualTo("workOrderCardPoolId", saveMesPmMatchingOrderDto.getWorkOrderCardPoolId())
                    .andEqualTo("status", 1);
            MesPmMatchingOrder mesPmMatchingOrder1 = mesPmMatchingOrderMapper.selectOneByExample(example2);

            MesPmMatchingOrder mesPmMatchingOrder = new MesPmMatchingOrder();
            //该配套单存在
            if (StringUtils.isNotEmpty(mesPmMatchingOrder1)) {
                //该配套单还未提交
                if (mesPmMatchingOrder1.getStatus() == 1) {
                    mesPmMatchingOrder1.setMatchingQuantity(saveMesPmMatchingOrderDto.getMatchingQuantity());
                    mesPmMatchingOrder1.setModifiedUserId(currentUser.getUserId());
                    mesPmMatchingOrder1.setModifiedTime(new Date());
                    if (saveMesPmMatchingOrderDto.getStatus() == 1) {
                        mesPmMatchingOrder1.setStatus((byte) 1);
                    } else {
                        mesPmMatchingOrder1.setStatus((byte) 2);
                    }
                    return mesPmMatchingOrderMapper.updateByPrimaryKeySelective(mesPmMatchingOrder1);
                } else {
                    throw new BizErrorException("该配套单已经配套完成");
                }
            }

            mesPmMatchingOrder.setMatchingQuantity(saveMesPmMatchingOrderDto.getMatchingQuantity());
            mesPmMatchingOrder.setMatchingOrderCode(CodeUtils.getId("PT"));
            mesPmMatchingOrder.setCreateTime(new Date());
            mesPmMatchingOrder.setCreateUserId(currentUser.getUserId());
            mesPmMatchingOrder.setModifiedTime(new Date());
            mesPmMatchingOrder.setModifiedUserId(currentUser.getUserId());
            mesPmMatchingOrder.setOrganizationId(currentUser.getOrganizationId());
            mesPmMatchingOrder.setMatchingId(mesPmMatching.getMatchingId());
            if (saveMesPmMatchingOrderDto.getStatus() == 1) {
                mesPmMatchingOrder.setStatus((byte) 1);
            } else {
                mesPmMatchingOrder.setStatus((byte) 2);
            }
            mesPmMatchingOrder.setWorkOrderCardPoolId(saveMesPmMatchingOrderDto.getWorkOrderCardPoolId());

            i = mesPmMatchingOrderMapper.insertUseGeneratedKeys(mesPmMatchingOrder);
        }
        //新增完工入库单
        if (StringUtils.isNotEmpty(saveMesPmMatchingOrderDto.getStatus()) && saveMesPmMatchingOrderDto.getStatus() == 2) {
            WmsInFinishedProduct wmsInFinishedProduct = new WmsInFinishedProduct();//入库单
            WmsInFinishedProductDet wmsInFinishedProductDet = new WmsInFinishedProductDet();//入库单明细
            List<WmsInFinishedProductDet> wmsInFinishedProductDets = new ArrayList<>();

            wmsInFinishedProduct.setWorkOrderId(saveMesPmMatchingOrderDto.getWorkOrderId());
            wmsInFinishedProduct.setOperatorUserId(currentUser.getUserId());
            wmsInFinishedProduct.setInType((byte) 0);
            wmsInFinishedProduct.setInTime(new Date());
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
