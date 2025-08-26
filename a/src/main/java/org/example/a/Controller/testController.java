package org.example.a.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.List;

@RestController
@RequestMapping("testA")
public class testController {

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping("/AUserB")
    public Mono<String> ATestAPI() {
        try {
            // 调试信息
            List<String> services = discoveryClient.getServices();
            System.out.println("发现的所有服务: " + services);

            List<ServiceInstance> instances = discoveryClient.getInstances("BService");
            System.out.println("BService实例: " + instances);

            // 检查BService是否可用
            if (instances == null || instances.isEmpty()) {
                return Mono.just("错误：BService未注册或不可用");
            }

            System.out.println("准备调用服务: http://BService/testB/one");

            // 使用WebClient进行非阻塞调用
            return webClientBuilder.build()
                .get()
                .uri("http://BService/testB/one")
                .retrieve()
                .bodyToMono(String.class)
                .map(result -> "A接口调用了 " + result)
                .onErrorResume(e -> {
                    System.err.println("调用B服务失败: " + e.getMessage());
                    e.printStackTrace();
                    return Mono.just("错误：调用B服务失败 - " + e.getMessage());
                });
        } catch (Exception e) {
            e.printStackTrace();
            return Mono.just("错误：" + e.getMessage());
        }
    }

    @GetMapping("/testDiscovery")
    public String testDiscovery() {
        List<String> services = discoveryClient.getServices();
        StringBuilder result = new StringBuilder("所有服务: " + services + "\n");

        List<ServiceInstance> bInstances = discoveryClient.getInstances("BService");
        result.append("BService实例: ").append(bInstances).append("\n");

        if (!bInstances.isEmpty()) {
            ServiceInstance instance = bInstances.get(0);
            result.append("BService地址: ").append(instance.getUri()).append("\n");
        }

        return result.toString();
    }
}
