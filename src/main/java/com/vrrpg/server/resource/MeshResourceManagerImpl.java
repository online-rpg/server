package com.vrrpg.server.resource;

import com.vrrpg.server.mongo.BlenderObject;
import com.vrrpg.server.mongo.BlenderObjectRepository;
import com.vrrpg.server.mongo.Material;
import com.vrrpg.server.mongo.MaterialRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
class MeshResourceManagerImpl implements MeshResourceManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(MeshResourceManagerImpl.class);

    private final BlenderObjectRepository blenderObjectRepository;
    private final MaterialRepository materialRepository;

    MeshResourceManagerImpl(BlenderObjectRepository blenderObjectRepository, MaterialRepository materialRepository) {
        this.blenderObjectRepository = blenderObjectRepository;
        this.materialRepository = materialRepository;
    }

    @Override
    public Resource getMesh(String name, String extension) {
        LOGGER.trace("getMesh - {}, {}", name, extension);

        if ("obj".equalsIgnoreCase(extension)) {
            BlenderObject blenderObject = blenderObjectRepository.findOne(name + "." + extension);

            if (blenderObject != null) {
                return new ByteArrayResource(blenderObject.getData());
            }
        } else if ("mtl".equalsIgnoreCase(extension)) {
            Material material = materialRepository.findOne(name + "." + extension);

            if (material != null) {
                return new ByteArrayResource(material.getData());
            }
        }

        return null;
    }
}
