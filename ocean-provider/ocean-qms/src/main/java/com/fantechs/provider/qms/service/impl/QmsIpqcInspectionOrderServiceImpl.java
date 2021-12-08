package com.fantechs.provider.qms.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderDto;
import com.fantechs.common.base.general.dto.qms.QmsIpqcInspectionOrderDetDto;
import com.fantechs.common.base.general.entity.basic.BaseInspectionStandard;
import com.fantechs.common.base.general.entity.basic.BaseInspectionWay;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseInspectionStandard;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseInspectionWay;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmWorkOrder;
import com.fantechs.common.base.general.entity.qms.QmsIpqcInspectionOrder;
import com.fantechs.common.base.general.entity.qms.QmsIpqcInspectionOrderDet;
import com.fantechs.common.base.general.entity.qms.QmsIpqcInspectionOrderDetSample;
import com.fantechs.common.base.general.entity.qms.history.QmsHtIpqcInspectionOrder;
import com.fantechs.common.base.general.entity.qms.search.SearchQmsIpqcInspectionOrder;
import com.fantechs.common.base.general.entity.qms.search.SearchQmsIpqcInspectionOrderDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.fileserver.service.FileFeignApi;
import com.fantechs.provider.api.mes.pm.PMFeignApi;
import com.fantechs.provider.qms.mapper.QmsHtIpqcInspectionOrderMapper;
import com.fantechs.provider.qms.mapper.QmsIpqcInspectionOrderDetMapper;
import com.fantechs.provider.qms.mapper.QmsIpqcInspectionOrderDetSampleMapper;
import com.fantechs.provider.qms.mapper.QmsIpqcInspectionOrderMapper;
import com.fantechs.provider.qms.service.QmsIpqcInspectionOrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

/**
 *
 * Created by leifengzhi on 2021/06/02.
 */
@Service
public class QmsIpqcInspectionOrderServiceImpl extends BaseService<QmsIpqcInspectionOrder> implements QmsIpqcInspectionOrderService {

    @Resource
    private QmsIpqcInspectionOrderMapper qmsIpqcInspectionOrderMapper;
    @Resource
    private QmsIpqcInspectionOrderDetMapper qmsIpqcInspectionOrderDetMapper;
    @Resource
    private QmsIpqcInspectionOrderDetSampleMapper qmsIpqcInspectionOrderDetSampleMapper;
    @Resource
    private QmsHtIpqcInspectionOrderMapper qmsHtIpqcInspectionOrderMapper;
    @Resource
    private QmsIpqcInspectionOrderDetServiceImpl qmsIpqcInspectionOrderDetService;
    @Resource
    private FileFeignApi fileFeignApi;
    @Resource
    private PMFeignApi pmFeignApi;
    @Resource
    private BaseFeignApi baseFeignApi;

