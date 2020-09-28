package com.fantechs.controller;

import com.bstek.ureport.export.ExportManager;
import com.bstek.ureport.export.html.HtmlReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/exportHtml")
    public HtmlReport exportHtml(HttpServletRequest request){
        Map<String,Object> parameters=new HashMap<String,Object>();
        parameters.put("userId","1");
        return exportManager.exportHtml("file:user.ureport.xml",request.getContextPath(),parameters);
    }
}
