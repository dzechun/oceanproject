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
import java.io.File;
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
    public List<BaseLabelDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return baseLabelMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int add(BaseLabel baseLabel, MultipartFile file) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();

        if(StringUtils.isEmpty(file)){
            throw new BizErrorException("?????????????????????????????????");
        }
        if(StringUtils.isEmpty(file.getOriginalFilename()) || file.getOriginalFilename().equals("")){
            throw new BizErrorException("???????????????????????????");
        }
        Matcher matcher = Pattern.compile("[a-zA-Z]+").matcher(baseLabel.getLabelCode());
        if(!matcher.find()){
            throw new BizErrorException("???????????????????????????????????????");
        }
//        String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~???@#???%??????&*????????????+|{}????????????????????????????????????]";
//        Matcher matcher = Pattern.compile(regEx).matcher(baseLabel.getLabelCode());
//        if(matcher.find()){
//            throw new BizErrorException("???????????????????????????????????????");
//        }
//        matcher = Pattern.compile("[\\u4e00-\\u9fa5]").matcher(baseLabel.getLabelCode());
//        if(matcher.find()){
//            throw new BizErrorException("????????????????????????");
//        }
        //??????????????????
        String reg = ".+(.btw|.BTW)$";
        matcher = Pattern.compile(reg).matcher(file.getOriginalFilename());
        if(!matcher.find()){
            throw new BizErrorException("?????????????????????????????????");
        }
        if(baseLabel.getIsDefaultLabel() != null && baseLabel.getIsDefaultLabel()==(byte)1){
            Example example = new Example(BaseLabel.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("orgId", currentUserInfo.getOrganizationId());
            criteria.andEqualTo("labelCategoryId",baseLabel.getLabelCategoryId());
            List<BaseLabel> baseLabels = baseLabelMapper.selectByExample(example);
            if(baseLabels.size()>0){
                throw new BizErrorException("??????????????????????????????");
            }
        }
        Example example = new Example(BaseLabel.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orgId", currentUserInfo.getOrganizationId());
        criteria.andEqualTo("labelCode",baseLabel.getLabelCode());
        criteria.andNotEqualTo("labelId",baseLabel.getLabelId());
        BaseLabel baseLabel1 = baseLabelMapper.selectOneByExample(example);
        if(!StringUtils.isEmpty(baseLabel1)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
        BaseLabelCategory baseLabelCategory = baseLabelCategoryMapper.selectByPrimaryKey(baseLabel.getLabelCategoryId());
        //?????????=????????????+????????????+?????????
        baseLabel.setLabelVersion("0.0.1");
        baseLabel.setLabelName(baseLabelCategory.getLabelCategoryName()+"|"+baseLabel.getLabelCode()+"|"+baseLabel.getLabelVersion());

        //????????????????????????????????????
        this.MkdirDocByVersion(baseLabel.getLabelVersion(),baseLabelCategory.getLabelCategoryName());
        //????????????
        String path = this.UploadFile(baseLabelCategory.getLabelCategoryName(),file,baseLabel.getLabelName(),baseLabel.getLabelVersion());

        baseLabel.setSavePath(path);
        baseLabel.setCreateTime(new Date());
        baseLabel.setCreateUserId(currentUserInfo.getUserId());
        baseLabel.setModifiedTime(new Date());
        baseLabel.setModifiedUserId(currentUserInfo.getUserId());
        baseLabel.setIsDelete((byte) 0);
        baseLabel.setOrgId(currentUserInfo.getOrganizationId());
        int num = baseLabelMapper.insertUseGeneratedKeys(baseLabel);

        BaseHtLabel baseHtLabel = new BaseHtLabel();
        BeanUtils.copyProperties(baseLabel, baseHtLabel);
        baseHtLabelMapper.insertSelective(baseHtLabel);
        return num;
    }

    /**
     * ??????????????????????????????
     *
     * @param c
     * ??????????????????
     * @return true???????????????
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
            || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
            || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
            || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
            || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
            || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(BaseLabel baseLabel, MultipartFile file) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();

        if(StringUtils.isNotEmpty(file) && (StringUtils.isEmpty(file.getOriginalFilename()) || file.getOriginalFilename().equals(""))){
            throw new BizErrorException("???????????????????????????");
        }
        Example example = new Example(BaseLabel.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orgId", currentUserInfo.getOrganizationId());
        criteria.andEqualTo("labelCode",baseLabel.getLabelCode());
        criteria.andNotEqualTo("labelId",baseLabel.getLabelId());
        BaseLabel baseLabel1 = baseLabelMapper.selectOneByExample(example);
        if(!StringUtils.isEmpty(baseLabel1)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        BaseLabelCategory baseLabelCategory = baseLabelCategoryMapper.selectByPrimaryKey(baseLabel.getLabelCategoryId());

        if(file!=null){
            //????????????????????????????????????????????????????????????????????????
            baseLabel.setLabelVersion(this.generationVersion(baseLabel.getLabelVersion()));
            this.MkdirDocByVersion(baseLabel.getLabelVersion(),baseLabelCategory.getLabelCategoryName());
            //????????????
            baseLabel.setLabelName(baseLabelCategory.getLabelCategoryName()+"|"+baseLabel.getLabelCode()+"|"+baseLabel.getLabelVersion());
            String path = this.UploadFile(baseLabelCategory.getLabelCategoryName(),file,baseLabel.getLabelName(),baseLabel.getLabelVersion());
            baseLabel.setSavePath(path);
        }

        baseLabel.setModifiedUserId(currentUserInfo.getUserId());
        baseLabel.setModifiedTime(new Date());
        baseLabel.setOrgId(currentUserInfo.getOrganizationId());

        BaseHtLabel baseHtLabel = new BaseHtLabel();
        BeanUtils.copyProperties(baseLabel, baseHtLabel);
        baseHtLabelMapper.insertSelective(baseHtLabel);

        return baseLabelMapper.updateByPrimaryKeySelective(baseLabel);
    }

    @Override
    public List<BaseLabel> findListByIDs(List<Long> ids) {
        Example example = new Example(BaseLabel.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("labelId", ids);
        List<BaseLabel> labelList = baseLabelMapper.selectByExample(example);
        return labelList;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();

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
    private String UploadFile(String labelCategoryName,MultipartFile file,String fileName,String labelVersion){
        try {
            //?????????????????????????????????
            SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
            searchSysSpecItem.setSpecCode("LabelFilePath");
            ResponseEntity<List<SysSpecItem>> itemList= securityFeignApi.findSpecItemList(searchSysSpecItem);
            List<SysSpecItem> sysSpecItemList = itemList.getData();
            Map map = (Map) JSON.parse(sysSpecItemList.get(0).getParaValue());
            InputStream stream = file.getInputStream();
            String path = map.get("path") +labelCategoryName+"/"+labelVersion+"/";
            FileOutputStream fs = new FileOutputStream(path+fileName+".btw");
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
            throw new BizErrorException("????????????????????????");
        }
    }

    /**
     * Build version number
     * @param version
     * @return New version
     */
    private String generationVersion(String version){
        if(StringUtils.isEmpty(version)){
            throw new BizErrorException("?????????????????????");
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

    /**
     * ????????????????????????
     * @param labelVersion
     * @param categoryName
     * @return
     */
    private void MkdirDocByVersion(String labelVersion,String categoryName){
        SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
        searchSysSpecItem.setSpecCode("LabelFilePath");
        ResponseEntity<List<SysSpecItem>> itemList = securityFeignApi.findSpecItemList(searchSysSpecItem);
        List<SysSpecItem> sysSpecItemList = itemList.getData();
        Map map = (Map) JSON.parse(sysSpecItemList.get(0).getParaValue());
        String path = map.get("path").toString();
        File file = new File(path+categoryName+"/"+labelVersion);
        //label??????????????????????????????????????????
        file.mkdirs();
    }
}
