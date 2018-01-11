package com.vrpg.server.api.handler;

import com.vrpg.communication.model.networking.envelopes.RequestEnvelope;
import com.vrpg.communication.model.networking.envelopes.ResponseEnvelope;
import com.vrpg.communication.model.networking.requests.RequestType;

public interface RestMessageHandler {

    ResponseEnvelope handle(RequestEnvelope message);

    RequestType getRequestType();
}
