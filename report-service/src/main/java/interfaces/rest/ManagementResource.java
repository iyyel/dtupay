package interfaces.rest;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import services.ReportService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @primary-author Tobias (s173899)
 * @co-author Troels (s161791)
 */

@Tag(ref = "ManagementResource")
@Path("/management")
public class ManagementResource {


    private final static ReportService service = new ReportService();

    @Tag(ref = "All transactions and summery")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response allTransactions() {
            return Response.ok().entity(service.getSummary()).build();
    }

    @Tag(ref = "Get costumer report for a given period")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("report/customer/{customerId}")
    public Response customerReport(@PathParam("customerId") String customerId, @QueryParam("start") String startDate, @QueryParam("end") String endDate ) {
        if(startDate != null && endDate != null) {
            return reportWithStartAndEnd(customerId, startDate, endDate, true);
        } else if (startDate != null) {
            return reportWithStart(customerId, startDate, true);
        } else if (endDate != null) {
            return reportWithEnd(customerId, endDate, true);
        } else {
            return reportWithoutDate(customerId, true);
        }
    }

    @Tag(ref = "Get merchant report for a given period")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("report/merchant/{merchantId}")
    public Response merchantReport(@PathParam("merchantId") String merchantId, @QueryParam("start") String startDate, @QueryParam("end") String endDate ) {
        if(startDate != null && endDate != null) {
            return reportWithStartAndEnd(merchantId, startDate, endDate, false);
        } else if (startDate != null) {
            return reportWithStart(merchantId, startDate, false);
        } else if (endDate != null) {
            return reportWithEnd(merchantId, endDate, false);
        } else {
            return reportWithoutDate(merchantId, false);
        }
    }

    private Date fixDate(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("E, MMM dd yyyy HH:mm:ss");
        return sdf.parse(date);
    }

    private Response reportWithoutDate(String id, boolean customer) {
        if(customer) {
            return Response.ok().entity(service.customerReport(id)).build();
        } else {
            return Response.ok().entity(service.merchantReport(id)).build();
        }
    }

    private Response reportWithEnd(String id, String endDate, boolean customer) {
        Date endDateFixed;
        try {
            endDateFixed = fixDate(endDate);
        } catch (ParseException e) {
            return Response.status(400).entity(e.getMessage()).build();
        }
        if(customer) {
            return Response.ok().entity(service.customerReport(id, new Date(0), endDateFixed)).build();
        } else {
            return Response.ok().entity(service.merchantReport(id, new Date(0), endDateFixed)).build();
        }
    }

    private Response reportWithStart(String id, String startDate, boolean customer) {
        Date startDateFixed;
        try {
            startDateFixed = fixDate(startDate);
        } catch (ParseException e) {
            return Response.status(400).entity(e.getMessage()).build();
        }
        if(customer) {
            return Response.ok().entity(service.customerReport(id, startDateFixed, new Date())).build();
        } else {
            return Response.ok().entity(service.merchantReport(id, startDateFixed, new Date())).build();
        }
    }

    private Response reportWithStartAndEnd(String id, String startDate, String endDate, boolean customer) {
        Date startDateFixed, endDateFixed;
        try {
            startDateFixed = fixDate(startDate);
            endDateFixed = fixDate(endDate);
        } catch (ParseException e) {
            return Response.status(400).entity(e.getMessage()).build();
        }
        if(customer) {
            return Response.ok().entity(service.customerReport(id, startDateFixed, endDateFixed)).build();
        } else {
            return Response.ok().entity(service.merchantReport(id, startDateFixed, endDateFixed)).build();
        }
    }
}
