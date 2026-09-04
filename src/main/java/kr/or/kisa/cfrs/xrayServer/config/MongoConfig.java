package kr.or.kisa.cfrs.xrayServer.config;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class MongoConfig {
    @Value("${rmus.mongodb.uri}")
    private String rmusUri;

    @Value("${rmus.mongodb.database}")
    private String rmusDbName;

    @Value("${cfrs.mongodb.uri}")
    private String cfrsUri;

    @Value("${cfrs.mongodb.database}")
    private String cfrsDbName;

    @Primary
    @Bean
    public MongoClient rmusMongoClient() {
        return MongoClients.create(rmusUri);
    }

    @Primary
    @Bean
    public MongoDatabase mongoDatabase() {
        CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build())
        );
        return rmusMongoClient().getDatabase(rmusDbName).withCodecRegistry(pojoCodecRegistry);
    }

    @Bean
    public MongoClient cfrsMongoClient() {
        return MongoClients.create(cfrsUri);
    }

    @Bean
    public MongoDatabase cfrsDatabase() {
        CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build())
        );
        return cfrsMongoClient().getDatabase(cfrsDbName).withCodecRegistry(pojoCodecRegistry);
    }
}
