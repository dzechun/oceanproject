package com.fantechs.controller;

import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.entity.QmsProcessModelShow;
import com.fantechs.entity.QmsProcessPassRateModel;
import com.fantechs.service.QmsProcessService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author
 * @Date 2021/12/24
 */
@RestController
@Api(tags = "雷赛-质量管理看板")
@RequestMapping("/qmsProcess")
@Validated
public class QmsProcessController {
    @Resource
    private QmsProcessService qmsProcessService;

    @PostMapping("/findList")
    @ApiOperation("组装实时合格率")
    public ResponseEntity<List<QmsProcessModelShow>> findList() {
        List<QmsProcessModelShow> resultList = qmsProcessService.findList();
        return ControllerUtil.returnDataSuccess(resultList, resultList.size());
    }

    @PostMapping("/findProcessRateList")
    @ApiOperation("中间部分柱状图")
    public ResponseEntity<Map> findProcessRateList() {
        Map<String,List<QmsProcessModelShow>> map = qmsProcessService.findProcessRateList();
        return ControllerUtil.returnDataSuccess(null,map);
    }

    @PostMapping("/findProcessPassRateList")
    @ApiOperation("一次通过率")
    public ResponseEntity<List<QmsProcessPassRateModel>> findProcessPassRateList() {
        List<QmsProcessPassRateModel> resultList = qmsProcessService.findProcessPassRateList();
        return ControllerUtil.returnDataSuccess(resultList, resultList.size());
    }

}
