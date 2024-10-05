package org.mjelle.rest;

import java.util.List;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.mjelle.orm.Fruit;

import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/hello")
public class GreetingResource {

    private final Emitter<String> emitter;

    @Inject
    public GreetingResource(@Channel("data2") Emitter<String> emitter) {
        this.emitter = emitter;
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<List<Fruit>> hello() {
        emitter.send("Hello called!");
        return Fruit.listAll(Sort.by("name"));
    }
}
