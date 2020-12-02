package com.fantechs.provider.client.controller;

import com.fantechs.common.base.response.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by lfz on 2020/11/30.
 */
@RestController
public class LoginController {

    @PostMapping(value="/login")
    public ResponseEntity sendElectronicTagStorage(@ModelAttribute("Identity") String Identity ) {
             return null;
    }
}
