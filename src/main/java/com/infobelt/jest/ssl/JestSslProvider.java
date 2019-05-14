package com.infobelt.jest.ssl;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.elasticsearch.jest.HttpClientConfigBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableConfigurationProperties({JestSslProperties.class})
public class JestSslProvider {

    @Autowired
    private JestSslProperties properties;

    @Bean
    public List<HttpClientConfigBuilderCustomizer> build() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException, IOException, CertificateException {
        List<HttpClientConfigBuilderCustomizer> customizers = new ArrayList<>();

        if (properties.isEnabled()) {
            KeyStore truststore = KeyStore.getInstance("jks");

            if (properties.getKeyStorePath() == null) {
                throw new RuntimeException("If you want SSL you need to provide a keyStorePath");
            }

            try (InputStream is = Files.newInputStream(properties.getKeyStorePath())) {
                truststore.load(is, properties.getKeyPassword().toCharArray());
            }
            SSLContextBuilder sslBuilder = SSLContexts.custom()
                .loadTrustMaterial(truststore, null);
            final SSLContext sslContext = sslBuilder.build();
            HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;
            SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
            customizers.add(builder -> builder.sslSocketFactory(sslSocketFactory));

        }

        return customizers;
    }
}
