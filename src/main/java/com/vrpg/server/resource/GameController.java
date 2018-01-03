//package com.vrpg.server.resource;
//
//import com.vrpg.communication.resource.GameDescription;
//import com.vrpg.communication.resource.MeshDescription;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Collection;
//
//@Controller
//@RequestMapping("game")
//class GameController {
//    private static final Logger LOGGER = LoggerFactory.getLogger(GameController.class);
//
//    private final GameDescriptionProvider gameDescriptionProvider;
//
//    GameController(GameDescriptionProvider gameDescriptionProvider) {
//        this.gameDescriptionProvider = gameDescriptionProvider;
//    }
//
//    @CrossOrigin(origins = "*")
//    @GetMapping
//    @ResponseBody
//    Collection<GameDescription> getGames() throws GameDescriptionNotFoundException {
//        LOGGER.trace("getGames");
//        return gameDescriptionProvider.getGames().orElseThrow(GameDescriptionNotFoundException::new);
//    }
//
//    @CrossOrigin(origins = "*")
//    @GetMapping(path = "/{game_id}")
//    @ResponseBody
//    GameDescription getGameDescription(@PathVariable("game_id") String id) throws GameDescriptionNotFoundException {
//        LOGGER.trace("getGameDescription - {}", id);
//        return gameDescriptionProvider.getGameDescription(id).orElseThrow(GameDescriptionNotFoundException::new);
//    }
//
//    @CrossOrigin(origins = "*")
//    @GetMapping(path = "/meshes/{mesh_id}")
//    @ResponseBody
//    MeshDescription getMeshDescription(@PathVariable("mesh_id") String id) throws MeshDescriptionNotFoundException {
//        LOGGER.trace("getMeshDescription - {}", id);
//        return gameDescriptionProvider.getMeshDescription(id).orElseThrow(MeshDescriptionNotFoundException::new);
//    }
//}
