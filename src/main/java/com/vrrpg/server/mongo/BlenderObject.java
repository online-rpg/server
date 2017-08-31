package com.vrrpg.server.mongo;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import static com.vrrpg.server.mongo.BlenderObject.COLLECTION_NAME;

@Builder
@Getter
@Document(collection = COLLECTION_NAME)
@ToString(of = "id")
public class BlenderObject {
    static final String COLLECTION_NAME = "blender-object";

    @Id
    private String id;
    private byte[] data;
}
