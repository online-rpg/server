package com.vrrpg.server.web;

import com.vrrpg.server.mongo.BlenderObject;
import com.vrrpg.server.mongo.BlenderObjectRepository;
import com.vrrpg.server.mongo.Material;
import com.vrrpg.server.mongo.MaterialRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

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

    @RequestMapping
    public String management(Map<String, Object> model) {
        LOGGER.trace("management - {}", model);
        return "management";
    }

    @RequestMapping(value = "/fileUploadObj", method = RequestMethod.POST)
    public String uploadObj(@RequestParam("myFile") MultipartFile myFile) throws IOException {
        LOGGER.trace("uploadObj - {}", myFile);

        if (myFile.getSize() > 0
                && myFile.getOriginalFilename().endsWith(".obj")
                && !blenderObjectRepository.exists(myFile.getOriginalFilename())) {
            blenderObjectRepository.save(BlenderObject.builder()
                    .id(myFile.getOriginalFilename())
                    .data(myFile.getBytes())
                    .build());
        }

        return "redirect:";
    }

    @RequestMapping(value = "/fileUploadMtl", method = RequestMethod.POST)
    public String uploadMtl(@RequestParam("myFile") MultipartFile myFile) throws IOException {
        LOGGER.trace("uploadMtl - {}", myFile);

        if (myFile.getSize() > 0
                && myFile.getOriginalFilename().endsWith(".mtl")
                && !materialRepository.exists(myFile.getOriginalFilename())) {
            materialRepository.save(Material.builder()
                    .id(myFile.getOriginalFilename())
                    .data(myFile.getBytes())
                    .build());
        }

        return "redirect:";
    }
}
