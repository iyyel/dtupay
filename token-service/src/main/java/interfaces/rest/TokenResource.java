/**
 *  @primary-author Emil (s174265)
 *  @co-author Tobias (s173899)
 *
 *  REST interface for the token service
 */

package interfaces.rest;

import dto.Token;
import exceptions.*;
import exceptions.token.InvalidTokenException;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import services.interfaces.ITokenService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Tag(ref = "TokenResource")
@Path("/token")
public class TokenResource {

    @Inject
    ITokenService service;

    @Tag(ref = "requestTokens")
    @POST
    public Response requestTokens(@QueryParam("customerId") String customerId,
                                  @QueryParam("amount") int amount) {
        try {
            service.requestTokens(customerId, amount);
            return Response
                    .status(Response.Status.OK)
                    .build();
        } catch (CustomerNotFoundException | TooManyTokensException | CustomerAlreadyRegisteredException e) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @Tag(ref = "getToken")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{customerId}")
    public Response getToken(@PathParam("customerId") String customerId) {
        try {
            Token token = service.getToken(customerId);
            return Response
                    .status(Response.Status.OK)
                    .entity(token)
                    .build();
        } catch (CustomerNotFoundException | CustomerHasNoTokensException e) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @Tag(ref = "validateToken")
    @POST
    @Path("validate/{tokenId}")
    public Response validateToken(@PathParam("tokenId") String tokenId) {
        try {
            service.validateToken(tokenId);
            return Response
                    .status(Response.Status.OK)
                    .build();
        } catch (CustomerNotFoundException | TokenNotFoundException | InvalidTokenException e) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @Tag(ref = "retireCustomerTokens")
    @DELETE
    @Path("{customerId}")
    public Response retireCustomerTokens(@PathParam("customerId") String customerId) {
        try {
            service.deleteCustomer(customerId);
            return Response
                    .status(Response.Status.OK)
                    .build();
        } catch (CustomerNotFoundException e) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }

}