package com.vrpg.server.security.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import static com.vrpg.server.security.model.User.COLLECTION_NAME;

@Getter
@Builder
@AllArgsConstructor
@Document(collection = COLLECTION_NAME)
public class User {
    static final String COLLECTION_NAME = "user-store";

    @Id
    private final String email;
    private final String passToken;
}
