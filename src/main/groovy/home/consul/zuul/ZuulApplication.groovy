package home.consul.zuul

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.netflix.hystrix.EnableHystrix
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard
import org.springframework.cloud.netflix.turbine.EnableTurbine
import org.springframework.cloud.netflix.zuul.EnableZuulProxy
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@Configuration
@EnableAutoConfiguration
@EnableZuulProxy
@EnableTurbine
@EnableHystrix
@EnableHystrixDashboard
@EnableDiscoveryClient
@EnableOAuth2Sso
@Import([EndpointConfiguration])
class ZuulApplication extends WebSecurityConfigurerAdapter {

    @Value('#{\'${unprotectedResources}\'.split(\',\')}')
    List<String> unprotectedResources

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers(unprotectedResources.toArray(new String[unprotectedResources.size()])).permitAll()
                .anyRequest().authenticated()
    }

    static void main(String[] args) {
        new SpringApplicationBuilder(ZuulApplication).run(args)
    }

}
