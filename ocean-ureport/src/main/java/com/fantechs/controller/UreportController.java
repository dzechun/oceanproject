package com.fantechs.controller;

import com.bstek.ureport.export.ExportManager;
import com.bstek.ureport.export.html.HtmlReport;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lfz on 2020/9/27.
 */
@RestController
@RequestMapping("/ureport2")
public class UreportController {
    @Autowired
    private ExportManager exportManager;

    @PostMapping("/exportHtml")
    public HtmlReport exportHtml(@ApiParam(value = "必传：fileName",required = true) @RequestBody  Map<String,Object> param, HttpServletRequest request){
        return exportManager.exportHtml(param.get("fileName").toString(),request.getContextPath(),param);
    }
}
