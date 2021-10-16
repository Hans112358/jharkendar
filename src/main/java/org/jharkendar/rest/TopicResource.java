package org.jharkendar.rest;

import org.jharkendar.service.TopicService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

@Path("/topic")
public class TopicResource {

    @Inject
    TopicService topicService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response get(@PathParam("id") String id) {
        try {
            return Response.ok(topicService.getById(id)).build();
        } catch (NotFoundException e) {
            return NotFoundResponse.get(id);
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        return Response
                .ok(topicService.getAll())
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

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response delete(@PathParam("id") String id) {
        try {
            topicService.delete(id);
        } catch (NotFoundException e) {
            return NotFoundResponse.get(id);
        }
        return Response.ok().build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response update(@PathParam("id") String id, @Valid @NotNull UpdateTopicDto dto) {
        try {
            topicService.update(id, dto.name);
            return Response.ok().build();
        } catch (NotFoundException e) {
            return NotFoundResponse.get(id);
        }
    }
}
