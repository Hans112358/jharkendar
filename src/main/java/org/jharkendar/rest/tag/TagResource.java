package org.jharkendar.rest.tag;

import org.jharkendar.rest.NotFoundResponse;
import org.jharkendar.rest.tag.CreateTagDto;
import org.jharkendar.rest.tag.UpdateTagDto;
import org.jharkendar.service.TagService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

@Path("/tag")
public class TagResource {

    @Inject
    TagService tagService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response get(@PathParam("id") String id) {
        try {
            return Response.ok(tagService.getById(id)).build();
        } catch (NotFoundException e) {
            return NotFoundResponse.get(id);
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        return Response
                .ok(tagService.getAll())
                .build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(@Valid @NotNull CreateTagDto dto) {
        String id = tagService.create(dto.getName());
        return Response
                .created(URI.create("tag/" + id))
                .build();
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response delete(@PathParam("id") String id) {
        try {
            tagService.delete(id);
        } catch (NotFoundException e) {
            return NotFoundResponse.get(id);
        }
        return Response.ok().build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response update(@PathParam("id") String id, @Valid @NotNull UpdateTagDto dto) {
        try {
            tagService.update(id, dto.name);
            return Response.ok().build();
        } catch (NotFoundException e) {
            return NotFoundResponse.get(id);
        }
    }

}
