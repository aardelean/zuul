package home.consul.zuul

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.netflix.zuul.EnableZuulProxy
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@Configuration
@EnableAutoConfiguration
@EnableZuulProxy
@EnableDiscoveryClient
@EnableOAuth2Sso
class ZuulApplication extends WebSecurityConfigurerAdapter {

    @Value('#{\'${unprotectedResources}\'.split(\',\')}')
    List<String> unprotectedResources
    @Value('#{\'${userResources}\'.split(\',\')}')
    List<String> userResources
    @Value('#{\'${channelAdminResources}\'.split(\',\')}')
    List<String> channelAdminResources
    @Value('#{\'${superuserResources}\'.split(\',\')}')
    List<String> superuserResources

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers(splitPaths(userResources)).hasRole("USER")
                .antMatchers(splitPaths(channelAdminResources)).hasRole("CHANNEL_ADMIN")
                .antMatchers(splitPaths(superuserResources)).hasRole("SUPERUSER")
                .antMatchers(splitPaths(unprotectedResources)).permitAll()
    }

    private String[] splitPaths(List<String> resources) {
        resources.toArray(new String[resources.size()])
    }

    static void main(String[] args) {
        new SpringApplicationBuilder(ZuulApplication).run(args)
    }

}
