package com.vrpg.server.api.management;

import com.vrpg.server.mongo.BlenderObject;
import com.vrpg.server.mongo.BlenderObjectRepository;
import com.vrpg.server.mongo.Material;
import com.vrpg.server.mongo.MaterialRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("management")
class ManagementController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ManagementController.class);

    private final BlenderObjectRepository blenderObjectRepository;
    private final MaterialRepository materialRepository;

    ManagementController(BlenderObjectRepository blenderObjectRepository, MaterialRepository materialRepository) {
        this.blenderObjectRepository = blenderObjectRepository;
        this.materialRepository = materialRepository;
    }

    @RequestMapping(value = "/fileUploadObj", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity uploadObj(@RequestParam("myFile") MultipartFile myFile) throws IOException {
        LOGGER.trace("uploadObj - {}", myFile);

        if (myFile.getSize() > 0
                && myFile.getOriginalFilename().endsWith(".obj")
                && !blenderObjectRepository.exists(myFile.getOriginalFilename())) {
            blenderObjectRepository.save(BlenderObject.builder()
                    .id(myFile.getOriginalFilename())
                    .data(myFile.getBytes())
                    .build());
            return ResponseEntity.ok().build();
        } else {
            throw new InvalidFileException();
        }
    }

    @RequestMapping(value = "/fileUploadMtl", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity uploadMtl(@RequestParam("myFile") MultipartFile myFile) throws IOException {
        LOGGER.trace("uploadMtl - {}", myFile);

        if (myFile.getSize() > 0
                && myFile.getOriginalFilename().endsWith(".mtl")
                && !materialRepository.exists(myFile.getOriginalFilename())) {
            materialRepository.save(Material.builder()
                    .id(myFile.getOriginalFilename())
                    .data(myFile.getBytes())
                    .build());
            return ResponseEntity.ok().build();
        } else {
            throw new InvalidFileException();
        }
    }
}
