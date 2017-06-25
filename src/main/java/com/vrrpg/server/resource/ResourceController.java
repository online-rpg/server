package com.vrrpg.server.resource;

import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
class ResourceController {

    private final ResourceManager resourceManager;

    ResourceController(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    @CrossOrigin(origins = "*")
    @GetMapping(path = "/resources/{resource}")
    @ResponseBody
    Resource load(@PathVariable("resource") String resourcePath) {
        return resourceManager.getResource(resourcePath);
    }
}
