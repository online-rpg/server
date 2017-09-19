package com.vrrpg.server.resource;

import com.vrrpg.core.communication.model.ApiGameDescription;
import com.vrrpg.core.communication.model.ApiMeshDescription;
import com.vrrpg.server.mongo.GameDescriptionRepository;
import com.vrrpg.server.mongo.MeshDescriptionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static java.util.Optional.ofNullable;

@Component
class GameDescriptionProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameDescriptionProvider.class);

    private final GameDescriptionRepository gameDescriptionRepository;
    private final MeshDescriptionRepository meshDescriptionRepository;

    GameDescriptionProvider(GameDescriptionRepository gameDescriptionRepository,
                            MeshDescriptionRepository meshDescriptionRepository) {
        this.gameDescriptionRepository = gameDescriptionRepository;
        this.meshDescriptionRepository = meshDescriptionRepository;
    }

    ApiGameDescription getGameDescription(String id) {
        LOGGER.trace("getGameDescription - {}", id);
        ApiGameDescription.Builder builder = ApiGameDescription.newBuilder();

        ofNullable(gameDescriptionRepository.findOne(id)).ifPresent(g -> {
            ofNullable(g.getId()).ifPresent(builder::setId);
            ofNullable(g.getName()).ifPresent(builder::setName);
            ofNullable(g.getObjects()).ifPresent(builder::addAllObjects);
        });

        return builder.build();
    }

    ApiMeshDescription getMeshDescription(String id) {
        LOGGER.trace("getMeshDescription - {}", id);
        ApiMeshDescription.Builder builder = ApiMeshDescription.newBuilder();

        ofNullable(meshDescriptionRepository.findOne(id)).ifPresent(m -> {
            ofNullable(m.getId()).ifPresent(builder::setId);
            ofNullable(m.getName()).ifPresent(builder::setName);
            ofNullable(m.getObjectPath()).ifPresent(builder::setObjectPath);
            ofNullable(m.getPosX()).ifPresent(builder::setPosX);
            ofNullable(m.getPosY()).ifPresent(builder::setPosY);
            ofNullable(m.getPosZ()).ifPresent(builder::setPosZ);
            ofNullable(m.getScale()).ifPresent(builder::setScale);
        });

        return builder.build();
    }
}
