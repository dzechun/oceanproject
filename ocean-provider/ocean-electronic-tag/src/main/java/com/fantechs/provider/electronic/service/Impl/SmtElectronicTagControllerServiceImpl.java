package com.fantechs.provider.electronic.service.Impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.electronic.dto.ImportSmtElectronicTagControllerDto;
import com.fantechs.common.base.electronic.dto.SmtElectronicTagControllerDto;
import com.fantechs.common.base.electronic.entity.SmtElectronicTagController;
import com.fantechs.common.base.electronic.entity.history.SmtHtElectronicTagController;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.common.base.utils.UUIDUtils;
import com.fantechs.provider.electronic.mapper.SmtElectronicTagControllerMapper;
import com.fantechs.provider.electronic.mapper.SmtHtElectronicTagControllerMapper;
import com.fantechs.provider.electronic.service.SmtElectronicTagControllerService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by leifengzhi on 2020/11/16.
 */
@Service
public class SmtElectronicTagControllerServiceImpl extends BaseService<SmtElectronicTagController> implements SmtElectronicTagControllerService {

    @Resource
    private SmtElectronicTagControllerMapper smtElectronicTagControllerMapper;
    @Resource
    private SmtHtElectronicTagControllerMapper smtHtElectronicTagControllerMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(SmtElectronicTagController smtElectronicTagController) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(SmtElectronicTagController.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("electronicTagControllerCode",smtElectronicTagController.getElectronicTagControllerCode());
        SmtElectronicTagController electronicTagController = smtElectronicTagControllerMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(electronicTagController)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
        smtElectronicTagController.setCreateUserId(user.getUserId());
        smtElectronicTagController.setCreateTime(new Date());
        smtElectronicTagController.setModifiedUserId(user.getUserId());
        smtElectronicTagController.setModifiedTime(new Date());
        smtElectronicTagController.setIdentity(UUIDUtils.getUUID());
        smtElectronicTagController.setStatus(StringUtils.isEmpty(smtElectronicTagController.getStatus())?1:smtElectronicTagController.getStatus());
        int i = smtElectronicTagControllerMapper.insertUseGeneratedKeys(smtElectronicTagController);

        //插入历史
        SmtHtElectronicTagController smtHtElectronicTagController = new SmtHtElectronicTagController();
        BeanUtils.copyProperties(smtElectronicTagController,smtHtElectronicTagController);
        smtHtElectronicTagControllerMapper.insert(smtHtElectronicTagController);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(SmtElectronicTagController smtElectronicTagController) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        Example example = new Example(SmtElectronicTagController.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("electronicTagControllerCode",smtElectronicTagController.getElectronicTagControllerCode())
                .andNotEqualTo("electronicTagControllerId",smtElectronicTagController.getElectronicTagControllerId());
        SmtElectronicTagController electronicTagController = smtElectronicTagControllerMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(electronicTagController)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        smtElectronicTagController.setModifiedTime(new Date());
        smtElectronicTagController.setModifiedUserId(user.getUserId());

        SmtHtElectronicTagController smtHtElectronicTagController = new SmtHtElectronicTagController();
        BeanUtils.copyProperties(smtElectronicTagController,smtHtElectronicTagController);
        smtHtElectronicTagControllerMapper.insert(smtHtElectronicTagController);

        return smtElectronicTagControllerMapper.updateByPrimaryKeySelective(smtElectronicTagController);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        LinkedList<SmtHtElectronicTagController> list = new LinkedList<>();
        String[] idsArr = ids.split(",");
        for (String id : idsArr) {
            SmtElectronicTagController electronicTagController = smtElectronicTagControllerMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(electronicTagController)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            SmtHtElectronicTagController smtHtElectronicTagController = new SmtHtElectronicTagController();
            BeanUtils.copyProperties(electronicTagController,smtHtElectronicTagController);
            smtHtElectronicTagController.setModifiedTime(new Date());
            smtHtElectronicTagController.setModifiedUserId(user.getUserId());
            list.add(smtHtElectronicTagController);
        }
        smtHtElectronicTagControllerMapper.insertList(list);
        return smtElectronicTagControllerMapper.deleteByIds(ids);
    }

    @Override
    public List<SmtElectronicTagControllerDto> findList(Map<String, Object> map) {
        return smtElectronicTagControllerMapper.findList(map);
    }

    @Override
    public Map<String, Object> importElectronicTagController(List<ImportSmtElectronicTagControllerDto> importSmtElectronicTagControllerDtos) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        Map<String, Object> resutlMap = new HashMap<>();  //返回操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败数
        LinkedList<SmtElectronicTagController> list = new LinkedList<>();
        LinkedList<SmtHtElectronicTagController> htList = new LinkedList<>();
        for (int i = 0; i < importSmtElectronicTagControllerDtos.size(); i++) {
            ImportSmtElectronicTagControllerDto importSmtElectronicTagControllerDto = importSmtElectronicTagControllerDtos.get(i);
            if (StringUtils.isEmpty(importSmtElectronicTagControllerDto.getElectronicTagControllerCode(),
                    importSmtElectronicTagControllerDto.getElectronicTagControllerIp(),
                    importSmtElectronicTagControllerDto.getElectronicTagControllerPort())){
                fail.add(i+3);
                continue;
            }
            Example example = new Example(SmtElectronicTagController.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("electronicTagControllerCode",importSmtElectronicTagControllerDto.getElectronicTagControllerCode());
            SmtElectronicTagController electronicTagController = smtElectronicTagControllerMapper.selectOneByExample(example);
            if (StringUtils.isNotEmpty(electronicTagController)){
                fail.add(i+3);
                continue;
            }
            SmtElectronicTagController smtElectronicTagController = new SmtElectronicTagController();
            BeanUtils.copyProperties(importSmtElectronicTagControllerDto,smtElectronicTagController);
            smtElectronicTagController.setCreateTime(new Date());
            smtElectronicTagController.setCreateUserId(currentUser.getUserId());
            smtElectronicTagController.setModifiedTime(new Date());
            smtElectronicTagController.setModifiedUserId(currentUser.getUserId());
            list.add(smtElectronicTagController);
        }

        if (StringUtils.isNotEmpty(list)){
             success = smtElectronicTagControllerMapper.insertList(list);
        }
        for (SmtElectronicTagController smtElectronicTagController : list) {
            SmtHtElectronicTagController smtHtElectronicTagController = new SmtHtElectronicTagController();
            BeanUtils.copyProperties(smtElectronicTagController,smtHtElectronicTagController);
            htList.add(smtHtElectronicTagController);
            smtHtElectronicTagControllerMapper.insertList(htList);
        }
        resutlMap.put("操作成功总数",success);
        resutlMap.put("操作失败行数",fail);
        return resutlMap;
    }
}
