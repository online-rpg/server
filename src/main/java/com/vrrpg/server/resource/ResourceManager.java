package com.vrrpg.server.resource;

import org.springframework.core.io.Resource;

interface ResourceManager {

    Resource getResource(String path);
}
