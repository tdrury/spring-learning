package com.tdrury.example.mongo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Slf4j
@Testcontainers
class MongoIT {

    @Container
    private static final MongoDBContainer mongo = new MongoDBContainer("mongo:7.0")
                                                          .withExposedPorts(27017);

    String connectionString;
    String host;
    Integer port;

    @BeforeEach
    public void setUp() {
        connectionString = mongo.getConnectionString();
        host = mongo.getHost();
        port = mongo.getFirstMappedPort();
    }

    @Test
    void verifyConnection() {
        log.info("verifyConnection: connectionString={}", connectionString);
        assertThat(connectionString, is(notNullValue()));
        log.info("verifyConnection: host={} port={}", host, port);
        assertThat(host, is(notNullValue()));
        assertThat(port, is(notNullValue()));
    }

    @Test
    void testConnection() {
        assertThat(mongo.isRunning(), is(true));
    }

    @Test
    void testWriteAndRead() {
        try (MongoClient mongoClient = MongoClients.create(connectionString)) {
            MongoCollection<Document> collection = mongoClient.getDatabase("mydb").getCollection("mycollection");

            Document doc = new Document("name", "Test Document").append("value", 123);
            collection.insertOne(doc);

            Document foundDocument = collection.find(Filters.eq("name", "Test Document")).first();
            assertThat(foundDocument.get("value"), is(123));
        }
    }

}