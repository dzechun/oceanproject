package com.fantechs.provider.base.service.impl;

import com.alibaba.fastjson.JSON;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseBarCodeDto;
import com.fantechs.common.base.general.dto.basic.BaseBarCodeWorkDto;
import com.fantechs.common.base.general.dto.basic.BaseBarcodeRuleSpecDto;
import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderDto;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseBarcodeRuleSpec;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmWorkOrder;
import com.fantechs.common.base.general.entity.basic.BaseBarCode;
import com.fantechs.common.base.general.entity.basic.BaseBarCodeDet;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseBarCode;
import com.fantechs.common.base.general.entity.basic.BaseBarcodeRuleSpec;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.mes.pm.PMFeignApi;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.base.mapper.BaseBarCodeDetMapper;
import com.fantechs.provider.base.mapper.BaseBarCodeMapper;
import com.fantechs.provider.base.mapper.BaseBarcodeRuleSpecMapper;
import com.fantechs.provider.base.service.BaseBarCodeService;
import com.fantechs.provider.base.service.BaseBarcodeRuleSpecService;
import com.fantechs.provider.base.util.BarcodeRuleUtils;
import com.fantechs.provider.base.util.SocketClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
* @author Mr.Lei
* @create 2020/12/22.
*/
@Service
public class BaseBarCodeServiceImpl extends BaseService<BaseBarCode> implements BaseBarCodeService {

    @Resource
    private BaseBarCodeMapper baseBarCodeMapper;
    @Resource
    private SecurityFeignApi securityFeignApi;
    @Resource
    private BaseBarCodeDetMapper baseBarCodeDetMapper;
    @Resource
    private BaseBarcodeRuleSpecMapper baseBarcodeRuleSpecMapper;
    @Resource
    private BaseBarcodeRuleSpecService baseBarcodeRuleSpecService;
    @Resource
    private PMFeignApi pmFeignApi;

    @Value("${print.ip}")
    private String ip;
    @Value("${print.port}")
    private Integer port;

