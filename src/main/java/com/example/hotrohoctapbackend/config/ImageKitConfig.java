package com.example.hotrohoctapbackend.config;


import io.imagekit.sdk.ImageKit;
import io.imagekit.sdk.config.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class ImageKitConfig {

    @Value("${imagekit.public.key}")
    private String publicKey;

    @Value("${imagekit.private.key}")
    private String privateKey;

    @Value("${imagekit.url.endpoint}")
    private String imageKitEndpoint;


    /**
     * Configures the ImageKit instance.
     * This bean will be used in your service classes to interact with ImageKit.
     *
     * @return ImageKit instance
     */
    @Bean
    public ImageKit imageKit() {
        // Get the singleton instance of ImageKit
        ImageKit imageKit = ImageKit.getInstance();

        // Set the configuration
        Configuration config = new Configuration(publicKey, privateKey, imageKitEndpoint);
        imageKit.setConfig(config); // Setting the configuration for the instance

        return imageKit;  // Return the ImageKit instance
    }
}
