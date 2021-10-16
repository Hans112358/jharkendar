package org.jharkendar.rest;

import service.TopicService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Path("/topic")
public class TopicResource {

    @Inject
    TopicService topicService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        return Response
                .ok(topicService.getAll())
                .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@QueryParam("name") String name) {
        String decodedName = URLDecoder.decode(name, StandardCharsets.UTF_8);
        return Response
                .ok(topicService.get(decodedName))
                .build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(@Valid @NotNull CreateTopicDto dto) {
        String id = topicService.create(dto.getName());
        return Response
                .created(URI.create("topic/" + id))
                .build();
    }
}
