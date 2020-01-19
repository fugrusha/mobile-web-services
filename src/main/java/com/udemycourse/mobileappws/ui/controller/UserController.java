package com.udemycourse.mobileappws.ui.controller;

import com.udemycourse.mobileappws.service.UserService;
import com.udemycourse.mobileappws.shared.dto.UserDTO;
import com.udemycourse.mobileappws.ui.model.request.UserDetailsRequestModel;
import com.udemycourse.mobileappws.ui.model.response.UserRest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String getUser() {
        return "get user";
    }

    @PostMapping
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) {

        UserRest returnValue = new UserRest();

        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(userDetails, userDTO);

        UserDTO createdUser = userService.createUser(userDTO);
        BeanUtils.copyProperties(createdUser, returnValue);

        return returnValue;
    }

    @PutMapping
    public String updateUser() {
        return "update user";
    }

    @DeleteMapping
    public  String deleteUser() {
        return "delete user";
    }
}
