package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamNewsDto;
import com.fantechs.common.base.general.entity.eam.*;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquipment;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquipmentParam;
import com.fantechs.common.base.general.entity.eam.history.EamHtMaintainProject;
import com.fantechs.common.base.general.entity.eam.history.EamHtNews;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.mapper.EamEquipmentMapper;
import com.fantechs.provider.eam.mapper.EamHtNewsMapper;
import com.fantechs.provider.eam.mapper.EamNewsAttachmentMapper;
import com.fantechs.provider.eam.mapper.EamNewsMapper;
import com.fantechs.provider.eam.service.EamNewsService;
import com.fantechs.provider.eam.service.socket.impl.SocketServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/07/07.
 */
@Service
public class EamNewsServiceImpl extends BaseService<EamNews> implements EamNewsService {

    @Resource
    private EamNewsMapper eamNewsMapper;
    @Resource
    private EamHtNewsMapper eamHtNewsMapper;
    @Resource
    private EamNewsAttachmentMapper eamNewsAttachmentMapper;
    @Resource
    private EamEquipmentMapper eamEquipmentMapper;
    @Resource
    private SocketServiceImpl socketService;

    @Override
    public List<EamNewsDto> findList(Map<String, Object> map) {
        if(StringUtils.isEmpty(map.get("equipmentIp"))) {
            SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
            if (StringUtils.isEmpty(user)) {
                throw new BizErrorException(ErrorCodeEnum.UAC10011039);
            }
            map.put("orgId", user.getOrganizationId());
        }else {
            Example example = new Example(EamEquipment.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("equipmentIp",map.get("equipmentIp"));
            List<EamEquipment> eamEquipments = eamEquipmentMapper.selectByExample(example);
            if(StringUtils.isEmpty(eamEquipments)){
                throw new BizErrorException("查无绑定此IP的设备");
            }
            map.put("newStatus",3);
            map.put("orgId", eamEquipments.get(0).getOrgId());
        }

        return eamNewsMapper.findList(map);
    }

    @Override
    public EamNews selectByKey(Object key) {
        EamNews eamNews = eamNewsMapper.selectByPrimaryKey(key);

        Example example = new Example(EamNewsAttachment.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("newsId",eamNews.getNewsId());
        List<EamNewsAttachment> eamNewsAttachments = eamNewsAttachmentMapper.selectByExample(example);
        eamNews.setList(eamNewsAttachments);

        return eamNews;
    }

    /**
     * 审核并发布新闻
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int audit(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        int num = 0;
        String[] idArry = ids.split(",");
        for (String id : idArry) {
            EamNews eamNews = eamNewsMapper.selectByPrimaryKey(id);
            if(eamNews.getNewStatus()!=(byte)1){
                throw new BizErrorException("此新闻已审核发布");
            }

            eamNews.setNewStatus((byte)3);
            eamNews.setAuditDate(new Date());
            eamNews.setAuditUserId(user.getUserId());
            num += eamNewsMapper.updateByPrimaryKeySelective(eamNews);
        }

        //发送消息
        socketService.BatchInstructions(null,"1201","http://192.168.204.163/#/ESOPDataShow?ip=");

        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(EamNews record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        record.setNewsCode(CodeUtils.getId("XW-"));
        record.setCreateUserId(user.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setNewStatus((byte)1);
        record.setStatus(StringUtils.isEmpty(record.getStatus())?1: record.getStatus());
        record.setOrgId(user.getOrganizationId());
        eamNewsMapper.insertUseGeneratedKeys(record);

        List<EamNewsAttachment> eamNewsAttachments = record.getList();
        if(StringUtils.isNotEmpty(eamNewsAttachments)) {
            for (EamNewsAttachment eamNewsAttachment : eamNewsAttachments) {
                eamNewsAttachment.setNewsId(record.getNewsId());
                eamNewsAttachment.setCreateUserId(user.getUserId());
                eamNewsAttachment.setCreateTime(new Date());
                eamNewsAttachment.setModifiedUserId(user.getUserId());
                eamNewsAttachment.setModifiedTime(new Date());
                eamNewsAttachment.setStatus(StringUtils.isEmpty(eamNewsAttachment.getStatus()) ? 1 : eamNewsAttachment.getStatus());
                eamNewsAttachment.setOrgId(user.getOrganizationId());
            }
            eamNewsAttachmentMapper.insertList(eamNewsAttachments);
        }

        EamHtNews eamHtNews = new EamHtNews();
        BeanUtils.copyProperties(record, eamHtNews);
        int i = eamHtNewsMapper.insert(eamHtNews);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(EamNews entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        entity.setNewStatus((byte)1);
        entity.setModifiedTime(new Date());
        entity.setModifiedUserId(user.getUserId());
        int i = eamNewsMapper.updateByPrimaryKeySelective(entity);

        //删除原附件
        Example example1 = new Example(EamNewsAttachment.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("newsId", entity.getNewsId());
        eamNewsAttachmentMapper.deleteByExample(example1);

        List<EamNewsAttachment> eamNewsAttachments = entity.getList();
        if(StringUtils.isNotEmpty(eamNewsAttachments)) {
            for (EamNewsAttachment eamNewsAttachment : eamNewsAttachments) {
                eamNewsAttachment.setNewsId(entity.getNewsId());
                eamNewsAttachment.setCreateUserId(user.getUserId());
                eamNewsAttachment.setCreateTime(new Date());
                eamNewsAttachment.setModifiedUserId(user.getUserId());
                eamNewsAttachment.setModifiedTime(new Date());
                eamNewsAttachment.setStatus(StringUtils.isEmpty(eamNewsAttachment.getStatus()) ? 1 : eamNewsAttachment.getStatus());
                eamNewsAttachment.setOrgId(user.getOrganizationId());
            }
            eamNewsAttachmentMapper.insertList(eamNewsAttachments);
        }

        EamHtNews eamHtNews = new EamHtNews();
        BeanUtils.copyProperties(entity, eamHtNews);
        eamHtNewsMapper.insert(eamHtNews);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        List<EamHtNews> list = new ArrayList<>();
        String[] idArry = ids.split(",");
        for (String id : idArry) {
            EamNews eamNews = eamNewsMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(eamNews)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            EamHtNews eamHtNews = new EamHtNews();
            BeanUtils.copyProperties(eamNews, eamHtNews);
            list.add(eamHtNews);

            //删除附件
            Example example1 = new Example(EamNewsAttachment.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("newsId", eamNews.getNewsId());
            eamNewsAttachmentMapper.deleteByExample(example1);
        }

        eamHtNewsMapper.insertList(list);

        return eamNewsMapper.deleteByIds(ids);
    }

}
