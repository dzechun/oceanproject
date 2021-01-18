package com.fantechs.provider.bcm.service.impl;

import com.alibaba.fastjson.JSON;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.bcm.BcmLabelDto;
import com.fantechs.common.base.general.entity.bcm.BcmLabel;
import com.fantechs.common.base.general.entity.bcm.BcmLabelCategory;
import com.fantechs.common.base.general.entity.bcm.history.BcmHtLabel;
import com.fantechs.common.base.general.entity.bcm.search.SearchBcmLabel;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.bcm.mapper.BcmHtLabelMapper;
import com.fantechs.provider.bcm.mapper.BcmLabelCategoryMapper;
import com.fantechs.provider.bcm.mapper.BcmLabelMapper;
import com.fantechs.provider.bcm.service.BcmLabelService;
import com.fantechs.provider.bcm.util.FTPUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* @author Mr.Lei
* @create 2020/12/17.
*/
@Service
public class BcmLabelServiceImpl  extends BaseService<BcmLabel> implements BcmLabelService {

    @Resource
    private BcmLabelMapper bcmLabelMapper;
    @Resource
    private BcmHtLabelMapper bcmHtLabelMapper;
    @Resource
    private SecurityFeignApi securityFeignApi;
    @Resource
    private BcmLabelCategoryMapper bcmLabelCategoryMapper;
    @Autowired
    private FTPUtil ftpUtil;

    @Override
    public List<BcmLabelDto> findList(SearchBcmLabel searchBcmLabel) {
        return bcmLabelMapper.findList(searchBcmLabel);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int add(BcmLabel record, MultipartFile file) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUserInfo)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(BcmLabel.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("labelCode",record.getLabelCode());
        BcmLabel bcmLabel = bcmLabelMapper.selectOneByExample(example);
        if(!StringUtils.isEmpty(bcmLabel)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        BcmLabelCategory bcmLabelCategory = bcmLabelCategoryMapper.selectByPrimaryKey(record.getLabelCategoryId());
//        //存放路径：标签类别+名称
        record.setSavePath(bcmLabelCategory.getLabelCategoryName()+"/"+file.getOriginalFilename());
        SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
        searchSysSpecItem.setSpecCode("FTP");
        ResponseEntity<List<SysSpecItem>> itemList= securityFeignApi.findSpecItemList(searchSysSpecItem);
        List<SysSpecItem> sysSpecItemList = itemList.getData();
        Map map = (Map) JSON.parse(sysSpecItemList.get(0).getParaValue());
        map.put("savePath","/"+bcmLabelCategory.getLabelCategoryName());
        boolean success = uploadFile(map,file);
//        if(!success){
//            throw new BizErrorException(ErrorCodeEnum.valueOf("上传FTP服务器失败"));
//        }

        record.setCreateTime(new Date());
        record.setCreateUserId(currentUserInfo.getUserId());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(currentUserInfo.getUserId());

        return bcmLabelMapper.insertUseGeneratedKeys(record);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(BcmLabel entity,MultipartFile file) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUserInfo)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(BcmLabel.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("labelCode",entity.getLabelCode());
        criteria.andNotEqualTo("labelId",entity.getLabelId());
        BcmLabel bcmLabel = bcmLabelMapper.selectOneByExample(example);
        if(!StringUtils.isEmpty(bcmLabel)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        BcmLabelCategory bcmLabelCategory = bcmLabelCategoryMapper.selectByPrimaryKey(entity.getLabelCategoryId());
        if(!file.isEmpty()){
            //存放路径：标签类别+名称
            entity.setSavePath(bcmLabelCategory.getLabelCategoryName()+"/"+file.getName());
            SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
            searchSysSpecItem.setSpecCode("FTP");
            ResponseEntity<List<SysSpecItem>> itemList= securityFeignApi.findSpecItemList(searchSysSpecItem);
            List<SysSpecItem> sysSpecItemList = itemList.getData();
            Map map = (Map) JSON.parse(sysSpecItemList.get(0).getParaValue());
            map.put("savePath",entity.getSavePath());
            boolean success = uploadFile(map,file);
//            if(!success){
//                throw new BizErrorException(ErrorCodeEnum.valueOf("上传FTP服务器失败"));
//            }
        }

        entity.setModifiedUserId(currentUserInfo.getUserId());
        entity.setModifiedTime(new Date());

        BcmHtLabel bcmHtLabel = new BcmHtLabel();
        BeanUtils.copyProperties(entity,bcmHtLabel);
        bcmHtLabelMapper.insertSelective(bcmHtLabel);

        return bcmLabelMapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUserInfo)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        List<BcmHtLabel> list = new ArrayList<>();
        String[] idArry = ids.split(",");
        for (String id : idArry) {
            BcmLabel bcmLabel = bcmLabelMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(bcmLabel)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            BcmHtLabel bcmHtLabel = new BcmHtLabel();
            BeanUtils.copyProperties(bcmLabel,bcmHtLabel);
            list.add(bcmHtLabel);
        }
        bcmHtLabelMapper.insertList(list);

        return bcmLabelMapper.deleteByIds(ids);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public boolean uploadFile(Map map, MultipartFile file){
        boolean isLogin = false;
        boolean success = false;

        //上传FTP服务器
        try {
            String ip = map.get("ip").toString();
            Integer port = Integer.parseInt(map.get("port").toString());
            String username = map.get("username").toString();
            String password = map.get("password").toString();
            isLogin = this.ftpUtil.connectFTP(ip,port,username,password);
            if(isLogin){
                File MFile = FTPUtil.multipartFileToFile(file);
                success = this.ftpUtil.uploadFile(MFile,map.get("savePath").toString());
                FTPUtil.deleteTempFile(MFile);
            }
        }catch (Exception e){
            throw new BizErrorException(ErrorCodeEnum.valueOf("上传失败"));
        }finally {
            this.ftpUtil.loginOut();
        }
        return success;
    }
}
