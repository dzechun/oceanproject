package com.fantechs.provider.qms.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.basic.BaseSampleProcess;
import com.fantechs.common.base.general.entity.qms.*;
import com.fantechs.common.base.general.entity.qms.history.QmsHtIpqcInspectionOrder;
import com.fantechs.common.base.general.entity.qms.search.SearchQmsInspectionOrderDet;
import com.fantechs.common.base.general.entity.qms.search.SearchQmsIpqcInspectionOrderDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.fileserver.service.FileFeignApi;
import com.fantechs.provider.qms.mapper.QmsHtIpqcInspectionOrderMapper;
import com.fantechs.provider.qms.mapper.QmsIpqcInspectionOrderDetMapper;
import com.fantechs.provider.qms.mapper.QmsIpqcInspectionOrderDetSampleMapper;
import com.fantechs.provider.qms.mapper.QmsIpqcInspectionOrderMapper;
import com.fantechs.provider.qms.service.QmsIpqcInspectionOrderService;
import feign.Response;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
    private BaseFeignApi baseFeignApi;
    @Resource
    private FileFeignApi fileFeignApi;

    @Override
    public List<QmsIpqcInspectionOrder> findList(Map<String, Object> map) {
        List<QmsIpqcInspectionOrder> qmsIpqcInspectionOrders = qmsIpqcInspectionOrderMapper.findList(map);
        SearchQmsIpqcInspectionOrderDet searchIpqcQmsInspectionOrderDet = new SearchQmsIpqcInspectionOrderDet();

        for (QmsIpqcInspectionOrder qmsIpqcInspectionOrder:qmsIpqcInspectionOrders){
            searchIpqcQmsInspectionOrderDet.setIpqcInspectionOrderId(qmsIpqcInspectionOrder.getIpqcInspectionOrderId());
            List<QmsIpqcInspectionOrderDet> qmsIpqcInspectionOrderDets = qmsIpqcInspectionOrderDetMapper.findList(ControllerUtil.dynamicConditionByEntity(searchIpqcQmsInspectionOrderDet));
            this.getAcReQty(qmsIpqcInspectionOrder,qmsIpqcInspectionOrderDets);
        }

        return qmsIpqcInspectionOrders;
    }

    @Override
    public QmsIpqcInspectionOrder selectByKey(Object key) {
        QmsIpqcInspectionOrder qmsIpqcInspectionOrder = qmsIpqcInspectionOrderMapper.selectByPrimaryKey(key);
        SearchQmsIpqcInspectionOrderDet searchQmsIpqcInspectionOrderDet = new SearchQmsIpqcInspectionOrderDet();
        searchQmsIpqcInspectionOrderDet.setIpqcInspectionOrderId(qmsIpqcInspectionOrder.getIpqcInspectionOrderId());
        List<QmsIpqcInspectionOrderDet> qmsIpqcInspectionOrderDetList = qmsIpqcInspectionOrderDetMapper.findList(ControllerUtil.dynamicConditionByEntity(searchQmsIpqcInspectionOrderDet));

        return this.getAcReQty(qmsIpqcInspectionOrder,qmsIpqcInspectionOrderDetList);
    }


    public QmsIpqcInspectionOrder getAcReQty(QmsIpqcInspectionOrder qmsIpqcInspectionOrder,List<QmsIpqcInspectionOrderDet> qmsIpqcInspectionOrderDets){
        if(StringUtils.isNotEmpty(qmsIpqcInspectionOrderDets)){
            for (QmsIpqcInspectionOrderDet qmsIpqcInspectionOrderDet : qmsIpqcInspectionOrderDets){
                //抽样类型为抽样方案时，去抽样方案取AC、RE、样本数
                if(qmsIpqcInspectionOrderDet.getSampleProcessType()!=null&&qmsIpqcInspectionOrderDet.getSampleProcessType()==(byte)4){
                    BaseSampleProcess baseSampleProcess = baseFeignApi.getAcReQty(qmsIpqcInspectionOrderDet.getSampleProcessId(), qmsIpqcInspectionOrder.getQty()).getData();
                    if(StringUtils.isNotEmpty(baseSampleProcess)) {
                        qmsIpqcInspectionOrderDet.setSampleQty(baseSampleProcess.getSampleQty());
                        qmsIpqcInspectionOrderDet.setAcValue(baseSampleProcess.getAcValue());
                        qmsIpqcInspectionOrderDet.setReValue(baseSampleProcess.getReValue());
                    }
                }
            }
            qmsIpqcInspectionOrder.setQmsIpqcInspectionOrderDets(qmsIpqcInspectionOrderDets);
        }
        return qmsIpqcInspectionOrder;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(QmsIpqcInspectionOrder qmsIpqcInspectionOrder) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        //新增IPQC检验单
        qmsIpqcInspectionOrder.setIpqcInspectionOrderCode(CodeUtils.getId("IPQC-"));
        qmsIpqcInspectionOrder.setInspectionStatus((byte)1);
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
        qmsHtIpqcInspectionOrderMapper.insert(qmsHtIpqcInspectionOrder);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(QmsIpqcInspectionOrder qmsIpqcInspectionOrder) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        //修改IPQC检验单
        qmsIpqcInspectionOrder.setModifiedUserId(user.getUserId());
        qmsIpqcInspectionOrder.setModifiedTime(new Date());
        qmsIpqcInspectionOrder.setOrgId(user.getOrganizationId());
        int i=qmsIpqcInspectionOrderMapper.updateByPrimaryKeySelective(qmsIpqcInspectionOrder);

        //履历
        QmsHtIpqcInspectionOrder qmsHtIpqcInspectionOrder = new QmsHtIpqcInspectionOrder();
        BeanUtils.copyProperties(qmsIpqcInspectionOrder, qmsHtIpqcInspectionOrder);
        qmsHtIpqcInspectionOrderMapper.insert(qmsHtIpqcInspectionOrder);

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
    public int writeBack(Long ipqcInspectionOrderId){
        Example example = new Example(QmsIpqcInspectionOrderDet.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("ipqcInspectionOrderId",ipqcInspectionOrderId);
        List<QmsIpqcInspectionOrderDet> qmsIpqcInspectionOrderDets = qmsIpqcInspectionOrderDetMapper.selectByExample(example);

        //计算明细项目合格数与不合格数
        int qualifiedCount = 0;
        int unqualifiedCount = 0;
        for (QmsIpqcInspectionOrderDet qmsIpqcInspectionOrderDet : qmsIpqcInspectionOrderDets){
            if(StringUtils.isNotEmpty(qmsIpqcInspectionOrderDet.getInspectionResult())){
                if(qmsIpqcInspectionOrderDet.getInspectionResult()==(byte)0){
                    unqualifiedCount++;
                }else {
                    qualifiedCount++;
                }
            }
        }

        if(qualifiedCount + unqualifiedCount == qmsIpqcInspectionOrderDets.size()){
            QmsIpqcInspectionOrder qmsIpqcInspectionOrder = new QmsIpqcInspectionOrder();
            qmsIpqcInspectionOrder.setIpqcInspectionOrderId(ipqcInspectionOrderId);
            qmsIpqcInspectionOrder.setInspectionStatus((byte) 3);
            qmsIpqcInspectionOrder.setInspectionResult(qualifiedCount==qmsIpqcInspectionOrderDets.size() ? (byte)1 : (byte)2);
            return qmsIpqcInspectionOrderMapper.updateByPrimaryKeySelective(qmsIpqcInspectionOrder);
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
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

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
