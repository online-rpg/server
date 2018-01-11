package com.vrpg.server.api.usermanagement;

import com.vrpg.communication.model.networking.envelopes.RequestEnvelope;
import com.vrpg.communication.model.networking.envelopes.ResponseEnvelope;
import com.vrpg.communication.model.networking.requests.Request;
import com.vrpg.communication.model.networking.requests.RequestType;
import com.vrpg.server.api.RestHandler;
import com.vrpg.server.api.exception.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
class UserManagementController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserManagementController.class);

    private final RestHandler restHandler;

    UserManagementController(RestHandler restHandler) {
        this.restHandler = restHandler;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, path = "/login")
    ResponseEnvelope login(@RequestBody RequestEnvelope requestEnvelope) {
        LOGGER.trace("login - {}", requestEnvelope);

        return handle(requestEnvelope, RequestType.LOGIN_REQUEST);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, path = "/register")
    ResponseEnvelope register(@RequestBody RequestEnvelope requestEnvelope) {
        LOGGER.trace("register - {}", requestEnvelope);

        return handle(requestEnvelope, RequestType.REGISTER_REQUEST);
    }

    private ResponseEnvelope handle(RequestEnvelope requestEnvelope, RequestType acceptedRequestType) {
        Request request = requestEnvelope.getRequest();
        if (request == null || request.getRequestType() == null || request.getRequestType() != acceptedRequestType) {
            throw new BadRequestException();
        }

        return restHandler.handle(requestEnvelope);
    }
}
