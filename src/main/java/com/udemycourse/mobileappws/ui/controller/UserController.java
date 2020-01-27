package com.udemycourse.mobileappws.ui.controller;

import com.udemycourse.mobileappws.exceptions.UserServiceException;
import com.udemycourse.mobileappws.service.UserService;
import com.udemycourse.mobileappws.shared.dto.UserDTO;
import com.udemycourse.mobileappws.ui.model.request.UserDetailsRequestModel;
import com.udemycourse.mobileappws.ui.model.response.ErrorMessages;
import com.udemycourse.mobileappws.ui.model.response.OperationStatusModel;
import com.udemycourse.mobileappws.ui.model.response.RequestOperationStatus;
import com.udemycourse.mobileappws.ui.model.response.UserRest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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

    @DeleteMapping(path = "/{id}",
            produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
    public OperationStatusModel deleteUser(@PathVariable("id") String userId) {
        OperationStatusModel returnValue = new OperationStatusModel();
        returnValue.setOperationName(RequestOperationName.DELETE.name());

        userService.deleteUser(userId);

        returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
        return returnValue;
    }

    @GetMapping(produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
    public List<UserRest> getUsers(
            @RequestParam(value="page", defaultValue = "0") int page,
            @RequestParam(value = "limit", defaultValue = "25") int limit
    ) {
        if (page>0) page = page - 1;

        List<UserRest> returnValue = new ArrayList<>();

        List<UserDTO> users = userService.getUsers(page, limit);

        for (UserDTO userDTO : users) {
            UserRest userModel = new UserRest();
            BeanUtils.copyProperties(userDTO, userModel);
            returnValue.add(userModel);
        }

        return returnValue;
    }
}
