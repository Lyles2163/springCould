package org.example.a.Controller;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("testA")
public class testController {
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping("/AUserB")
    public String ATestAPI() {
        try {
            // 调试：查看发现的所有服务
            List<String> services = discoveryClient.getServices();
            System.out.println("发现的所有服务: " + services);

            // 调试：查看BService实例
            List<ServiceInstance> instances = discoveryClient.getInstances("BService");
            System.out.println("BService实例: " + instances);

            // 确保我们只调用服务名
            System.out.println("准备调用服务: http://BService/testB/one");
            String serviceResult = restTemplate.getForObject("http://BService/testB/one", String.class);
            return "A接口调用了" + serviceResult;
        } catch (Exception e) {
            e.printStackTrace();
            return "错误：" + e.getMessage();
        }
    }
}


