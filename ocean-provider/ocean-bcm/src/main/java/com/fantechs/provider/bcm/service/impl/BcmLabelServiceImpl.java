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
import com.fantechs.provider.bcm.util.RabbitProducer;
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
    @Autowired
    private RabbitProducer rabbitProducer;

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
        if(StringUtils.isEmpty(file)){
            throw new BizErrorException("未监测到上传的标签文件");
        }
        if(StringUtils.isEmpty(file.getOriginalFilename()) || file.getOriginalFilename().equals("")){
            throw new BizErrorException("请规范标签文件名称");
        }
        if(record.getIsDefaultLabel()==(byte)1){
            Example example = new Example(BcmLabel.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("labelCategoryId",record.getLabelCategoryId());
            List<BcmLabel> bcmLabels = bcmLabelMapper.selectByExample(example);
            if(bcmLabels.size()>0){
                throw new BizErrorException("此类别已经有默认模版");
            }
        }
        Example example = new Example(BcmLabel.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("labelCode",record.getLabelCode());
        criteria.andNotEqualTo("labelId",record.getLabelId());
        BcmLabel bcmLabel = bcmLabelMapper.selectOneByExample(example);
        if(!StringUtils.isEmpty(bcmLabel)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
        record.setLabelName(file.getOriginalFilename());
        record.setLabelVersion("V0.0.1");
        BcmLabelCategory bcmLabelCategory = bcmLabelCategoryMapper.selectByPrimaryKey(record.getLabelCategoryId());
        record.setSavePath("bartender服务器");
        //文件上传
        //rabbitProducer.sendFiles(record.getLabelVersion(),record.getLabelName(),file);


        record.setCreateTime(new Date());
        record.setCreateUserId(currentUserInfo.getUserId());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(currentUserInfo.getUserId());
        int num = bcmLabelMapper.insertUseGeneratedKeys(record);

        BcmHtLabel bcmHtLabel = new BcmHtLabel();
        BeanUtils.copyProperties(record,bcmHtLabel);
        bcmHtLabelMapper.insertSelective(bcmHtLabel);
        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(BcmLabel entity,MultipartFile file) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUserInfo)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        if(StringUtils.isEmpty(file) && StringUtils.isEmpty(file.getOriginalFilename()) || file.getOriginalFilename().equals("")){
            throw new BizErrorException("请规范标签文件名称");
        }else{
            entity.setLabelName(file.getOriginalFilename());
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
//        if(file!=null){
//            rabbitProducer.sendFiles(entity.getLabelVersion(),entity.getLabelName(),file);
//        }

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
}