    @Override
    public List<QmsIpqcInspectionOrder> findList(Map<String, Object> map) {
        if(StringUtils.isEmpty(map.get("orgId"))) {
            SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
            map.put("orgId", user.getOrganizationId());
        }
        List<QmsIpqcInspectionOrder> qmsIpqcInspectionOrders = qmsIpqcInspectionOrderMapper.findList(map);

        return qmsIpqcInspectionOrders;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public QmsIpqcInspectionOrder createOrder( SearchQmsIpqcInspectionOrder searchQmsIpqcInspectionOrder) {
        SearchMesPmWorkOrder searchMesPmWorkOrder = new SearchMesPmWorkOrder();
        searchMesPmWorkOrder.setWorkOrderCode(searchQmsIpqcInspectionOrder.getWorkOrderCode());
        searchMesPmWorkOrder.setCodeQueryMark(1);
        List<MesPmWorkOrderDto> workOrderDtos = pmFeignApi.findWorkOrderList(searchMesPmWorkOrder).getData();
        if(StringUtils.isEmpty(workOrderDtos)){
            throw new BizErrorException("查无此工单");
        }

        SearchBaseInspectionWay searchBaseInspectionWay = new SearchBaseInspectionWay();
        searchBaseInspectionWay.setInspectionWayDesc(searchQmsIpqcInspectionOrder.getInspectionWayDesc());
        searchBaseInspectionWay.setInspectionType((byte)4);
        searchBaseInspectionWay.setQueryMark(1);
        List<BaseInspectionWay> baseInspectionWays = baseFeignApi.findList(searchBaseInspectionWay).getData();
        if(StringUtils.isEmpty(baseInspectionWays)){
            throw new BizErrorException("查无此检验方式");
        }

        //获取对应的检验标准
        List<BaseInspectionStandard> inspectionStandards = new ArrayList<>();
        SearchBaseInspectionStandard searchBaseInspectionStandard = new SearchBaseInspectionStandard();
        searchBaseInspectionStandard.setMaterialId(workOrderDtos.get(0).getMaterialId());
        searchBaseInspectionStandard.setInspectionWayId(baseInspectionWays.get(0).getInspectionWayId());
        inspectionStandards = baseFeignApi.findList(searchBaseInspectionStandard).getData();
        if(StringUtils.isEmpty(inspectionStandards)){
            searchBaseInspectionStandard.setMaterialId((long)0);
            inspectionStandards = baseFeignApi.findList(searchBaseInspectionStandard).getData();
            if(StringUtils.isEmpty(inspectionStandards)){
                throw new BizErrorException("未维护与该工单物料及该检验方式绑定的检验标准");
            }
        }

        //检验单信息
        QmsIpqcInspectionOrder qmsIpqcInspectionOrder = new QmsIpqcInspectionOrder();
        qmsIpqcInspectionOrder.setWorkOrderId(workOrderDtos.get(0).getWorkOrderId());
        qmsIpqcInspectionOrder.setMaterialId(workOrderDtos.get(0).getMaterialId());
        qmsIpqcInspectionOrder.setQty(workOrderDtos.get(0).getWorkOrderQty());
        qmsIpqcInspectionOrder.setInspectionWayId(baseInspectionWays.get(0).getInspectionWayId());
        qmsIpqcInspectionOrder.setInspectionStandardId(inspectionStandards.get(0).getInspectionStandardId());

        //获取明细信息
        List<QmsIpqcInspectionOrderDetDto> qmsIpqcInspectionOrderDetDtos = qmsIpqcInspectionOrderDetService.showOrderDet(inspectionStandards.get(0).getInspectionStandardId(), workOrderDtos.get(0).getWorkOrderQty());

        List<QmsIpqcInspectionOrderDet> qmsIpqcInspectionOrderDets = new ArrayList<>();
        for (QmsIpqcInspectionOrderDetDto detDto : qmsIpqcInspectionOrderDetDtos){
            QmsIpqcInspectionOrderDet qmsIpqcInspectionOrderDet = new QmsIpqcInspectionOrderDet();
            qmsIpqcInspectionOrderDet.setInspectionStandardDetId(detDto.getInspectionStandardDetId());
            qmsIpqcInspectionOrderDet.setIfMustInspection(detDto.getIfMustInspection());
            qmsIpqcInspectionOrderDet.setBigInspectionItemDesc(detDto.getInspectionItemDescBig());
            qmsIpqcInspectionOrderDet.setSmallInspectionItemDesc(detDto.getInspectionItemDescSmall());
            qmsIpqcInspectionOrderDet.setInspectionStandardName(detDto.getInspectionItemStandard());
            qmsIpqcInspectionOrderDet.setInspectionTag(detDto.getInspectionTag());
            qmsIpqcInspectionOrderDet.setSampleQty(detDto.getSampleQty());
            qmsIpqcInspectionOrderDet.setSpecificationUpperLimit(detDto.getSpecificationUpperLimit());
            qmsIpqcInspectionOrderDet.setSpecificationFloor(detDto.getSpecificationFloor());
            qmsIpqcInspectionOrderDet.setUnitName(detDto.getUnitName());
            qmsIpqcInspectionOrderDet.setAqlValue(detDto.getAqlValue());
            qmsIpqcInspectionOrderDet.setAcValue(detDto.getAcValue());
            qmsIpqcInspectionOrderDet.setReValue(detDto.getReValue());
            qmsIpqcInspectionOrderDets.add(qmsIpqcInspectionOrderDet);
        }
        qmsIpqcInspectionOrder.setQmsIpqcInspectionOrderDets(qmsIpqcInspectionOrderDets);
        this.save(qmsIpqcInspectionOrder);

        Map<String, Object> map = new HashMap<>();
        map.put("ipqcInspectionOrderId",qmsIpqcInspectionOrder.getIpqcInspectionOrderId());
        List<QmsIpqcInspectionOrder> list = this.findList(map);

        return list.get(0);
    }


    @Override
    public QmsIpqcInspectionOrder selectByKey(Long key) {
        Map<String,Object> map = new HashMap<>();
        map.put("ipqcInspectionOrderId",key);
        QmsIpqcInspectionOrder qmsIpqcInspectionOrder = qmsIpqcInspectionOrderMapper.findList(map).get(0);
        SearchQmsIpqcInspectionOrderDet searchQmsIpqcInspectionOrderDet = new SearchQmsIpqcInspectionOrderDet();
        searchQmsIpqcInspectionOrderDet.setIpqcInspectionOrderId(qmsIpqcInspectionOrder.getIpqcInspectionOrderId());
        List<QmsIpqcInspectionOrderDet> qmsIpqcInspectionOrderDetList = qmsIpqcInspectionOrderDetMapper.findDetList(ControllerUtil.dynamicConditionByEntity(searchQmsIpqcInspectionOrderDet));
        qmsIpqcInspectionOrder.setQmsIpqcInspectionOrderDets(qmsIpqcInspectionOrderDetList);

        return qmsIpqcInspectionOrder;
    }


    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(QmsIpqcInspectionOrder qmsIpqcInspectionOrder) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        //新增IPQC检验单
        qmsIpqcInspectionOrder.setIpqcInspectionOrderCode(CodeUtils.getId("IPQC-"));
        qmsIpqcInspectionOrder.setInspectionStatus((byte)1);
        qmsIpqcInspectionOrder.setAuditStatus((byte)1);
        qmsIpqcInspectionOrder.setCreateUserId(user.getUserId());
        qmsIpqcInspectionOrder.setCreateTime(new Date());
        qmsIpqcInspectionOrder.setModifiedUserId(user.getUserId());
        qmsIpqcInspectionOrder.setModifiedTime(new Date());
        qmsIpqcInspectionOrder.setStatus(StringUtils.isEmpty(qmsIpqcInspectionOrder.getStatus())?1:qmsIpqcInspectionOrder.getStatus());
        qmsIpqcInspectionOrder.setOrgId(user.getOrganizationId());
        int i = qmsIpqcInspectionOrderMapper.insertUseGeneratedKeys(qmsIpqcInspectionOrder);


        //新增IPQC检验单明细
        List<QmsIpqcInspectionOrderDet> qmsIpqcInspectionOrderDets = qmsIpqcInspectionOrder.getQmsIpqcInspectionOrderDets();
        if(StringUtils.isNotEmpty(qmsIpqcInspectionOrderDets)){
            for (QmsIpqcInspectionOrderDet qmsIpqcInspectionOrderDet:qmsIpqcInspectionOrderDets){
                qmsIpqcInspectionOrderDet.setIpqcInspectionOrderId(qmsIpqcInspectionOrder.getIpqcInspectionOrderId());
                qmsIpqcInspectionOrderDet.setCreateUserId(user.getUserId());
                qmsIpqcInspectionOrderDet.setCreateTime(new Date());
                qmsIpqcInspectionOrderDet.setModifiedUserId(user.getUserId());
                qmsIpqcInspectionOrderDet.setModifiedTime(new Date());
                qmsIpqcInspectionOrderDet.setStatus(StringUtils.isEmpty(qmsIpqcInspectionOrderDet.getStatus())?1:qmsIpqcInspectionOrderDet.getStatus());
                qmsIpqcInspectionOrderDet.setOrgId(user.getOrganizationId());
            }
            qmsIpqcInspectionOrderDetMapper.insertList(qmsIpqcInspectionOrderDets);
        }

        //履历
        QmsHtIpqcInspectionOrder qmsHtIpqcInspectionOrder = new QmsHtIpqcInspectionOrder();
        BeanUtils.copyProperties(qmsIpqcInspectionOrder, qmsHtIpqcInspectionOrder);
        qmsHtIpqcInspectionOrderMapper.insertSelective(qmsHtIpqcInspectionOrder);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(QmsIpqcInspectionOrder qmsIpqcInspectionOrder) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        //修改IPQC检验单
        qmsIpqcInspectionOrder.setModifiedUserId(user.getUserId());
        qmsIpqcInspectionOrder.setModifiedTime(new Date());
        qmsIpqcInspectionOrder.setOrgId(user.getOrganizationId());
        int i=qmsIpqcInspectionOrderMapper.updateByPrimaryKeySelective(qmsIpqcInspectionOrder);

        //履历
        QmsHtIpqcInspectionOrder qmsHtIpqcInspectionOrder = new QmsHtIpqcInspectionOrder();
        BeanUtils.copyProperties(qmsIpqcInspectionOrder, qmsHtIpqcInspectionOrder);
        qmsHtIpqcInspectionOrderMapper.insertSelective(qmsHtIpqcInspectionOrder);

        //原来有的明细只更新
        ArrayList<Long> idList = new ArrayList<>();
        List<QmsIpqcInspectionOrderDet> qmsIpqcInspectionOrderDets = qmsIpqcInspectionOrder.getQmsIpqcInspectionOrderDets();
        if(StringUtils.isNotEmpty(qmsIpqcInspectionOrderDets)) {
            for (QmsIpqcInspectionOrderDet qmsIpqcInspectionOrderDet : qmsIpqcInspectionOrderDets) {
                if (StringUtils.isNotEmpty(qmsIpqcInspectionOrderDet.getIpqcInspectionOrderDetId())) {
                    qmsIpqcInspectionOrderDetMapper.updateByPrimaryKeySelective(qmsIpqcInspectionOrderDet);
                    idList.add(qmsIpqcInspectionOrderDet.getIpqcInspectionOrderDetId());
                }
            }
        }

        //删除原有IPQC检验单明细
        Example example = new Example(QmsIpqcInspectionOrderDet.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("ipqcInspectionOrderId", qmsIpqcInspectionOrder.getIpqcInspectionOrderId());
        if (idList.size() > 0) {
            criteria.andNotIn("ipqcInspectionOrderDetId", idList);
        }
        List<QmsIpqcInspectionOrderDet> qmsIpqcInspectionOrderDets1 = qmsIpqcInspectionOrderDetMapper.selectByExample(example);
        if (StringUtils.isNotEmpty(qmsIpqcInspectionOrderDets1)) {
            for (QmsIpqcInspectionOrderDet qmsIpqcInspectionOrderDet : qmsIpqcInspectionOrderDets1) {
                //删除IPQC检验单明细样本
                Example example1 = new Example(QmsIpqcInspectionOrderDetSample.class);
                Example.Criteria criteria1 = example1.createCriteria();
                criteria1.andEqualTo("ipqcInspectionOrderDetId", qmsIpqcInspectionOrderDet.getIpqcInspectionOrderDetId());
                qmsIpqcInspectionOrderDetSampleMapper.deleteByExample(example1);
            }
        }
        //删除原IPQC检验单明细
        qmsIpqcInspectionOrderDetMapper.deleteByExample(example);

        //新增新的IPQC检验单明细
        if(StringUtils.isNotEmpty(qmsIpqcInspectionOrderDets)){
            for (QmsIpqcInspectionOrderDet qmsIpqcInspectionOrderDet:qmsIpqcInspectionOrderDets){
                if (idList.contains(qmsIpqcInspectionOrderDet.getIpqcInspectionOrderDetId())) {
                    continue;
                }
                qmsIpqcInspectionOrderDet.setIpqcInspectionOrderId(qmsIpqcInspectionOrder.getIpqcInspectionOrderId());
                qmsIpqcInspectionOrderDet.setCreateUserId(user.getUserId());
                qmsIpqcInspectionOrderDet.setCreateTime(new Date());
                qmsIpqcInspectionOrderDet.setModifiedUserId(user.getUserId());
                qmsIpqcInspectionOrderDet.setModifiedTime(new Date());
                qmsIpqcInspectionOrderDet.setStatus(StringUtils.isEmpty(qmsIpqcInspectionOrderDet.getStatus())?1:qmsIpqcInspectionOrderDet.getStatus());
                qmsIpqcInspectionOrderDet.setOrgId(user.getOrganizationId());
                qmsIpqcInspectionOrderDetMapper.insert(qmsIpqcInspectionOrderDet);
            }
        }

        //返写检验状态与检验结果
        this.writeBack(qmsIpqcInspectionOrder.getIpqcInspectionOrderId());

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int PDASubmit(QmsIpqcInspectionOrder qmsIpqcInspectionOrder){
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        //修改IPQC检验单
        qmsIpqcInspectionOrder.setModifiedUserId(user.getUserId());
        qmsIpqcInspectionOrder.setModifiedTime(new Date());
        int i=qmsIpqcInspectionOrderMapper.updateByPrimaryKeySelective(qmsIpqcInspectionOrder);

        //修改IPQC检验单明细
        List<QmsIpqcInspectionOrderDet> qmsIpqcInspectionOrderDets = qmsIpqcInspectionOrder.getQmsIpqcInspectionOrderDets();
        if(StringUtils.isNotEmpty(qmsIpqcInspectionOrderDets)){
            for (QmsIpqcInspectionOrderDet qmsIpqcInspectionOrderDet:qmsIpqcInspectionOrderDets){
                qmsIpqcInspectionOrderDet.setModifiedUserId(user.getUserId());
                qmsIpqcInspectionOrderDet.setModifiedTime(new Date());
                qmsIpqcInspectionOrderDetMapper.updateByPrimaryKeySelective(qmsIpqcInspectionOrderDet);
            }
        }

        //返写检验状态与检验结果
        this.writeBack(qmsIpqcInspectionOrder.getIpqcInspectionOrderId());

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int writeBack(Long ipqcInspectionOrderId){
        Example example = new Example(QmsIpqcInspectionOrderDet.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("ipqcInspectionOrderId",ipqcInspectionOrderId);
        List<QmsIpqcInspectionOrderDet> qmsIpqcInspectionOrderDets = qmsIpqcInspectionOrderDetMapper.selectByExample(example);

        if(StringUtils.isNotEmpty(qmsIpqcInspectionOrderDets)) {
            //计算明细项目合格数、不合格数、必检项目已检验数、必检项目数
            int qualifiedCount = 0;
            int unqualifiedCount = 0;
            int inspectionCount = 0;
            int mustInspectionCount = 0;
            for (QmsIpqcInspectionOrderDet qmsIpqcInspectionOrderDet : qmsIpqcInspectionOrderDets) {
                if (StringUtils.isNotEmpty(qmsIpqcInspectionOrderDet.getInspectionResult())) {
                    if (qmsIpqcInspectionOrderDet.getInspectionResult() == (byte) 0) {
                        unqualifiedCount++;
                    } else {
                        qualifiedCount++;
                    }
                }

                if (qmsIpqcInspectionOrderDet.getIfMustInspection() == (byte) 1) {
                    mustInspectionCount++;
                }

                if (StringUtils.isNotEmpty(qmsIpqcInspectionOrderDet.getInspectionResult())
                        && qmsIpqcInspectionOrderDet.getIfMustInspection() == (byte) 1) {
                    inspectionCount++;
                }
            }

            if (inspectionCount == mustInspectionCount) {
                QmsIpqcInspectionOrder qmsIpqcInspectionOrder = new QmsIpqcInspectionOrder();
                qmsIpqcInspectionOrder.setIpqcInspectionOrderId(ipqcInspectionOrderId);
                qmsIpqcInspectionOrder.setInspectionStatus((byte) 3);
                qmsIpqcInspectionOrder.setInspectionResult(unqualifiedCount == 0 ? (byte) 1 : (byte) 2);
                return qmsIpqcInspectionOrderMapper.updateByPrimaryKeySelective(qmsIpqcInspectionOrder);
            }
        }

        return 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String uploadFile(MultipartFile file){
        Map<String, Object> data = (Map<String, Object>)fileFeignApi.fileUpload(file).getData();
        String path = data.get("url").toString();
        return path;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        List<QmsHtIpqcInspectionOrder> list = new ArrayList<>();
        String[] idArry = ids.split(",");
        for (String id : idArry) {
            QmsIpqcInspectionOrder qmsIpqcInspectionOrder = qmsIpqcInspectionOrderMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(qmsIpqcInspectionOrder)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            QmsHtIpqcInspectionOrder qmsHtIpqcInspectionOrder = new QmsHtIpqcInspectionOrder();
            BeanUtils.copyProperties(qmsIpqcInspectionOrder, qmsHtIpqcInspectionOrder);
            list.add(qmsHtIpqcInspectionOrder);

            //删除IPQC检验单明细
            Example example = new Example(QmsIpqcInspectionOrderDet.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("ipqcInspectionOrderId", qmsIpqcInspectionOrder.getIpqcInspectionOrderId());
            List<QmsIpqcInspectionOrderDet> qmsIpqcInspectionOrderDets = qmsIpqcInspectionOrderDetMapper.selectByExample(example);
            for (QmsIpqcInspectionOrderDet qmsIpqcInspectionOrderDet : qmsIpqcInspectionOrderDets){
                //删除检验单明细样本
                Example example1 = new Example(QmsIpqcInspectionOrderDetSample.class);
                Example.Criteria criteria1 = example1.createCriteria();
                criteria1.andEqualTo("ipqcInspectionOrderDetId", qmsIpqcInspectionOrderDet.getIpqcInspectionOrderDetId());
                qmsIpqcInspectionOrderDetSampleMapper.deleteByExample(example1);
            }
            qmsIpqcInspectionOrderDetMapper.deleteByExample(example);
        }

        //履历
        qmsHtIpqcInspectionOrderMapper.insertList(list);

        return qmsIpqcInspectionOrderMapper.deleteByIds(ids);
    }

}
