package com.bookstore.controller;

import com.bookstore.dto.requestDTO.UserRequestDTO;
import com.bookstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/writer")
    public String main(UserRequestDTO dto, Model model) {
        String writer = userService.getWriterName(dto);
        model.addAttribute("writer", writer);
        return "main";
    }
}
