package com.vrpg.server.mongo;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Builder
@Getter
@Document(collection = GameDescription.COLLECTION_NAME)
@ToString(of = "id")
public class GameDescription {
    static final String COLLECTION_NAME = "game-description";

    @Id
    private String id;
    private String name;
    private List<String> objects;
}
