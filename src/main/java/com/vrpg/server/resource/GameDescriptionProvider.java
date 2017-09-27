package com.vrpg.server.resource;

import com.vrpg.server.mongo.GameDescriptionRepository;
import com.vrpg.server.mongo.MeshDescriptionRepository;
import com.vrrpg.core.communication.model.ApiGameDescription;
import com.vrrpg.core.communication.model.ApiMeshDescription;
import com.vrpg.server.mongo.GameDescription;
import com.vrpg.server.mongo.MeshDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.*;
import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;

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

    Optional<Collection<ApiGameDescription>> getGames() {
        LOGGER.trace("getGames");

        Iterable<GameDescription> all = gameDescriptionRepository.findAll();

        if (all == null) {
            return empty();
        }

        List<ApiGameDescription> result = stream(all.spliterator(), false)
                .map(this::convertGameDescription)
                .collect(toList());

        if (result.isEmpty()) {
            return empty();
        }

        return of(result);
    }

    Optional<ApiGameDescription> getGameDescription(String id) {
        LOGGER.trace("getGameDescription - {}", id);

        GameDescription gameDescription = gameDescriptionRepository.findOne(id);

        if (gameDescription == null) {
            return empty();
        }

        return of(convertGameDescription(gameDescription));
    }

    private ApiGameDescription convertGameDescription(GameDescription gameDescription) {
        ApiGameDescription.Builder builder = ApiGameDescription.newBuilder();
        ofNullable(gameDescription.getId()).ifPresent(builder::setId);
        ofNullable(gameDescription.getName()).ifPresent(builder::setName);
        ofNullable(gameDescription.getObjects()).ifPresent(builder::addAllObjects);
        return builder.build();
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
