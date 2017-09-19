package com.vrrpg.server.resource;

import com.vrrpg.core.communication.model.ApiGameDescription;
import com.vrrpg.core.communication.model.ApiMeshDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@Controller
@RequestMapping("game")
class GameController {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameController.class);

    private final GameDescriptionProvider gameDescriptionProvider;

    GameController(GameDescriptionProvider gameDescriptionProvider) {
        this.gameDescriptionProvider = gameDescriptionProvider;
    }

    @CrossOrigin(origins = "*")
    @GetMapping
    @ResponseBody
    Collection<ApiGameDescription> getGames() throws GameDescriptionNotFoundException {
        LOGGER.trace("getGames");
        return gameDescriptionProvider.getGames().orElseThrow(GameDescriptionNotFoundException::new);
    }

    @CrossOrigin(origins = "*")
    @GetMapping(path = "/{game_id}")
    @ResponseBody
    ApiGameDescription getGameDescription(@PathVariable("game_id") String id) throws GameDescriptionNotFoundException {
        LOGGER.trace("getGameDescription - {}", id);
        return gameDescriptionProvider.getGameDescription(id).orElseThrow(GameDescriptionNotFoundException::new);
    }

    @CrossOrigin(origins = "*")
    @GetMapping(path = "/meshes/{mesh_id}")
    @ResponseBody
    ApiMeshDescription getMeshDescription(@PathVariable("mesh_id") String id) throws MeshDescriptionNotFoundException {
        LOGGER.trace("getMeshDescription - {}", id);
        return gameDescriptionProvider.getMeshDescription(id).orElseThrow(MeshDescriptionNotFoundException::new);
    }
}
