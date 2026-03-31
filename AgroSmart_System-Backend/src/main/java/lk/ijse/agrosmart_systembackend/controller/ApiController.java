package lk.ijse.agrosmart_systembackend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

    @RequestMapping("home")
    public String homePage(){
        return "homePage";
    }

    @RequestMapping("user")
    public String userPage(){
        return "userPage";
    }
}
