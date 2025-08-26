package org.example.b.Controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("testB")
public class testController {
    @GetMapping("/one")
    public String BOne() {
        return "B接口的第一个方法";
    }
}
