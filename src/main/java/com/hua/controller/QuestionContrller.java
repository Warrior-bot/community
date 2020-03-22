package com.hua.controller;

import com.hua.dto.QuesstionDTO;
import com.hua.service.QuestionServcie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class QuestionContrller {

    @Autowired
    private QuestionServcie questionServcie;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id") Integer id,
                           Model model) {
        QuesstionDTO quesstionDTO= questionServcie.getById(id);
        model.addAttribute("question",quesstionDTO);
        return "question";
    }
}
