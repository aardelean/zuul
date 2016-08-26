package home.consul.zuul

import org.apache.log4j.Logger
import org.apache.log4j.PropertyConfigurator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.client.discovery.DiscoveryClient
import org.springframework.cloud.context.config.annotation.RefreshScope
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

import javax.ws.rs.QueryParam

@RestController
@RequestMapping("/endpoint")
@RefreshScope
@Configuration
class EndpointConfiguration {
    @Value('${configuration-test}')
    String configurationValue

    @Autowired
    DiscoveryClient discoveryClient

    @RequestMapping(path = "/configurationTest")
    String configuration() {
        PropertyConfigurator.configure("log4j.properties");
        Logger log = Logger.getLogger(EndpointConfiguration);
        log.info "hit the unprotected resource"
        configurationValue
    }

    @RequestMapping(path = "/discover", method = RequestMethod.GET)
    String discover(@QueryParam("service") String name){
        discoveryClient.getInstances(name).get(0).uri
    }
}
