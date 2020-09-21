package com.fantechs.provider.imes.basic.controller;


import com.fantechs.common.base.entity.basic.SmtProLine;
import com.fantechs.common.base.entity.basic.history.SmtHtProLine;
import com.fantechs.common.base.entity.basic.search.SearchSmtProLine;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.basic.service.SmtHtProLineService;
import com.fantechs.provider.imes.basic.service.SmtProLineService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Auther: wcz
 * @Date: 2020/9/1 16:30
 * @Description:
 * @Version: 1.0
 */
@RestController
@RequestMapping(value = "/smtProLine")
@Api(tags = "生产线管理")
@Slf4j
public class SmtProLineController {
    @Autowired
    private SmtProLineService smtProLineService;

    @Autowired
    private SmtHtProLineService smtHtProLineService;

    @ApiOperation("根据条件查询生产线信息列表")
    @PostMapping("/selectProLines")
    public ResponseEntity<List<SmtProLine>> selectProLines(@RequestBody(required = false) SearchSmtProLine searchSmtProLine){
        Page<Object> page = PageHelper.startPage(searchSmtProLine.getStartPage(),searchSmtProLine.getPageSize());
        List<SmtProLine> smtProLines = smtProLineService.findList(searchSmtProLine);
        return ControllerUtil.returnDataSuccess(smtProLines,(int)page.getTotal());
    }

    @ApiOperation("增加生产线信息")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：proCode、proName、factoryId、workShopId",required = true)@RequestBody SmtProLine smtProLine){
        if(StringUtils.isEmpty(
                smtProLine.getProCode(),
                smtProLine.getProName(),
                smtProLine.getFactoryId(),
                smtProLine.getWorkShopId())){
            return ControllerUtil.returnFailByParameError();
        }
        return ControllerUtil.returnCRUD(smtProLineService.insert(smtProLine));
    }

    @ApiOperation("修改生产线信息")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "生产线信息对象，生产线信息Id必传",required = true)@RequestBody SmtProLine smtProLine){
        if(StringUtils.isEmpty(smtProLine.getProLineId())){
            return ControllerUtil.returnFailByParameError();
        }
        return ControllerUtil.returnCRUD(smtProLineService.updateById(smtProLine));
    }

    @ApiOperation("删除生产线信息")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "生产线对象ID",required = true)@RequestBody List<Long> proLineIds){
        if(StringUtils.isEmpty(proLineIds)){
            return ControllerUtil.returnFailByParameError();
        }
            return ControllerUtil.returnCRUD(smtProLineService.deleteByIds(proLineIds));
    }

    /**
     * 导出数据
     * @return
     * @throws
     */
    @PostMapping(value = "/exportProLines")
    @ApiOperation(value = "导出生产线信息excel",notes = "导出生产线信息excel")
    public void exportProLines(HttpServletResponse response, @ApiParam(value = "查询对象")
                               @RequestBody(required = false) SearchSmtProLine searchSmtProLine){
        List<SmtProLine> list = smtProLineService.exportProLines(searchSmtProLine);
        try {
            // 导出操作
            EasyPoiUtils.exportExcel(list, "生产线信息导出", "生产线信息", SmtProLine.class, "生产线信息.xls", response);
        } catch (Exception e) {
            throw new BizErrorException(e);
        }
    }


    @PostMapping("/selectHtProLines")
    @ApiOperation(value = "根据条件查询生产线履历信息",notes = "根据条件查询生产线履历信息")
    public ResponseEntity<List<SmtHtProLine>> selectHtProLines(@ApiParam(value = "查询对象")@RequestBody SearchSmtProLine searchSmtProLine) {
        Page<Object> page = PageHelper.startPage(searchSmtProLine.getStartPage(),searchSmtProLine.getPageSize());
        List<SmtHtProLine> smtHtDepts=smtHtProLineService.selectHtProLines(searchSmtProLine);
        return  ControllerUtil.returnDataSuccess(smtHtDepts, (int)page.getTotal());
    }
}
