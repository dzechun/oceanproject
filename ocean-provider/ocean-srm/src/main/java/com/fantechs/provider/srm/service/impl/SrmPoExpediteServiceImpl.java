package com.fantechs.provider.srm.service.impl;

import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.general.dto.srm.SrmPoExpediteDto;
import com.fantechs.common.base.general.dto.srm.SrmPoExpediteRecordDto;
import com.fantechs.common.base.general.entity.basic.BaseFile;
import com.fantechs.common.base.general.entity.srm.SrmPoExpedite;
import com.fantechs.common.base.general.entity.srm.SrmPoExpediteRecord;
import com.fantechs.common.base.general.entity.srm.history.SrmHtPoExpedite;
import com.fantechs.common.base.general.entity.srm.history.SrmHtPoExpediteRecord;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.srm.mapper.SrmHtPoExpediteMapper;
import com.fantechs.provider.srm.mapper.SrmHtPoExpediteRecordMapper;
import com.fantechs.provider.srm.mapper.SrmPoExpediteMapper;
import com.fantechs.provider.srm.mapper.SrmPoExpediteRecordMapper;
import com.fantechs.provider.srm.service.SrmPoExpediteService;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/11/18.
 */
@Service
public class SrmPoExpediteServiceImpl extends BaseService<SrmPoExpedite> implements SrmPoExpediteService {

    @Resource
    private SrmPoExpediteMapper srmPoExpediteMapper;
    @Resource
    private SrmPoExpediteRecordMapper srmPoExpediteRecordMapper;

    @Resource
    private SrmHtPoExpediteMapper srmHtPoExpediteMapper;
    @Resource
    private SrmHtPoExpediteRecordMapper srmHtPoExpediteRecordMapper;

    @Resource
    private BaseFeignApi baseFeignApi;


    @Override
    public List<SrmPoExpediteDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(map.get("orgId"))) {
            map.put("orgId",user.getOrganizationId());
        }


        if (StringUtils.isNotEmpty(user.getSupplierId())) {
            map.put("supplierId", user.getSupplierId());
        }

        return srmPoExpediteMapper.findList(map);
    }


    @Override
    public int save(SrmPoExpedite record) {

        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        record.setCreateUserId(user.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setStatus(StringUtils.isEmpty(record.getStatus())?1: record.getStatus());
        record.setOrgId(user.getOrganizationId());
        int i = srmPoExpediteMapper.insertUseGeneratedKeys(record);

        //保存明细跟图片
        saveImage(record,user);

        SrmHtPoExpedite srmHtPoExpedite = new SrmHtPoExpedite();
        BeanUtils.copyProperties(record, srmHtPoExpedite);
        srmHtPoExpediteMapper.insertUseGeneratedKeys(srmHtPoExpedite);

        ht(record,srmHtPoExpedite);

        return i;
    }

    @Override
    public int update(SrmPoExpedite entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        entity.setModifiedUserId(user.getUserId());
        entity.setModifiedTime(new Date());

        int i = srmPoExpediteMapper.updateByPrimaryKeySelective(entity);

        //保存明细跟图片
        saveImage(entity,user);

        SrmHtPoExpedite srmHtPoExpedite = new SrmHtPoExpedite();
        BeanUtils.copyProperties(entity, srmHtPoExpedite);
        srmHtPoExpediteMapper.insertUseGeneratedKeys(srmHtPoExpedite);

        ht(entity,srmHtPoExpedite);

        return i;
    }

    private void saveImage(SrmPoExpedite record,SysUser user){
        if (StringUtils.isNotEmpty(record.getList())) {
            List<BaseFile> fileList = new ArrayList<>();
            for (SrmPoExpediteRecordDto srmPoExpediteRecordDto : record.getList()) {
                if (StringUtils.isNotEmpty(srmPoExpediteRecordDto.getPoExpediteRecordId())) {
                    continue;
                }
                srmPoExpediteRecordDto.setCreateUserId(user.getUserId());
                srmPoExpediteRecordDto.setCreateTime(new Date());
                srmPoExpediteRecordDto.setModifiedTime(new Date());
                srmPoExpediteRecordDto.setModifiedUserId(user.getUserId());
                srmPoExpediteRecordDto.setPoExpediteId(record.getPoExpediteId());
                srmPoExpediteRecordMapper.insertUseGeneratedKeys(srmPoExpediteRecordDto);

                if (StringUtils.isNotEmpty(srmPoExpediteRecordDto.getFileList())) {
                    for (SrmPoExpediteRecordDto poExpediteRecordDto : srmPoExpediteRecordDto.getFileList()) {
                        BaseFile baseFile = new BaseFile();
                        baseFile.setAccessUrl(poExpediteRecordDto.getAccessUrl());
                        baseFile.setRelevanceId(srmPoExpediteRecordDto.getPoExpediteRecordId());
                        baseFile.setRelevanceTableName("srm_po_expedite_record");
                        fileList.add(baseFile);
                    }
                }


            }
            if (StringUtils.isNotEmpty(fileList)) {
                baseFeignApi.batchAddFile(fileList);
            }

        }
    }

    @Override
    public int batchDelete(String ids) {
        String[] idArry = ids.split(",");

        Example detExample = new Example(SrmPoExpediteRecord.class);

        for (String id : idArry) {
            SrmPoExpedite srmPoExpedite = srmPoExpediteMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(srmPoExpedite)){
                continue;
            }

            SrmHtPoExpedite srmHtPoExpedite = new SrmHtPoExpedite();
            BeanUtils.copyProperties(srmPoExpedite, srmHtPoExpedite);
            srmHtPoExpediteMapper.insertUseGeneratedKeys(srmHtPoExpedite);

            detExample.createCriteria().andEqualTo("poExpediteId",srmPoExpedite.getPoExpediteId());
            List<SrmPoExpediteRecord> srmPoExpediteRecordList = srmPoExpediteRecordMapper.selectByExample(detExample);
            List<SrmPoExpediteRecordDto> list = new ArrayList<>();
            BeanUtils.copyProperties(srmPoExpediteRecordList,list);
            srmPoExpedite.setList(list);
            srmPoExpediteRecordMapper.deleteByExample(detExample);
            detExample.clear();

            ht(srmPoExpedite,srmHtPoExpedite);
        }

        return srmPoExpediteMapper.deleteByIds(ids);
    }


    private void ht(SrmPoExpedite record,SrmHtPoExpedite srmHtPoExpedite){
        if (StringUtils.isNotEmpty(record.getList())) {
            List<SrmHtPoExpediteRecord> detist = new ArrayList<>();
            for (SrmPoExpediteRecord srmPoExpediteRecord : record.getList()) {

                SrmHtPoExpediteRecord srmHtPoExpediteRecord = new SrmHtPoExpediteRecord();
                BeanUtils.copyProperties(srmPoExpediteRecord, srmHtPoExpediteRecord);
                srmHtPoExpediteRecord.setPoExpediteId(srmHtPoExpedite.getHtPoExpediteId());

                srmPoExpediteRecord.setPoExpediteId(record.getPoExpediteId());

                detist.add(srmHtPoExpediteRecord);
            }
            srmHtPoExpediteRecordMapper.insertList(detist);
        }
    }
}
