package com.vrrpg.server.resource;

import com.vrrpg.core.communication.model.ApiGameDescription;
import com.vrrpg.core.communication.model.ApiMeshDescription;
import com.vrrpg.server.mongo.GameDescription;
import com.vrrpg.server.mongo.GameDescriptionRepository;
import com.vrrpg.server.mongo.MeshDescription;
import com.vrrpg.server.mongo.MeshDescriptionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static java.util.Optional.*;

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

    Optional<ApiGameDescription> getGameDescription(String id) {
        LOGGER.trace("getGameDescription - {}", id);

        GameDescription gameDescription = gameDescriptionRepository.findOne(id);

        if (gameDescription == null) {
            return empty();
        }

        ApiGameDescription.Builder builder = ApiGameDescription.newBuilder();

        ofNullable(gameDescription.getId()).ifPresent(builder::setId);
        ofNullable(gameDescription.getName()).ifPresent(builder::setName);
        ofNullable(gameDescription.getObjects()).ifPresent(builder::addAllObjects);

        return of(builder.build());
    }

    Optional<ApiMeshDescription> getMeshDescription(String id) {
        LOGGER.trace("getMeshDescription - {}", id);

        MeshDescription meshDescription = meshDescriptionRepository.findOne(id);

        if (meshDescription == null) {
            return empty();
        }

        ApiMeshDescription.Builder builder = ApiMeshDescription.newBuilder();

        ofNullable(meshDescription.getId()).ifPresent(builder::setId);
        ofNullable(meshDescription.getName()).ifPresent(builder::setName);
        ofNullable(meshDescription.getObjectPath()).ifPresent(builder::setObjectPath);
        ofNullable(meshDescription.getPosX()).ifPresent(builder::setPosX);
        ofNullable(meshDescription.getPosY()).ifPresent(builder::setPosY);
        ofNullable(meshDescription.getPosZ()).ifPresent(builder::setPosZ);
        ofNullable(meshDescription.getScale()).ifPresent(builder::setScale);

        return of(builder.build());
    }
}
