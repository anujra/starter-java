package com.twilio.resources;

import com.google.common.base.Function;
import com.google.common.util.concurrent.Futures;
import com.ning.http.client.AsyncHttpClient;
import com.yammer.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author: arjuna
 * Date: 9/11/13
 * Time: 8:06 PM
 */
public class SynchronousResource {

    private AsyncHttpClient asyncHttpClient;

    /**
     * Constructor for Incoming Content Resource
     */
    public SynchronousResource(AsyncHttpClient asyncHttpClient) {
        this.asyncHttpClient = asyncHttpClient;
    }


    private final static Logger LOG = LoggerFactory.getLogger(SynchronousResource.class);

    @POST
    @Timed(name = "synchronous")
    @Path("synchronous")
    public Response genericSyncCallback(
            @DefaultValue("") @FormParam("SmsSid") final String smsSid,
            @DefaultValue("") @FormParam("AccountSid") final String accountSid,
            @DefaultValue("") @FormParam("SmsMessageSid") final String smsMessageSid,
            @DefaultValue("") @FormParam("To") final String to,
            @DefaultValue("") @FormParam("From") final String from,
            @DefaultValue("") @FormParam("Body") final String body,
            @DefaultValue("") @FormParam("ApiVersion") final String ApiVersion,
            @DefaultValue("") @FormParam("SmsStatus") final String SmsStatus,
            @DefaultValue("") @FormParam("FromZip") final String FromZip,
            @DefaultValue("") @FormParam("FromCity") final String FromCity,
            @DefaultValue("") @FormParam("FromState") final String FromState,
            @DefaultValue("") @FormParam("FromCountry") final String FromCountry,
            @DefaultValue("") @FormParam("ToZip") final String ToZip,
            @DefaultValue("") @FormParam("ToCity") final String ToCity,
            @DefaultValue("") @FormParam("ToState") final String ToState,
            @DefaultValue("") @FormParam("ToCountry") final String ToCountry,
            @Context HttpServletRequest httpRequest,
            final MultivaluedMap<String, String> multivaluedParams
    ) throws Exception {

        LOG.debug("CallbackResource::genericSyncCallback, Incoming Request " + multivaluedParams.toString() );

        final AsyncResource asyncResource = new AsyncResource(httpRequest, 5000);

        return asyncResource.process(new AsyncResource.AsyncResponseHandler() {
            @Override
            public com.google.common.util.concurrent.ListenableFuture<Response> processAsync() {

                /// validate all inputs to the request.
                /// check that authToken and callbackURi exists

                boolean validInputs = true; // do a check to ensure your inputs are valid
                if (!validInputs)
                {
                    return Futures.immediateFuture(Response.status(Response.Status.BAD_REQUEST)
                            .entity("A required authToken or callbackUri is missing")
                            .build());
                }


                try {
                    //// this function does something that returns a future.
                    //// Eg get information from a second another resource to construct your twiml
                    ////
                    com.google.common.util.concurrent.ListenableFuture<com.ning.http.client.Response> future = null;  ///

                    /// do something crazy here...
                    //future = asyncHttpClient.executeRequest(....)

                    /// transform the response from a ning.http.client.Response to a javax.ws.rs.core.Response
                    return Futures.transform(future, new Function<com.ning.http.client.Response, Response>() {
                        @Override
                        public Response apply(com.ning.http.client.Response response) {
                            try {
                                Response.ResponseBuilder builder = AsyncResource.ningToJaxRsResponse(response);

                                /// return the response
                                return builder.build();

                            } catch (IOException ie) {
                                LOG.error("genericSyncCallback, Exception: ", ie);
                                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                                        .build();
                            }
                        }
                    });
                } catch (Exception e) {

                    LOG.error("genericSyncCallback, Exception: ", e);
                    return Futures.immediateFuture(Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                            .entity("genericSyncCallback, Exception: " + e.getMessage())
                            .build());
                }
            }

            @Override
            public Response getContinuationExpiredResponse() {
                return Response.status(504)
                        .build();
            }
        });
    }
}
