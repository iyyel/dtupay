package interfaces.rest;

import dto.PaymentRequest;
import dto.TransactionDTO;
import exceptions.EventFailedException;
import exceptions.SendEventFailedException;
import infrastructure.rabbitmq.payment.PaymentFactory;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import services.PaymentEventService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @primary-author Troels (s161791)
 * @co-author Daniel (s151641)
 */
@Tag(ref = "Payment")
@Path("/payment")
public class PaymentResource {

    private final PaymentEventService service = PaymentFactory.getService();

    /**
     * Pay x amount to a merchant
     */
    @Operation(summary = "Pay x amount to a merchant")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response processPayment(PaymentRequest paymentRequest) {
        try {
            TransactionDTO transactionDTO = service.sendProcessPaymentEvent(paymentRequest);
            if (transactionDTO.isSuccessful()) {
                return Response
                        .ok().entity(transactionDTO)
                        .build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).entity(transactionDTO).build();
            }
        } catch (SendEventFailedException e) {
            throw new InternalServerErrorException(e.getMessage());
        } catch (EventFailedException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    /**
     * Refund amount to customer
     */
    @Operation(summary = "Refund amount to customer")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("refund")
    public Response processRefund(PaymentRequest paymentRequest) {
        try {
            TransactionDTO transactionDTO = service.sendProcessRefundEvent(paymentRequest);
            if (transactionDTO.isSuccessful()) {
                return Response.ok().entity(transactionDTO).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).entity(transactionDTO).build();
            }
        } catch (SendEventFailedException e) {
            throw new InternalServerErrorException(e.getMessage());
        } catch (EventFailedException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

}