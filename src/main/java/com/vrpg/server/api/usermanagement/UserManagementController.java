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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
class UserManagementController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserManagementController.class);

    private final RestHandler restHandler;

    UserManagementController(RestHandler restHandler) {
        this.restHandler = restHandler;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, path = "/login")
    ResponseEnvelope login(HttpServletRequest request) throws IOException {
        LOGGER.trace("login - {}", request);

        return handle(RequestEnvelope.parseFrom(request.getInputStream()), RequestType.LOGIN_REQUEST);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, path = "/register")
    ResponseEnvelope register(HttpServletRequest request) throws IOException {
        LOGGER.trace("register - {}", request);

        return handle(RequestEnvelope.parseFrom(request.getInputStream()), RequestType.REGISTER_REQUEST);
    }

    private ResponseEnvelope handle(RequestEnvelope requestEnvelope, RequestType acceptedRequestType) {
        Request request = requestEnvelope.getRequest();
        if (request == null || request.getRequestType() == null || request.getRequestType() != acceptedRequestType) {
            throw new BadRequestException();
        }

        return restHandler.handle(requestEnvelope);
    }
}
