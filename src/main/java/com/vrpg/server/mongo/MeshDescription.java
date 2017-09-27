package com.vrpg.server.mongo;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import static com.vrpg.server.mongo.MeshDescription.COLLECTION_NAME;

@Builder
@Getter
@Document(collection = COLLECTION_NAME)
@ToString(of = "id")
public class MeshDescription {
    static final String COLLECTION_NAME = "mesh-description";

    @Id
    private String id;
    private String name;
    private String objectPath;
    private Double posX;
    private Double posY;
    private Double posZ;
    private Double scale;
}
