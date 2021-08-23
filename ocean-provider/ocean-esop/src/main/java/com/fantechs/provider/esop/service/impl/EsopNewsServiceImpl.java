package com.fantechs.provider.esop.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.esop.EsopNewsDto;
import com.fantechs.common.base.general.entity.esop.EsopEquipment;
import com.fantechs.common.base.general.entity.esop.EsopNews;
import com.fantechs.common.base.general.entity.esop.EsopNewsAttachment;
import com.fantechs.common.base.general.entity.esop.history.EsopHtNews;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.esop.mapper.EsopEquipmentMapper;
import com.fantechs.provider.esop.mapper.EsopHtNewsMapper;
import com.fantechs.provider.esop.mapper.EsopNewsAttachmentMapper;
import com.fantechs.provider.esop.mapper.EsopNewsMapper;
import com.fantechs.provider.esop.service.EsopNewsService;
import com.fantechs.provider.esop.service.socket.impl.SocketServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/07/07.
 */
@Service
public class EsopNewsServiceImpl extends BaseService<EsopNews> implements EsopNewsService {

    @Resource
    private EsopNewsMapper esopNewsMapper;
    @Resource
    private EsopHtNewsMapper esopHtNewsMapper;
    @Resource
    private EsopNewsAttachmentMapper esopNewsAttachmentMapper;
    @Resource
    private EsopEquipmentMapper esopEquipmentMapper;
    @Resource
    private SocketServiceImpl socketService;

    @Override
    public List<EsopNewsDto> findList(Map<String, Object> map) {
        if(StringUtils.isEmpty(map.get("equipmentIp"))) {
            SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
            if (StringUtils.isEmpty(user)) {
                throw new BizErrorException(ErrorCodeEnum.UAC10011039);
            }
            map.put("orgId", user.getOrganizationId());
        }else {
            Example example = new Example(EsopEquipment.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("equipmentIp",map.get("equipmentIp"));
            List<EsopEquipment> EsopEquipments = esopEquipmentMapper.selectByExample(example);
            if(StringUtils.isEmpty(EsopEquipments)){
                throw new BizErrorException("查无绑定此IP的设备");
            }
            map.put("newStatus",3);
            map.put("orgId", EsopEquipments.get(0).getOrgId());
        }

        return esopNewsMapper.findList(map);
    }

    @Override
    public EsopNews selectByKey(Object key) {
        EsopNews EsopNews = esopNewsMapper.selectByPrimaryKey(key);

        Example example = new Example(EsopNewsAttachment.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("newsId",EsopNews.getNewsId());
        List<EsopNewsAttachment> EsopNewsAttachments = esopNewsAttachmentMapper.selectByExample(example);
        EsopNews.setList(EsopNewsAttachments);

        return EsopNews;
    }

    /**
     * 审核并发布新闻
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int audit(String ids) throws UnknownHostException {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        int num = 0;
        String[] idArry = ids.split(",");
        for (String id : idArry) {
            EsopNews EsopNews = esopNewsMapper.selectByPrimaryKey(id);
            if(EsopNews.getNewStatus()!=(byte)1){
                throw new BizErrorException("此新闻已审核发布");
            }

            EsopNews.setNewStatus((byte)3);
            EsopNews.setAuditDate(new Date());
            EsopNews.setAuditUserId(user.getUserId());
            num += esopNewsMapper.updateByPrimaryKeySelective(EsopNews);
        }
        //发送消息
        socketService.BatchInstructions(null,"1201","/#/ESOPDataShow?ip=");
   //   socketService.BatchInstructions(null,"1201","http://qmsapp.donlim.com/esop/#/ESOPDataShow?ip=");

        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(EsopNews record) {
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
        esopNewsMapper.insertUseGeneratedKeys(record);

        List<EsopNewsAttachment> EsopNewsAttachments = record.getList();
        if(StringUtils.isNotEmpty(EsopNewsAttachments)) {
            for (EsopNewsAttachment EsopNewsAttachment : EsopNewsAttachments) {
                EsopNewsAttachment.setNewsId(record.getNewsId());
                EsopNewsAttachment.setCreateUserId(user.getUserId());
                EsopNewsAttachment.setCreateTime(new Date());
                EsopNewsAttachment.setModifiedUserId(user.getUserId());
                EsopNewsAttachment.setModifiedTime(new Date());
                EsopNewsAttachment.setStatus(StringUtils.isEmpty(EsopNewsAttachment.getStatus()) ? 1 : EsopNewsAttachment.getStatus());
                EsopNewsAttachment.setOrgId(user.getOrganizationId());
            }
            esopNewsAttachmentMapper.insertList(EsopNewsAttachments);
        }

        EsopHtNews EsopHtNews = new EsopHtNews();
        BeanUtils.copyProperties(record, EsopHtNews);
        int i = esopHtNewsMapper.insert(EsopHtNews);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(EsopNews entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        entity.setNewStatus((byte)1);
        entity.setModifiedTime(new Date());
        entity.setModifiedUserId(user.getUserId());
        int i = esopNewsMapper.updateByPrimaryKeySelective(entity);

        //删除原附件
        Example example1 = new Example(EsopNewsAttachment.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("newsId", entity.getNewsId());
        esopNewsAttachmentMapper.deleteByExample(example1);

        List<EsopNewsAttachment> EsopNewsAttachments = entity.getList();
        if(StringUtils.isNotEmpty(EsopNewsAttachments)) {
            for (EsopNewsAttachment EsopNewsAttachment : EsopNewsAttachments) {
                EsopNewsAttachment.setNewsId(entity.getNewsId());
                EsopNewsAttachment.setCreateUserId(user.getUserId());
                EsopNewsAttachment.setCreateTime(new Date());
                EsopNewsAttachment.setModifiedUserId(user.getUserId());
                EsopNewsAttachment.setModifiedTime(new Date());
                EsopNewsAttachment.setStatus(StringUtils.isEmpty(EsopNewsAttachment.getStatus()) ? 1 : EsopNewsAttachment.getStatus());
                EsopNewsAttachment.setOrgId(user.getOrganizationId());
            }
            esopNewsAttachmentMapper.insertList(EsopNewsAttachments);
        }

        EsopHtNews EsopHtNews = new EsopHtNews();
        BeanUtils.copyProperties(entity, EsopHtNews);
        esopHtNewsMapper.insert(EsopHtNews);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        List<EsopHtNews> list = new ArrayList<>();
        String[] idArry = ids.split(",");
        for (String id : idArry) {
            EsopNews EsopNews = esopNewsMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(EsopNews)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            EsopHtNews EsopHtNews = new EsopHtNews();
            BeanUtils.copyProperties(EsopNews, EsopHtNews);
            list.add(EsopHtNews);

            //删除附件
            Example example1 = new Example(EsopNewsAttachment.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("newsId", EsopNews.getNewsId());
            esopNewsAttachmentMapper.deleteByExample(example1);
        }

        esopHtNewsMapper.insertList(list);

        return esopNewsMapper.deleteByIds(ids);
    }

}
