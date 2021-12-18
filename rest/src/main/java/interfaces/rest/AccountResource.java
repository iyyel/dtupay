package interfaces.rest;

import dto.UserAccountDTO;
import dto.UserRegistrationDTO;
import exceptions.EventFailedException;
import exceptions.SendEventFailedException;
import infrastructure.rabbitmq.account.AccountFactory;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import services.AccountEventService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * @primary-author Kasper (s141250)
 * @co-author Sebastian (s135243)
 */
@Tag(ref = "AccountResource")
@Path("/account")
public class AccountResource {

    private final AccountEventService service = AccountFactory.getService();

    @Tag(ref = "register")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(UserRegistrationDTO userRegistrationDTO) {
        try {
            String internalId = service.sendRegisterEvent(userRegistrationDTO);
            return Response.ok().entity(internalId).build();
        } catch (SendEventFailedException e) {
            throw new InternalServerErrorException(e.getMessage());
        } catch (EventFailedException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @Tag(ref = "getAccount")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Response getAccount(@PathParam("id") String id) {
        try {
            UserAccountDTO userAccountDTO = service.sendGetAccountEvent(id);
            return Response.ok().entity(userAccountDTO).build();
        } catch (SendEventFailedException e) {
            throw new InternalServerErrorException(e.getMessage());
        } catch (EventFailedException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @Tag(ref = "getAccountByCpr")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/by-cpr/{cpr}")
    public Response getAccountByCpr(@PathParam("cpr") String cpr) {
        try {
            UserAccountDTO userAccountDTO = service.sendGetAccountByCprEvent(cpr);
            return Response.ok().entity(userAccountDTO).build();
        } catch (SendEventFailedException e) {
            throw new InternalServerErrorException(e.getMessage());
        } catch (EventFailedException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @Tag(ref = "retireAccount")
    @DELETE
    @Path("{id}")
    public Response retireAccount(@PathParam("id") String id) {
        try {
            service.sendRetireAccountEvent(id);
            return Response.ok().build();
        } catch (SendEventFailedException e) {
            throw new InternalServerErrorException(e.getMessage());
        } catch (EventFailedException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @Tag(ref = "retireAccountByCpr")
    @DELETE
    @Path("/by-cpr/{cpr}")
    public Response retireAccountByCpr(@PathParam("cpr") String cpr) {
        try {
            service.sendRetireAccountByCprEvent(cpr);
            return Response.ok().build();
        } catch (SendEventFailedException e) {
            throw new InternalServerErrorException(e.getMessage());
        } catch (EventFailedException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @Tag(ref = "getAllAccounts")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllAccounts() {
        try {
            List<UserAccountDTO> userAccountDTOs = service.sendGetAllAccountsEvent();
            return Response.ok().entity(userAccountDTOs).build();
        } catch (SendEventFailedException e) {
            throw new InternalServerErrorException(e.getMessage());
        } catch (EventFailedException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

}
