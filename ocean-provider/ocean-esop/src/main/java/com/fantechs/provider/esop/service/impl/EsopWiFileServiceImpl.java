package com.fantechs.provider.esop.service.impl;

import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.esop.EsopWiFile;
import com.fantechs.common.base.general.entity.esop.history.EsopHtWiFile;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.fileserver.service.FileFeignApi;
import com.fantechs.provider.esop.mapper.EsopHtWiFileMapper;
import com.fantechs.provider.esop.mapper.EsopWiFileMapper;
import com.fantechs.provider.esop.service.EsopWiFileService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by leifengzhi on 2021/07/06.
 */
@Service
public class EsopWiFileServiceImpl extends BaseService<EsopWiFile> implements EsopWiFileService {

    @Resource
    private FileFeignApi fileFeignApi;
    @Resource
    private EsopWiFileMapper esopWiFileMapper;
    @Resource
    private EsopHtWiFileMapper esopHtWiFileMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public String uploadFile(MultipartFile file) {
        Map<String, Object> data = (Map<String, Object>) fileFeignApi.fileUpload(file).getData();
        String path = data.get("url").toString();
        return path;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchUploadFile(MultipartFile file) {
        Map<String, Object> data = (Map<String, Object>) fileFeignApi.fileUpload(file).getData();
        String path = data.get("url").toString();
        String fileName = file.getOriginalFilename();
        List<EsopWiFile> list = new ArrayList<>();
        EsopWiFile esopWiFile = new EsopWiFile();
        esopWiFile.setAccessUrl(path);
        esopWiFile.setWiFileName(fileName);
        list.add(esopWiFile);
        return this.batchAdd(list);
    }

    @Override
    public int batchAdd(List<EsopWiFile> esopWiFiles) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        List<EsopWiFile> list = new ArrayList<>();
        List<EsopHtWiFile> htList = new ArrayList<>();
        int i=0;
        for (EsopWiFile file : esopWiFiles) {
            if(StringUtils.isEmpty(file.getAccessUrl()))
                throw new BizErrorException("附件不能为空");

            file.setCreateUserId(user.getUserId());
            file.setCreateTime(new Date());
            file.setModifiedUserId(user.getUserId());
            file.setModifiedTime(new Date());
            file.setStatus(StringUtils.isEmpty(file.getStatus()) ? 1 : file.getStatus());
            file.setOrgId(user.getOrganizationId());
            list.add(file);
            EsopHtWiFile esopHtWiFile = new EsopHtWiFile();
            BeanUtils.copyProperties(file, esopHtWiFile);
            htList.add(esopHtWiFile);
        }
        if(StringUtils.isNotEmpty(list))
           i = esopWiFileMapper.insertList(list);
        if(StringUtils.isNotEmpty(htList))
            esopHtWiFileMapper.insertList(htList);
        return i;
    }

    @Override
    public int batchUpdate(List<EsopWiFile> esopWiFiles) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        List<EsopHtWiFile> htList = new ArrayList<>();
        int i=0;
        for (EsopWiFile file : esopWiFiles) {
            if(StringUtils.isEmpty(file.getAccessUrl(),file.getWiFileId()))
                throw new BizErrorException("附件不能为空");
            file.setModifiedUserId(user.getUserId());
            file.setModifiedTime(new Date());
//            i = esopWiFileMapper.updateByPrimaryKeySelective(file);
            //需要更新null，删除与wi的关联关系
            i =esopWiFileMapper.updateByPrimaryKey(file);
            EsopHtWiFile esopHtWiFile = new EsopHtWiFile();
            BeanUtils.copyProperties(file, esopHtWiFile);
            htList.add(esopHtWiFile);
        }
        return i;
    }

    @Override
    public List<EsopWiFile> findList(Map<String, Object> map) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",sysUser.getOrganizationId());
        return esopWiFileMapper.findList(map);
    }


}
