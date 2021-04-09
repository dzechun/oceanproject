package com.fantechs.provider.bcm.service.impl;

import com.alibaba.fastjson.JSON;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.bcm.BcmLabelCategoryDto;
import com.fantechs.common.base.general.entity.bcm.BcmLabelCategory;
import com.fantechs.common.base.general.entity.bcm.history.BcmHtLabelCategory;
import com.fantechs.common.base.general.entity.bcm.search.SearchBcmLabelCategory;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.bcm.mapper.BcmHtLabelCategoryMapper;
import com.fantechs.provider.bcm.mapper.BcmLabelCategoryMapper;
import com.fantechs.provider.bcm.service.BcmLabelCategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* @author Mr.Lei
* @create 2020/12/17.
*/
@Service
public class BcmLabelCategoryServiceImpl  extends BaseService<BcmLabelCategory> implements BcmLabelCategoryService {

         @Resource
         private BcmLabelCategoryMapper bcmLabelCategoryMapper;
         @Resource
         private BcmHtLabelCategoryMapper bcmHtLabelCategoryMapper;
         @Resource
         private SecurityFeignApi securityFeignApi;

    @Override
    public List<BcmLabelCategoryDto> findList(SearchBcmLabelCategory searchBcmLabelCategory) {
        return bcmLabelCategoryMapper.findList(searchBcmLabelCategory);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(BcmLabelCategory record) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUserInfo)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        Example example = new Example(BcmLabelCategory.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("labelCategoryCode",record.getLabelCategoryCode());
//        example.or();
//        criteria.andEqualTo("labelCategoryName",record.getLabelCategoryName());
        BcmLabelCategory bcmLabelCategory = bcmLabelCategoryMapper.selectOneByExample(example);
        if(!StringUtils.isEmpty(bcmLabelCategory)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
        if(!this.MkdirDoc(null,record.getLabelCategoryName())){
            throw new BizErrorException("请检查文件路径");
        }
        record.setCreateTime(new Date());
        record.setCreateUserId(currentUserInfo.getUserId());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(currentUserInfo.getUserId());

        return bcmLabelCategoryMapper.insertUseGeneratedKeys(record);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(BcmLabelCategory entity) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUserInfo)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(BcmLabelCategory.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("labelCategoryCode",entity.getLabelCategoryCode());
        criteria.andNotEqualTo("labelCategoryId",entity.getLabelCategoryId());
        BcmLabelCategory bcmLabelCategory = bcmLabelCategoryMapper.selectOneByExample(example);

        entity.setModifiedUserId(currentUserInfo.getUserId());
        entity.setModifiedTime(new Date());
        int num = bcmLabelCategoryMapper.updateByPrimaryKeySelective(entity);

        BcmLabelCategory bcm = bcmLabelCategoryMapper.selectByPrimaryKey(entity.getLabelCategoryId());
        if(!bcm.getLabelCategoryName().equals(entity.getLabelCategoryName())){
            if(!this.MkdirDoc(bcm.getLabelCategoryName(),entity.getLabelCategoryName())){
                throw new BizErrorException("请检查文件路径");
            }
        }
        //新增历史记录
        BcmHtLabelCategory bcmHtLabelCategory = new BcmHtLabelCategory();
        BeanUtils.copyProperties(entity,bcmHtLabelCategory);
        bcmHtLabelCategoryMapper.updateByPrimaryKeySelective(bcmHtLabelCategory);

        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUserInfo)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        List<BcmHtLabelCategory> list = new ArrayList<>();

        String[] idArry = ids.split(",");
        for (String id : idArry) {
            BcmLabelCategory bcmLabelCategory = bcmLabelCategoryMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(bcmLabelCategory)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            BcmHtLabelCategory bcmHtLabelCategory = new BcmHtLabelCategory();
            BeanUtils.copyProperties(bcmLabelCategory,bcmHtLabelCategory);
            list.add(bcmHtLabelCategory);

            //删除文件
            if(!this.MkdirDoc(bcmLabelCategory.getLabelCategoryName(),null)){
                throw new BizErrorException("删除失败");
            }
        }
        bcmHtLabelCategoryMapper.insertList(list);

        return bcmLabelCategoryMapper.deleteByIds(ids);
    }

    /**
     * 按类别创建文件夹
     * @param oldDocName
     * @param docName
     * @return
     */
    private boolean MkdirDoc(String oldDocName,String docName){
        boolean isOk = false;
        SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
        searchSysSpecItem.setSpecCode("LabelFilePath");
        ResponseEntity<List<SysSpecItem>> itemList= securityFeignApi.findSpecItemList(searchSysSpecItem);
        List<SysSpecItem> sysSpecItemList = itemList.getData();
        Map map = (Map) JSON.parse(sysSpecItemList.get(0).getParaValue());
        if(!StringUtils.isEmpty(oldDocName)){
            String path = map.get("path").toString();
            File file = new File(path+oldDocName);
            if(file.exists()){
                isOk = file.delete();
            }else {
                isOk=true;
            }
        }
        if(!StringUtils.isEmpty(docName)){
            String path = map.get("path").toString();
            File file = new File(path+docName);
            if(!file.exists()){
                isOk = file.mkdirs();
            }
        }
        return isOk;
    }
}
