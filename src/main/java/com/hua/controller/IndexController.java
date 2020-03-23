package com.hua.controller;


import com.hua.dto.pageDTO;


import com.hua.service.QuestionServcie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
public class IndexController {

    @Autowired
    private QuestionServcie questionServcie;

    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(name="page",defaultValue="1") Integer page,
                        @RequestParam(name="size",defaultValue="2") Integer size){


        //在跳转之前，把数据全部查出来返回前端页面
        pageDTO pagiation=questionServcie.list(page,size);
        model.addAttribute("pagiation",pagiation);

        return "index";
    }
}
