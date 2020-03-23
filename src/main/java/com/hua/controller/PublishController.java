package com.hua.controller;

import com.hua.dto.QuesstionDTO;
import com.hua.mapper.QuesstionMapper;
import com.hua.model.Quesstion;
import com.hua.model.User;
import com.hua.service.QuestionServcie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {

    @Autowired
    private QuestionServcie questionServcie;

    //处理编辑功能
    @GetMapping("/publish/{id}")
    public String edit(@PathVariable(name="id") Integer id,
                       Model model) {
        QuesstionDTO quesstion = questionServcie.getById(id);
        model.addAttribute("title",quesstion.getTitle());
        model.addAttribute("description",quesstion.getDescription());
        model.addAttribute("tag",quesstion.getTag());
        model.addAttribute("id",quesstion.getId());
        return "publish";
    }

    @GetMapping("/publish")
    public String publish() {
        return "publish";
    }

    //处理编辑发布功能的
    @PostMapping("/publish")
    public String dopublish(
            @RequestParam(value="title",required = false) String title,
            @RequestParam(value="description",required = false) String description,
            @RequestParam(value="tag" ,required = false) String tag,
            @RequestParam(value = "id" ,required = false) Integer id,
            HttpServletRequest request,
            Model model) {
        //全部是判断
        model.addAttribute("title",title);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);
        if (title == null||title ==""){
            model.addAttribute("error", "标题不能为空");
            return "publish";
        }
        if (description == null||description ==""){
            model.addAttribute("error", "内容不能为空");
            return "publish";
        }
        if (tag == null||tag ==""){
            model.addAttribute("error", "标签不能为空");
            return "publish";
        }

        //判断用户是否登陆 和Index和ProfileContrller里面的方法一样
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
                //如果为空，则返回错误信息        }
                model.addAttribute("error", "用户未登录");
                return "publish";
            }

            Quesstion quesstion = new Quesstion();
            quesstion.setTitle(title);
            quesstion.setDescription(description);
            quesstion.setTag(tag);
            quesstion.setCreator(user.getId());
            quesstion.setId(id);
            quesstion.setGmtCreate(System.currentTimeMillis());
            quesstion.setGmtModified(quesstion.getGmtModified());
            questionServcie.createOrUpdate(quesstion);
            return "redirect:/";
        }
    }
