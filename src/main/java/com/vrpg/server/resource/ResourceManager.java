package com.vrpg.server.resource;

import org.springframework.core.io.Resource;

interface ResourceManager {

    Resource getResource(String name, String extension);
}
