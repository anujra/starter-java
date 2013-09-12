package com.twilio.resources;

import com.ning.http.client.AsyncHttpClient;
import javax.xml.bind.annotation.XmlElement;
import com.yammer.metrics.annotation.Timed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.xml.bind.annotation.XmlRootElement;


@Path("/")
public class SmsResource {

    private final static Logger LOG = LoggerFactory.getLogger(SmsResource.class);

    /**
     * Constructor for Incoming Content Resource
     */
    public SmsResource() {
    }

    /**
     * SmsResponse class
     */
    @XmlRootElement(name = "Response")
    public static class SmsResponse {

        @XmlElement(name = "Sms")
        public String sms;
    }

   /**
    * Entry point for asynchronous callbacks
    * @return Response
    * @throws Exception
    */
    @POST
    @Timed(name = "sms")
    @Path("/sms")
    @Produces(MediaType.APPLICATION_XML)
    public SmsResponse sms (
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
            final MultivaluedMap<String, String> multivaluedParams
    ) throws Exception {

        LOG.debug("SmsResource::sms, Incoming Request {}", multivaluedParams.toString());
        SmsResponse rep = new SmsResponse();
        rep.sms = "Msg from:" + from + ", To:" + to + ", Body:" + body;
        return rep;
    }
}
