package com.hang.reiji.settings;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.cbor.MappingJackson2CborHttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * 配置类
 */
@Slf4j
@Configuration
public class AppConfig implements WebMvcConfigurer {

    /**
     * 添加拦截器
     */
 //   @Override
 //   public void addInterceptors(InterceptorRegistry registry) {
 //
 //       //添加访问页面的拦截器，这个拦截器是自定义的
 //
 //       SafeInterceptor interceptor = new SafeInterceptor();
 //       String[] path = {"/**"};
 //       String[] excludePath = {"/backend/page/login/login.html",
 //                               "/employee/login",
 //                               "/employee/logout",
 //                               "/front/page/login.html",
 //                               "/**/*.js",
 //                               "/**/*.css",
 //                               "/**/*.ico",
 //                               "/**/*.png",
 //                               "/**/*.ttf",
 //                               "/**/*.woff",
 //                               "/**/*.woff2",
 //                               "/**/*.otf",
 //                               "/**/*.map"};
 //       registry.addInterceptor(interceptor).addPathPatterns(path).excludePathPatterns(excludePath);
 //   }

    /**
     * 创建一个mybatisPlus的的拦截器扔进spring容器
     * 作用是：启用IPage的分页功能
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(){
        //创建一个mybatisPlus的的拦截器,这个拦截器相当于是一个容器，能装东西,本质是一个List
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        //往拦截器里装一个分页拦截器
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return interceptor;
    }

    /**
     * 后端的Long类型返回给前端后，会丢失精度，后几位变成0.
     * 解决思路：在返回数据给前端的时候把Long转成String。
     * 解决办法：
     *      方法1，在实体类的变量上加@JsonSerialize(using = ToStringSerializer.class)。这种办法需要一个一个加。
     *      方法2，使用MybatisPlus提供的工具，在配置类实现WebMvcConfigurer接口，重写extendMessageConverters方法。
     *            这样就能自动转换了。
     */
    /*@Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(new Jackson2ObjectMapper());
        converters.add(0,converter);
    }*/
}
