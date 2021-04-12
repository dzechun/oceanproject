package com.fantechs.provider.base.service.impl;

import com.alibaba.fastjson.JSON;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseLabelCategoryDto;
import com.fantechs.common.base.general.entity.basic.BaseLabelCategory;
import com.fantechs.common.base.general.entity.basic.history.BaseHtLabelCategory;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseLabelCategory;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.base.mapper.BaseHtLabelCategoryMapper;
import com.fantechs.provider.base.mapper.BaseLabelCategoryMapper;
import com.fantechs.provider.base.service.BaseLabelCategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
public class BaseLabelCategoryServiceImpl extends BaseService<BaseLabelCategory> implements BaseLabelCategoryService {

         @Resource
         private BaseLabelCategoryMapper baseLabelCategoryMapper;
         @Resource
         private BaseHtLabelCategoryMapper baseHtLabelCategoryMapper;
         @Resource
         private SecurityFeignApi securityFeignApi;

    @Override
    public List<BaseLabelCategoryDto> findList(SearchBaseLabelCategory searchBaseLabelCategory) {
        return baseLabelCategoryMapper.findList(searchBaseLabelCategory);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(BaseLabelCategory record) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUserInfo)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        Example example = new Example(BaseLabelCategory.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("labelCategoryCode",record.getLabelCategoryCode());
//        example.or();
//        criteria.andEqualTo("labelCategoryName",record.getLabelCategoryName());
        BaseLabelCategory baseLabelCategory = baseLabelCategoryMapper.selectOneByExample(example);
        if(!StringUtils.isEmpty(baseLabelCategory)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
        if(!this.MkdirDoc(null,record.getLabelCategoryName())){
            throw new BizErrorException("请检查文件路径");
        }
        record.setCreateTime(new Date());
        record.setCreateUserId(currentUserInfo.getUserId());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(currentUserInfo.getUserId());

        return baseLabelCategoryMapper.insertUseGeneratedKeys(record);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(BaseLabelCategory entity) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUserInfo)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(BaseLabelCategory.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("labelCategoryCode",entity.getLabelCategoryCode());
        criteria.andNotEqualTo("labelCategoryId",entity.getLabelCategoryId());
        BaseLabelCategory baseLabelCategory = baseLabelCategoryMapper.selectOneByExample(example);

        entity.setModifiedUserId(currentUserInfo.getUserId());
        entity.setModifiedTime(new Date());
        int num = baseLabelCategoryMapper.updateByPrimaryKeySelective(entity);

        BaseLabelCategory bcm = baseLabelCategoryMapper.selectByPrimaryKey(entity.getLabelCategoryId());
        if(!bcm.getLabelCategoryName().equals(entity.getLabelCategoryName())){
            if(!this.MkdirDoc(bcm.getLabelCategoryName(),entity.getLabelCategoryName())){
                throw new BizErrorException("请检查文件路径");
            }
        }
        //新增历史记录
        BaseHtLabelCategory baseHtLabelCategory = new BaseHtLabelCategory();
        BeanUtils.copyProperties(entity, baseHtLabelCategory);
        baseHtLabelCategoryMapper.updateByPrimaryKeySelective(baseHtLabelCategory);

        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUserInfo)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        List<BaseHtLabelCategory> list = new ArrayList<>();

        String[] idArry = ids.split(",");
        for (String id : idArry) {
            BaseLabelCategory baseLabelCategory = baseLabelCategoryMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(baseLabelCategory)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            BaseHtLabelCategory baseHtLabelCategory = new BaseHtLabelCategory();
            BeanUtils.copyProperties(baseLabelCategory, baseHtLabelCategory);
            list.add(baseHtLabelCategory);

            //删除文件
            if(!this.MkdirDoc(baseLabelCategory.getLabelCategoryName(),null)){
                throw new BizErrorException("删除失败");
            }
        }
        baseHtLabelCategoryMapper.insertList(list);

        return baseLabelCategoryMapper.deleteByIds(ids);
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