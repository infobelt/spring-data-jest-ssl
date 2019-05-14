package com.infobelt.jest.ssl;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.file.Path;

@ConfigurationProperties(
        prefix = "spring.data.jest.ssl"
)
public class JestSslProperties {

    @Getter
    @Setter
    private boolean enabled = false;

    @Getter
    @Setter
    private Path keyStorePath;

    @Getter
    @Setter
    private String keyPassword;

}
