package org.jharkendar.rest.summary;

import org.jharkendar.rest.NotFoundResponse;
import org.jharkendar.service.SummaryService;
import org.jharkendar.util.exception.EntityNotFoundException;
import org.jharkendar.util.exception.SummaryNotFoundException;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

@Path("/summary")
public class SummaryResource {

    @Inject
    SummaryService summaryService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response get(@PathParam("id") String id) {
        try {
            return Response.ok(summaryService.getById(id)).build();
        } catch (SummaryNotFoundException e) {
            return NotFoundResponse.get(e.getMessage());
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        return Response
                .ok(summaryService.getAll())
                .build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(@Valid @NotNull CreateSummaryDto dto) {
        try {
            String id = summaryService.create(dto);
            return Response
                    .created(URI.create("summary/" + id))
                    .build();
        } catch (EntityNotFoundException e) {
            return NotFoundResponse.get(e.getMessage());
        }
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response delete(@PathParam("id") String id) {
        try {
            summaryService.delete(id);
        } catch (NotFoundException e) {
            return NotFoundResponse.get(e.getMessage());
        }
        return Response.ok().build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response update(@PathParam("id") String id, @Valid @NotNull UpdateSummaryDto dto) {
        try {
            summaryService.update(id, dto);
            return Response.ok().build();
        } catch (NotFoundException e) {
            return NotFoundResponse.get(e.getMessage());
        }
    }
}
