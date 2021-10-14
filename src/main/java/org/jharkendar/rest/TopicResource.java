package org.jharkendar.rest;

import org.jboss.resteasy.reactive.RestQuery;
import service.TopicService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/topic")
public class TopicResource {

    @Inject
    TopicService topicService;

    @Path("get")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        return Response
                .ok(topicService.getAll())
                .build();
    }

    @Path("get")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@RestQuery String name) {
        return Response
                .ok(topicService.get(name))
                .build();
    }
}
