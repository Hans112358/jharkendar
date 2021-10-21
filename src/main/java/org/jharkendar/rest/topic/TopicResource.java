package org.jharkendar.rest.topic;

import org.jharkendar.rest.MethodNotAllowedResponse;
import org.jharkendar.rest.NotFoundResponse;
import org.jharkendar.service.TopicService;
import org.jharkendar.util.exception.TopicNotFoundException;

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
        } catch (TopicNotFoundException e) {
            return NotFoundResponse.get(e.getMessage());
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
        } catch (TopicNotFoundException e) {
            return NotFoundResponse.get(e.getMessage());
        } catch (IllegalStateException e) {
            return MethodNotAllowedResponse.get(e.getMessage());
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
        } catch (TopicNotFoundException e) {
            return NotFoundResponse.get(e.getMessage());
        }
    }
}
