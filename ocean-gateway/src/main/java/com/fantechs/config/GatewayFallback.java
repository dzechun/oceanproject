package com.fantechs.config;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.response.ResponseEntity;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * @author lfz
 */
@Component
public class GatewayFallback implements FallbackProvider {
    @Override
    public String getRoute() {
        return null;
    }

    /**
     * 当服务无法执行时，该方法返回托底信息
     *
     * @param route
     * @param cause
     * @return
     */
    @Override
    public ClientHttpResponse fallbackResponse(String route, Throwable cause) {
        return new ClientHttpResponse() {

            /**
             *ClientHttpResponse的fallback的状态码，返回的是HttpStatus
             * @return
             */
            @Override
            public HttpStatus getStatusCode() throws IOException {
                return HttpStatus.OK;
            }


            /**
             *ClientHttpResponse的fallback的状态码，返回的是int
             * @return
             */
            @Override
            public int getRawStatusCode() throws IOException {
                return this.getStatusCode().value();
            }


            /**
             *ClientHttpResponse的fallback的状态码，返回的是String
             * @return
             */
            @Override
            public String getStatusText() throws IOException {
                return this.getStatusCode().getReasonPhrase();
            }

            @Override
            public void close() {

            }

            /**
             *设置响应体信息
             * @return
             */
            @Override
            public InputStream getBody() throws IOException {
                ResponseEntity<String> result = new ResponseEntity<>();
                result.setCode(ErrorCodeEnum.GL99990500.getCode());
                result.setMessage("服务不可用，请与管理员联系");
                return new ByteArrayInputStream(result.toString().getBytes());
            }

            /**
             *设置响应的头信息
             * @return
             */
            @Override
            public HttpHeaders getHeaders() {
                HttpHeaders headers = new HttpHeaders();
                MediaType mediaType = new MediaType("application", "json", Charset.forName("utf-8"));
                headers.setContentType(mediaType);
                return headers;
            }
        };
    }
}
