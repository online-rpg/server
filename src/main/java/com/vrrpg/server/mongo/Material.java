package com.vrrpg.server.mongo;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import static com.vrrpg.server.mongo.Material.COLLECTION_NAME;

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
