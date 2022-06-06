//package com.fantechs.auth.filter;
//
//import org.springframework.security.authentication.AuthenticationDetailsSource;
//import org.springframework.security.web.authentication.WebAuthenticationDetails;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.http.HttpServletRequest;
//
///**
// * Created by lfz on 2021/4/9.
// */
//@Component
//public class CustomAuthenticationDetailsSource implements AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> {
//
//    @Override
//    public WebAuthenticationDetails buildDetails(HttpServletRequest context) {
//        return new CustomWebAuthenticationDetails(context);
//    }
//}