    @Override
    public List<BaseBarCodeDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return baseBarCodeMapper.findList(map);
    }

    /**
     * ?????????????????????
     * @param searchBaseBarCode
     * @return
     */
    @Override
    public BaseBarCodeWorkDto work(SearchBaseBarCode searchBaseBarCode) {
        BaseBarCodeWorkDto baseBarCodeWorkDto = baseBarCodeMapper.sel_work_order(searchBaseBarCode);
        if(StringUtils.isEmpty(baseBarCodeWorkDto)){
            throw new BizErrorException("????????????????????????????????????????????????");
        }
        //????????????
        SearchBaseBarcodeRuleSpec searchBaseBarcodeRuleSpec = new SearchBaseBarcodeRuleSpec();
        searchBaseBarcodeRuleSpec.setBarcodeRuleId(baseBarCodeWorkDto.getBarcodeRuleId());
        List<BaseBarcodeRuleSpecDto> list = baseBarcodeRuleSpecMapper.findList(ControllerUtil.dynamicConditionByEntity(searchBaseBarcodeRuleSpec));
        ArrayList<BaseBarcodeRuleSpec> baseBarcodeRuleSpecs = new ArrayList<>();
        if (StringUtils.isNotEmpty(list)){
            for (BaseBarcodeRuleSpecDto baseBarcodeRuleSpecDto : list) {
                BaseBarcodeRuleSpec baseBarcodeRuleSpec = new BaseBarcodeRuleSpec();
                BeanUtils.copyProperties(baseBarcodeRuleSpecDto,baseBarcodeRuleSpec);
                baseBarcodeRuleSpecs.add(baseBarcodeRuleSpec);
            }
        }
        Example example1 = new Example(BaseBarCode.class);
        example1.createCriteria().andEqualTo("workOrderId", baseBarCodeWorkDto.getWorkOrderId());
        List<BaseBarCode> baseBarCodes = baseBarCodeMapper.selectByExample(example1);
        String maxCode = baseBarCodeMapper.selMaxCode(baseBarCodeWorkDto.getWorkOrderId());
        if(!StringUtils.isEmpty(maxCode)){
            maxCode = BarcodeRuleUtils.getMaxSerialNumber(baseBarcodeRuleSpecs, maxCode);
        }
        String code = BarcodeRuleUtils.analysisCode(baseBarcodeRuleSpecs,maxCode, baseBarCodeWorkDto.getMaterialCode(),null,null);
        baseBarCodeWorkDto.setBarcode(code);
        return baseBarCodeWorkDto;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void download(List<String> savePath, HttpServletResponse response) throws UnsupportedEncodingException {
            SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
            searchSysSpecItem.setSpecCode("FTP");
            ResponseEntity<List<SysSpecItem>> itemList= securityFeignApi.findSpecItemList(searchSysSpecItem);
            List<SysSpecItem> sysSpecItemList = itemList.getData();
            Map map = (Map) JSON.parse(sysSpecItemList.get(0).getParaValue());
//            List<InputStream> inputStreams = downloadFile(map,savePath);
//            List<String> path = new ArrayList<>();
//            for (String s : savePath) {
//                path.add(s.split("/")[1]);
//            }
//            String[] fileName = path.toArray(new String[path.size()]);
//            this.ftpUtil.downloadZipFiles(response,inputStreams,"????????????",fileName);
            String[] filName = savePath.get(0).split("/");
            // ??????????????????
            response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/octet-stream");
            // ?????????????????????????????????
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filName[1], "UTF-8"));
            // ??????????????????
            byte[] buffer = new byte[1024];
            InputStream fis = null;
            BufferedInputStream bis = null;
            try {
                //fis = downloadFile(map,savePath).get(0);
                bis = new BufferedInputStream(fis);
                OutputStream os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
                System.out.println("Download  successfully!");

            } catch (Exception e) {
                System.out.println("Download  failed!");

            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
    }

    /**
     * ??????
     * @param workOrderId
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int print(Long workOrderId) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
        Example example = new Example(BaseBarCode.class);
        example.createCriteria().andEqualTo("workOrderId",workOrderId).andEqualTo("status",(byte)1);
        List<BaseBarCode> baseBarCodes = baseBarCodeMapper.selectByExample(example);
        if(StringUtils.isEmpty(baseBarCodes)){
            throw new BizErrorException("????????????????????????????????????");
        }
//        if(!SocketClient.isConnect){
//            throw new BizErrorException("????????????????????????");
//        }
        try {
            for (BaseBarCode baseBarCode : baseBarCodes) {
                Example example1 = new Example(BaseBarCodeDet.class);
                example1.createCriteria().andEqualTo("barCodeId", baseBarCode.getBarCodeId());
                List<BaseBarCodeDet> baseBarCodeDets = baseBarCodeDetMapper.selectByExample(example1);
                for (BaseBarCodeDet baseBarCodeDet : baseBarCodeDets) {
                    //??????
                    SearchMesPmWorkOrder searchMesPmWorkOrder = new SearchMesPmWorkOrder();
                    searchMesPmWorkOrder.setWorkOrderId(workOrderId);
                    List<MesPmWorkOrderDto> smtWorkOrderDto = pmFeignApi.findWorkOrderList(searchMesPmWorkOrder).getData();
                    if(StringUtils.isEmpty(smtWorkOrderDto)){
                        throw new BizErrorException("????????????????????????");
                    }
                    MesPmWorkOrderDto sdto = smtWorkOrderDto.get(0);
                    Map<String, Object> map = JSON.parseObject(JSON.toJSONString(sdto),Map.class);
                    map.put("QrCode", baseBarCodeDet.getBarCodeContent());

                    //?????????????????????
                    new SocketClient(ip,port);
                    String json = JSON.toJSONString(map);
                    SocketClient.out(json);
                    SocketClient.closeSocket();
                }
                //?????????????????????
                baseBarCode.setStatus((byte)2);
                baseBarCode.setModifiedTime(new Date());
                baseBarCode.setModifiedUserId(currentUserInfo.getUserId());
                baseBarCodeMapper.updateByPrimaryKeySelective(baseBarCode);
            }
        }catch (Exception e){
            throw new BizErrorException("????????????");
        }
        return 1;
    }

    /**
     * ??????????????????
     * @param QrCode
     * @param workOrderId
     * @return
     */
    @Override
    public BaseBarCodeDet verifyQrCode(String QrCode, Long workOrderId) {
        Example example = new Example(BaseBarCode.class);
        example.createCriteria().andEqualTo("workOrderId",workOrderId).andEqualTo("status",(byte)2);
        List<BaseBarCode> list = baseBarCodeMapper.selectByExample(example);
        if(StringUtils.isEmpty(list)){
            return null;
        }
        List<Long> ids = list.stream().map(BaseBarCode::getBarCodeId).collect(Collectors.toList());
        Example example1 =new Example(BaseBarCodeDet.class);
        example1.createCriteria().andIn("barCodeId",ids).andEqualTo("barCodeContent",QrCode).andEqualTo("status",(byte)1);
        List<BaseBarCodeDet> baseBarCodeDets = baseBarCodeDetMapper.selectByExample(example1);
        if(baseBarCodeDets.size()>0){
            return baseBarCodeDets.get(0);
        }
        return null;
    }

    /**
     * ????????????
     * @param record
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int saveCode(BaseBarCodeWorkDto record) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();

        //????????????????????????????????????????????????
        Example example = new Example(BaseBarCode.class);
        example.createCriteria().andEqualTo("workOrderId",record.getWorkOrderId());
        List<BaseBarCode> baseBarCodes = baseBarCodeMapper.selectByExample(example);
        Integer num = baseBarCodes.stream().mapToInt(BaseBarCode::getPrintQuantity).sum();

        if(record.getWorkOrderQuantity().compareTo(BigDecimal.valueOf(num+record.getPrintQuantity()))==-1){
            throw new BizErrorException("????????????????????????????????????");
        }
        record.setStatus((byte)1);
        record.setCreateTime(new Date());
        record.setCreateUserId(currentUserInfo.getUserId());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(currentUserInfo.getUserId());
        int nm = baseBarCodeMapper.insertUseGeneratedKeys(record);

        for (Integer i = 0; i < record.getPrintQuantity(); i++) {
            BaseBarCodeDet baseBarCodeDet = new BaseBarCodeDet();
            baseBarCodeDet.setBarCodeId(record.getBarCodeId());
            SearchBaseBarcodeRuleSpec searchBaseBarcodeRuleSpec = new SearchBaseBarcodeRuleSpec();
            searchBaseBarcodeRuleSpec.setBarcodeRuleId(record.getBarcodeRuleId());
            List<BaseBarcodeRuleSpec> list = baseBarcodeRuleSpecService.findSpec(searchBaseBarcodeRuleSpec);
            String max = baseBarCodeMapper.selMaxCode(record.getWorkOrderId());
            String maxCode = BarcodeRuleUtils.getMaxSerialNumber(list, max);
            //String code = BarcodeRuleUtils.analysisCode(list,maxCode,record.getMaterialCode());
            //???????????????
            String code = BarcodeRuleUtils.analysisCode(list,maxCode,record.getMaterialCode(),null,null);
            baseBarCodeDet.setBarCodeContent(code);
            baseBarCodeDet.setCreateTime(new Date());
            baseBarCodeDet.setCreateUserId(currentUserInfo.getUserId());
            baseBarCodeDet.setModifiedTime(new Date());
            baseBarCodeDet.setModifiedUserId(currentUserInfo.getUserId());
            baseBarCodeDetMapper.insertSelective(baseBarCodeDet);
        }

        return nm;
    }

    /**
     * ????????????????????????
     * @param baseBarCodeDets
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int updateByContent(List<BaseBarCodeDet> baseBarCodeDets) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();

        int num = 0;
        for (BaseBarCodeDet baseBarCodeDet : baseBarCodeDets) {
            baseBarCodeDet.setModifiedUserId(currentUserInfo.getUserId());
            baseBarCodeDet.setModifiedTime(new Date());
            baseBarCodeDet.setStatus((byte)2);
            num=+baseBarCodeDetMapper.updateByPrimaryKeySelective(baseBarCodeDet);
        }
        return num;
    }

    /**
     * ????????????
     * @param workOrderId
     * @return
     */
    @Override
    public List<BaseBarCodeDto> reprintList(String workOrderId) {
        return baseBarCodeMapper.reprintList(workOrderId);
    }

    /**
     * ????????????
     * @param barCodeId
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int reprint(List<String> barCodeId) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();

        try {
            for (String s : barCodeId) {
                BaseBarCode baseBarCode = baseBarCodeMapper.selectByPrimaryKey(Long.parseLong(s));
                SearchMesPmWorkOrder searchMesPmWorkOrder = new SearchMesPmWorkOrder();
                searchMesPmWorkOrder.setWorkOrderId(baseBarCode.getWorkOrderId());
                List<MesPmWorkOrderDto> smtWorkOrderDto = pmFeignApi.findWorkOrderList(searchMesPmWorkOrder).getData();
                if(StringUtils.isEmpty(smtWorkOrderDto)){
                    throw new BizErrorException("????????????????????????");
                }
                Example example1 = new Example(BaseBarCodeDet.class);
                example1.createCriteria().andEqualTo("barCodeId", baseBarCode.getBarCodeId());
                List<BaseBarCodeDet> baseBarCodeDets = baseBarCodeDetMapper.selectByExample(example1);
                for (BaseBarCodeDet baseBarCodeDet : baseBarCodeDets) {
                    //??????
                    MesPmWorkOrderDto sdto = smtWorkOrderDto.get(0);
                    Map<String, Object> map = JSON.parseObject(JSON.toJSONString(sdto),Map.class);
                    map.put("QrCode", baseBarCodeDet.getBarCodeContent());
                    //?????????????????????
                    new SocketClient("192.168.200.56",8098);
                    String json = JSON.toJSONString(map);
                    SocketClient.out(json);
                    SocketClient.closeSocket();
                }
            }
            return 1;
        }catch (Exception e){
            throw new BizErrorException("????????????");
        }
    }

    /**
     * ??????FTP??????
     * @param map
     * @param savePath
     * @return
     */
//    @Transactional(rollbackFor = RuntimeException.class)
//    public List<InputStream> downloadFile(Map map, List<String> savePath) {
//        boolean isLogin = false;
//        List<InputStream> ins = new ArrayList<>();
//        //??????FTP?????????
//        for (String s : savePath) {
//            try {
//                String ip = map.get("ip").toString();
//                Integer port = Integer.parseInt(map.get("port").toString());
//                String username = map.get("username").toString();
//                String password = map.get("password").toString();
//                isLogin = this.ftpUtil.connectFTP(ip, port, username, password);
//                if (isLogin) {
//                    String[] path = s.split("/");
//                    InputStream in = this.ftpUtil.downFile(path[0], path[1]);
//                    ins.add(in);
//                }
//            } catch (Exception e) {
//                throw new BizErrorException(ErrorCodeEnum.valueOf("????????????"));
//            } finally {
//                this.ftpUtil.loginOut();
//            }
//        }
//        return ins;
//    }
}
