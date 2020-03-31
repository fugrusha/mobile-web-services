package com.udemycourse.mobileappws.ui.controller;

import com.udemycourse.mobileappws.ui.model.request.LoginRequestModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ResponseHeader;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @ApiResponses(value = {
            @ApiResponse(code = 200,
            message = "Response Header",
            responseHeaders = {
                    @ResponseHeader(
                            name = "authorization",
                            description = "Bearer JWT token here",
                            response = String.class),
                    @ResponseHeader(
                            name = "userId",
                            description = "public userId here",
                            response = String.class)
            })
    })
    @ApiOperation("User login")
    @PostMapping("/users/login")
    public void fakeLogin(@RequestBody LoginRequestModel loginRequestBody) {
        throw new IllegalStateException("This method should not be called."
                + " This method is implemented by Spring Security");
    }
}
