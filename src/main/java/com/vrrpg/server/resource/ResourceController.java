package com.vrrpg.server.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("resources")
class ResourceController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceController.class);

    private final ResourceManager resourceManager;
    private final ResourceManager meshResourceManager;

    ResourceController(ImageResourceManager resourceManager, MeshResourceManager meshResourceManager) {
        this.resourceManager = resourceManager;
        this.meshResourceManager = meshResourceManager;
    }

    @CrossOrigin(origins = "*")
    @GetMapping(path = "/textures/{resource_name}.{extension}")
    @ResponseBody
    Resource load(@PathVariable("resource_name") String resourceName, @PathVariable("extension") String extension) {
        LOGGER.trace("load - {}, {}", resourceName, extension);
        return resourceManager.getResource(resourceName, extension);
    }

    @CrossOrigin(origins = "*")
    @GetMapping(path = "/meshes/{mesh_name}.{extension}")
    @ResponseBody
    Resource loadObject(@PathVariable("mesh_name") String meshName, @PathVariable("extension") String extension) {
        LOGGER.trace("loadObject - {}, {}", meshName, extension);
        return meshResourceManager.getResource(meshName, extension);
    }
}
