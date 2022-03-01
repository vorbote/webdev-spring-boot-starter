package cn.vorbote.webdev;

import cn.vorbote.simplejwt.AccessKeyUtil;
import cn.vorbote.simplejwt.choices.JwtAlgorithm;
import cn.vorbote.web.filter.CorsFilter;
import cn.vorbote.webdev.cors.CorsProperties;
import cn.vorbote.webdev.jwt.JwtProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This is the auto configure class.
 *
 * @author vorbote thills@vorbote.cn
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(value = {JwtProperties.class, CorsProperties.class})
public class WebdevAutoConfigure {

    private final JwtProperties jwtProperties;

    private final CorsProperties corsProperties;

    @Autowired
    public WebdevAutoConfigure(JwtProperties jwtProperties, CorsProperties corsProperties) {
        this.jwtProperties = jwtProperties;
        this.corsProperties = corsProperties;
    }

    @Bean
    @ConditionalOnProperty(name = "vorbote.web-dev.jwt.enabled", havingValue = "true")
    public AccessKeyUtil accessKeyUtil() {
        log.debug("Injecting accessKeyUtil...");
        var info = this.jwtProperties;
        var algorithm = info.getAlgorithm();
        if (algorithm == null) {
            algorithm = JwtAlgorithm.HS256;
        }
        return new AccessKeyUtil(algorithm, info.getSecret(), info.getIssuer());
    }

    @Bean
    @ConditionalOnProperty(name = "vorbote.web-dev.cors.enabled", havingValue = "true")
    public CorsFilter corsFilter() {
        return new CorsFilter(corsProperties.isAllowCredentials(), corsProperties.getAllowOrigin(),
                corsProperties.getAllowMethods(), corsProperties.getAllowMethods(), corsProperties.getExposeHeaders());
    }
}
