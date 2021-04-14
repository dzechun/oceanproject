package com.fantechs.provider.base.service.impl;

import com.alibaba.fastjson.JSON;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseLabelDto;
import com.fantechs.common.base.general.entity.basic.BaseLabel;
import com.fantechs.common.base.general.entity.basic.BaseLabelCategory;
import com.fantechs.common.base.general.entity.basic.history.BaseHtLabel;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseLabel;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.base.mapper.BaseHtLabelMapper;
import com.fantechs.provider.base.mapper.BaseLabelCategoryMapper;
import com.fantechs.provider.base.mapper.BaseLabelMapper;
import com.fantechs.provider.base.service.BaseLabelService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* @author Mr.Lei
* @create 2020/12/17.
*/
@Service
public class BaseLabelServiceImpl extends BaseService<BaseLabel> implements BaseLabelService {

    @Resource
    private BaseLabelMapper baseLabelMapper;
    @Resource
    private BaseHtLabelMapper baseHtLabelMapper;
    @Resource
    private SecurityFeignApi securityFeignApi;
    @Resource
    private BaseLabelCategoryMapper baseLabelCategoryMapper;

    @Override
    public List<BaseLabelDto> findList(SearchBaseLabel searchBaseLabel) {
        return baseLabelMapper.findList(searchBaseLabel);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int add(BaseLabel record, MultipartFile file) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUserInfo)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        if(StringUtils.isEmpty(file)){
            throw new BizErrorException("未检测到上传的标签文件");
        }
        if(StringUtils.isEmpty(file.getOriginalFilename()) || file.getOriginalFilename().equals("")){
            throw new BizErrorException("请规范标签文件名称");
        }

        //匹配文件后缀
        String reg = ".+(.btw|.BTW)$";
        Matcher matcher = Pattern.compile(reg).matcher(file.getOriginalFilename());
        if(!matcher.find()){
            throw new BizErrorException("标签模版文件格式不正确");
        }
        if(record.getIsDefaultLabel() != null && record.getIsDefaultLabel()==(byte)1){
            Example example = new Example(BaseLabel.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("labelCategoryId",record.getLabelCategoryId());
            List<BaseLabel> baseLabels = baseLabelMapper.selectByExample(example);
            if(baseLabels.size()>0){
                throw new BizErrorException("此类别已经有默认模版");
            }
        }
        Example example = new Example(BaseLabel.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("labelCode",record.getLabelCode());
        criteria.andNotEqualTo("labelId",record.getLabelId());
        BaseLabel baseLabel = baseLabelMapper.selectOneByExample(example);
        if(!StringUtils.isEmpty(baseLabel)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
        //名称+编码
        String fileName = file.getOriginalFilename().substring(0,file.getOriginalFilename().indexOf("."));
        record.setLabelName(fileName+record.getLabelCode());
        record.setLabelVersion("0.0.1");
        BaseLabelCategory baseLabelCategory = baseLabelCategoryMapper.selectByPrimaryKey(record.getLabelCategoryId());
        //文件上传
        String path = this.UploadFile(baseLabelCategory.getLabelCategoryName(),file,record.getLabelName());

        record.setSavePath(path);
        record.setCreateTime(new Date());
        record.setCreateUserId(currentUserInfo.getUserId());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(currentUserInfo.getUserId());
        int num = baseLabelMapper.insertUseGeneratedKeys(record);

        BaseHtLabel baseHtLabel = new BaseHtLabel();
        BeanUtils.copyProperties(record, baseHtLabel);
        baseHtLabelMapper.insertSelective(baseHtLabel);
        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(BaseLabel entity, MultipartFile file) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUserInfo)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        if(StringUtils.isEmpty(file) && StringUtils.isEmpty(file.getOriginalFilename()) || file.getOriginalFilename().equals("")){
            throw new BizErrorException("请规范标签文件名称");
        }else{
            entity.setLabelName(file.getOriginalFilename());
        }

        Example example = new Example(BaseLabel.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("labelCode",entity.getLabelCode());
        criteria.andNotEqualTo("labelId",entity.getLabelId());
        BaseLabel baseLabel = baseLabelMapper.selectOneByExample(example);
        if(!StringUtils.isEmpty(baseLabel)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        BaseLabelCategory baseLabelCategory = baseLabelCategoryMapper.selectByPrimaryKey(entity.getLabelCategoryId());
        if(file!=null){
            //文件上传
            String path = this.UploadFile(baseLabelCategory.getLabelCategoryName(),file,entity.getLabelName());

            entity.setSavePath(path);
        }

        //update version number
        entity.setLabelVersion(this.generationVersion(entity.getLabelVersion()));

        entity.setModifiedUserId(currentUserInfo.getUserId());
        entity.setModifiedTime(new Date());

        BaseHtLabel baseHtLabel = new BaseHtLabel();
        BeanUtils.copyProperties(entity, baseHtLabel);
        baseHtLabelMapper.insertSelective(baseHtLabel);

        return baseLabelMapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUserInfo)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        List<BaseHtLabel> list = new ArrayList<>();
        String[] idArry = ids.split(",");
        for (String id : idArry) {
            BaseLabel baseLabel = baseLabelMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(baseLabel)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            BaseHtLabel baseHtLabel = new BaseHtLabel();
            BeanUtils.copyProperties(baseLabel, baseHtLabel);
            list.add(baseHtLabel);
        }
        baseHtLabelMapper.insertList(list);

        return baseLabelMapper.deleteByIds(ids);
    }

    /**
     * Upload the tag template to the server
     * @param file
     */
    private String UploadFile(String labelCategoryName,MultipartFile file,String fileName){
        try {
            SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
            searchSysSpecItem.setSpecCode("LabelFilePath");
            ResponseEntity<List<SysSpecItem>> itemList= securityFeignApi.findSpecItemList(searchSysSpecItem);
            List<SysSpecItem> sysSpecItemList = itemList.getData();
            Map map = (Map) JSON.parse(sysSpecItemList.get(0).getParaValue());
            InputStream stream = file.getInputStream();
            String path = map.get("path") + labelCategoryName;
            FileOutputStream fs=new FileOutputStream(path+fileName+".btw");
            byte[] buffer =new byte[1024*1024];
            int bytesum = 0;
            int byteread = 0;
            while ((byteread=stream.read(buffer))!=-1)
            {
                bytesum+=byteread;
                fs.write(buffer,0,byteread);
                fs.flush();
            }
            fs.close();
            stream.close();
            return path;
        }catch (Exception e){
            throw new BizErrorException("标签模版上传失败");
        }
    }

    /**
     * Build version number
     * @param version
     * @return New version
     */
    private String generationVersion(String version){
        if(StringUtils.isEmpty(version)){
            throw new BizErrorException("版本号获取失败");
        }
        String[] vr = version.split("\\.");
        StringBuilder sb = new StringBuilder();
        for (String s : vr) {
            sb.append(s);
        }
        version = sb.toString();
        String ss = String.format("%03d", Integer.valueOf(version) + 1);
        StringBuilder sbs = new StringBuilder();
        for (int i = 0; i < ss.length(); i++) {
            if (i!=ss.length()-1){
                sbs.append(ss.substring(i,i+1)+".");
            }else{
                sbs.append(ss.substring(i,i+1));
            }
        }
        return sbs.toString();
    }
}
