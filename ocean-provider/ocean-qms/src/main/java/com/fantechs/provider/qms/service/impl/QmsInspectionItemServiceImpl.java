package com.fantechs.provider.qms.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.qms.QmsInspectionItemDto;
import com.fantechs.common.base.general.dto.qms.QmsInspectionTypeDto;
import com.fantechs.common.base.general.entity.qms.QmsInspectionItem;
import com.fantechs.common.base.general.entity.qms.QmsInspectionItemDet;
import com.fantechs.common.base.general.entity.qms.history.QmsHtInspectionItem;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.qms.mapper.QmsHtInspectionItemMapper;
import com.fantechs.provider.qms.mapper.QmsInspectionItemDetMapper;
import com.fantechs.provider.qms.mapper.QmsInspectionItemMapper;
import com.fantechs.provider.qms.service.QmsInspectionItemService;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 *
 * Created by leifengzhi on 2020/12/25.
 */
@Service
public class QmsInspectionItemServiceImpl extends BaseService<QmsInspectionItem> implements QmsInspectionItemService {

    @Resource
    private QmsInspectionItemMapper qmsInspectionItemMapper;
    @Resource
    private QmsHtInspectionItemMapper qmsHtInspectionItemMapper;
    @Resource
    private QmsInspectionItemDetMapper qmsInspectionItemDetMapper;
    @Resource
    private SecurityFeignApi securityFeignApi;

    @Override
    public List<QmsInspectionItemDto> findList(Map<String, Object> map) {
        return qmsInspectionItemMapper.findList(map);
    }

    private static final Map<String,List<SysSpecItem>> hashMap = new Hashtable<>();

    @SneakyThrows
    @Override
    public List<QmsInspectionItemDto> exportExcel(Map<String, Object> map) {
        List<QmsInspectionItemDto> list = this.findList(map);

        List<SysSpecItem> inspectionItems = null;
        List<SysSpecItem> inspectionLevels = null;
        List<SysSpecItem> inspectionTools = null;

        new Thread(new SecurityRun("inspectionItem")).start();
        new Thread(new SecurityRun("inspectionLevel")).start();
        new Thread(new SecurityRun("inspectionTool")).start();

        while (true){
            Thread.sleep(1000);
            if (hashMap != null && hashMap.size() == 3){
                inspectionItems = hashMap.get("inspectionItem");
                inspectionLevels = hashMap.get("inspectionLevel");
                inspectionTools = hashMap.get("inspectionTool");
                break;
            }

        }
//        SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
//        searchSysSpecItem.setSpecCode("inspectionItem");
//        List<SysSpecItem> inspectionItems = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();
//        searchSysSpecItem.setSpecCode("inspectionLevel");
//        List<SysSpecItem> inspectionLevels = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();
//        searchSysSpecItem.setSpecCode("inspectionTool");
//        List<SysSpecItem> inspectionTools = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();

        for (QmsInspectionItemDto qmsInspectionItemDto : list) {
            qmsInspectionItemDto.setInspectionNapeName(JSONObject.parseObject(String.valueOf(JSONObject.parseArray(inspectionItems.get(0).getParaValue()).get(Integer.parseInt(qmsInspectionItemDto.getInspectionNape()+"")))).get("name")+"");
            qmsInspectionItemDto.setInspectionItemLevelName(JSONObject.parseObject(String.valueOf(JSONObject.parseArray(inspectionLevels.get(0).getParaValue()).get(Integer.parseInt(qmsInspectionItemDto.getInspectionItemLevel()+"")))).get("name")+"");
            qmsInspectionItemDto.setInspectionToolName(JSONObject.parseObject(String.valueOf(JSONObject.parseArray(inspectionTools.get(0).getParaValue()).get(Integer.parseInt(qmsInspectionItemDto.getInspectionTool()+"")))).get("name")+"");
        }
        hashMap.clear();
        return list;
    }

    class SecurityRun implements Runnable{

        private String code;

        public SecurityRun(String code){
            this.code = code;
        }

        @Override
        public void run()  {
            SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
            searchSysSpecItem.setSpecCode(this.code);
            List<SysSpecItem> list = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();
//            synchronized(this){
//                Map<String, List<SysSpecItem>> map = hashMap;
//                map.put(code,list);
//            }
            Map<String, List<SysSpecItem>> map = hashMap;
            map.put(code,list);

        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(QmsInspectionItem qmsInspectionItem) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        qmsInspectionItem.setCreateTime(new Date());
        qmsInspectionItem.setCreateUserId(user.getUserId());
        qmsInspectionItem.setModifiedTime(new Date());
        qmsInspectionItem.setModifiedUserId(user.getUserId());
        qmsInspectionItem.setStatus(StringUtils.isEmpty(qmsInspectionItem.getStatus())?1:qmsInspectionItem.getStatus());
        qmsInspectionItem.setInspectionItemCode(getOdd());

        int i = qmsInspectionItemMapper.insertUseGeneratedKeys(qmsInspectionItem);

        QmsHtInspectionItem baseHtProductFamily = new QmsHtInspectionItem();
        BeanUtils.copyProperties(qmsInspectionItem,baseHtProductFamily);
        qmsHtInspectionItemMapper.insert(baseHtProductFamily);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(QmsInspectionItem qmsInspectionItem) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        qmsInspectionItem.setModifiedTime(new Date());
        qmsInspectionItem.setModifiedUserId(user.getUserId());

        QmsHtInspectionItem baseHtProductFamily = new QmsHtInspectionItem();
        BeanUtils.copyProperties(qmsInspectionItem,baseHtProductFamily);
        qmsHtInspectionItemMapper.insert(baseHtProductFamily);

        return qmsInspectionItemMapper.updateByPrimaryKeySelective(qmsInspectionItem);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        List<QmsHtInspectionItem> qmsHtQualityInspections = new ArrayList<>();
        String[] idsArr  = ids.split(",");
        for (String id : idsArr) {
            QmsInspectionItem qmsInspectionItem = qmsInspectionItemMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(qmsInspectionItem)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            QmsHtInspectionItem qmsHtInspectionItem = new QmsHtInspectionItem();
            BeanUtils.copyProperties(qmsInspectionItem,qmsHtInspectionItem);
            qmsHtQualityInspections.add(qmsHtInspectionItem);
        }

        qmsHtInspectionItemMapper.insertList(qmsHtQualityInspections);

        Example example = new Example(QmsInspectionItemDet.class);
        Example.Criteria criteria = example.createCriteria();
        String[] split = ids.split(",");
        criteria.andIn("inspectionItemId",Arrays.asList(split));
        qmsInspectionItemDetMapper.deleteByExample(example);

        return qmsInspectionItemMapper.deleteByIds(ids);
    }

    /**
     * 生成检验项目单号
     * @return
     */
    public String getOdd(){
        String before = "JYXM";
        String amongst = new SimpleDateFormat("yyMMdd").format(new Date());
        QmsInspectionItem qmsInspectionItem = qmsInspectionItemMapper.getMax();
        String qmsInspectionItemCode = before+amongst+"0000";
        if (StringUtils.isNotEmpty(qmsInspectionItem)){
            qmsInspectionItemCode = qmsInspectionItem.getInspectionItemCode();
        }
        Integer maxCode = Integer.parseInt(qmsInspectionItemCode.substring(10, qmsInspectionItemCode.length()));
        String after = String.format("%04d", ++maxCode);
        String code = before + amongst + after;
        return code;
    }

}
