package com.udemycourse.mobileappws.ui.controller;

import com.udemycourse.mobileappws.exceptions.UserServiceException;
import com.udemycourse.mobileappws.service.UserService;
import com.udemycourse.mobileappws.shared.dto.UserDTO;
import com.udemycourse.mobileappws.ui.model.request.UserDetailsRequestModel;
import com.udemycourse.mobileappws.ui.model.response.ErrorMessages;
import com.udemycourse.mobileappws.ui.model.response.UserRest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(path = "/{id}",
            produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
    public UserRest getUser(@PathVariable String id) {
        UserRest returnValue = new UserRest();

        UserDTO userDTO = userService.getUserByUserId(id);
        BeanUtils.copyProperties(userDTO, returnValue);
        return returnValue;
    }

    @PostMapping(
            consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE },
            produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }
            )
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {

        if (userDetails.getFirstName().isEmpty()) {
            throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
        }

        UserRest returnValue = new UserRest();

        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(userDetails, userDTO);

        UserDTO createdUser = userService.createUser(userDTO);
        BeanUtils.copyProperties(createdUser, returnValue);

        return returnValue;
    }

    @PutMapping(path = "/{id}",
            consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE },
            produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }
            )
    public UserRest updateUser(@RequestBody UserDetailsRequestModel userDetails, @PathVariable String id) {
        UserRest returnValue = new UserRest();

        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(userDetails, userDTO);

        UserDTO updatedUser = userService.updateUser(id, userDTO);
        BeanUtils.copyProperties(updatedUser, returnValue);

        return returnValue;
    }

    @DeleteMapping
    public  String deleteUser() {
        return "delete user";
    }
}
