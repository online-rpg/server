package com.vrpg.server.resource;

import com.cloudinary.Cloudinary;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@ConditionalOnProperty(name = {CloudinaryResourceManager.PROPERTY})
class CloudinaryResourceManager implements ImageResourceManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(CloudinaryResourceManager.class);
    static final String PROPERTY = "cloudinary.url";

    private final Cloudinary cloudinary;
    private final OkHttpClient client;

    CloudinaryResourceManager(@Value("${" + PROPERTY + "}") String url) {
        cloudinary = new Cloudinary(url);
        client = new OkHttpClient.Builder()
                .followRedirects(true)
                .followSslRedirects(true)
                .build();
    }

    @Override
    public Resource getResource(String name, String extension) {
        LOGGER.trace("getResource - {}, {}", name, extension);

        String path = name + "." + extension;
        String url = cloudinary.url().generate(path);

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            byte[] bytes = response.body().bytes();
            if (bytes.length == 0) {
                LOGGER.warn("File with name \'{}\' does not exist in cloud!", path);
            }
            return new ByteArrayResource(bytes);
        } catch (IOException e) {
            LOGGER.warn(e.getMessage());
        }
        return null;
    }
}
