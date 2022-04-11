package com.company.bookstore.controller;


import com.company.bookstore.domain.Role;
import com.company.bookstore.domain.User;
import com.company.bookstore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;


@Controller
@RequestMapping(path = "/user-panel")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping(path = "/main")
    public String userList(Map<String, Object> model) {
        model.put("users", userRepository.findAll());

        return "user/userList";
    }

    @GetMapping(path = "/editAddUser")
    public String userEditAdd(@RequestParam(name = "id", required = false) Integer id, Map<String, Object> model) {
        User user;
        if (id == null) {
            user = new User();
        } else {
            user = userRepository.findById(id).get();
        }
        model.put("user", user);
        model.put("roles", Role.values());
        return "user/userEditAdd";
    }


    @PostMapping(path = "/save-user")
    public String userSave(@Valid User user, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "user/userEditAdd";
        }
        userRepository.save(user);

        return "redirect:/user-panel/main";
    }

    @GetMapping("/delete-user")
    public String deleteUser(@RequestParam Integer id) {
        userRepository.deleteById(id);
        return "redirect:/user-panel/main";

    }

    @GetMapping("/registration")
    public String registration(Map<String, Object> model) {
        User user = new User();
        model.put("user", user);
        return "user/registration";
    }

    @PostMapping("/registration")
    public String registerUser(@Valid User user, BindingResult bindingResult) {
        User userFromDb = userRepository.findByUsername(user.getUsername());
        if (userFromDb != null) {
            bindingResult.rejectValue("username", "messageCode", "User exists!");
        }
        if (!user.getPassword().equals(user.getPassword2())) {
            bindingResult.rejectValue("password", "messageCode", "your passwords do not match!");
        }

        if (bindingResult.hasErrors()) {
            return "user/registration";
        }
        user.setPassword(new BCryptPasswordEncoder(8).encode(user.getPassword()));
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        userRepository.save(user);

        return "redirect:/login";
    }
}
