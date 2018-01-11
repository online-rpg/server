package com.vrpg.server.api.resource;

import org.springframework.core.io.Resource;

interface MeshResourceManager extends ResourceManager {
    @Override
    default Resource getResource(String name, String extension) {
        if ("manifest".equalsIgnoreCase(extension)) {
            return null;
        }
        return getMesh(name, extension);
    }

    Resource getMesh(String name, String extension);
}
