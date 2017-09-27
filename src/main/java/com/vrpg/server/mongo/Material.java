package com.vrpg.server.mongo;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import static com.vrpg.server.mongo.Material.COLLECTION_NAME;

@Builder
@Getter
@Document(collection = COLLECTION_NAME)
@ToString(of = "id")
public class Material {
    static final String COLLECTION_NAME = "material";

    @Id
    private String id;
    private byte[] data;
}
