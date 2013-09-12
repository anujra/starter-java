package com.twilio.resources;

import com.ning.http.client.AsyncHttpClient;
import javax.xml.bind.annotation.XmlElement;

import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.resource.factory.SmsFactory;
import com.twilio.sdk.resource.instance.Account;
import com.twilio.sdk.resource.instance.IncomingPhoneNumber;
import com.twilio.sdk.resource.list.AccountList;
import com.twilio.sdk.resource.list.IncomingPhoneNumberList;
import com.twilio.sdk.verbs.Sms;
import com.yammer.metrics.annotation.Timed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.tools.tree.ReturnStatement;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


@Path("/test")
public class SdkTestResource {

    private final static Logger LOG = LoggerFactory.getLogger(SdkTestResource.class);

    private TwilioRestClient twilioRestClient;


    /**
     * Constructor for Incoming Content Resource
     */
    public SdkTestResource(TwilioRestClient twilioRestClient) {
        this.twilioRestClient = twilioRestClient;
    }

    /**
     * Entry point for asynchronous callbacks
     * @return Response
     * @throws Exception
     */
    @POST
    @Timed(name = "send")
    @Path("/send")
    public Response send (
            @DefaultValue("") @FormParam("To") final String to,
            @DefaultValue("") @FormParam("Body") final String body,
            final MultivaluedMap<String, String> multivaluedParams
    ) throws Exception {

        LOG.debug("SmsResource::send, Incoming Request {}", multivaluedParams.toString());

        // Get the main account (The one we used to authenticate the client)
        final Account mainAccount = twilioRestClient.getAccount();

        // Get all accounts including sub accounts
        AccountList accountList = twilioRestClient.getAccounts();

        com.twilio.sdk.resource.instance.Sms sms = null;

        // Get first account...
        final Iterator<Account> itr = accountList.iterator();
        if (itr.hasNext()) {
            final Account a = itr.next();

            // get an incoming number
            IncomingPhoneNumberList numberList = a.getIncomingPhoneNumbers();
            final Iterator<IncomingPhoneNumber> nitr = numberList.iterator();
            {
                if (nitr.hasNext()) {
                    final IncomingPhoneNumber number = nitr.next();
                    String phoneNumber = number.getPhoneNumber();

                    // Send an sms from this number
                    final SmsFactory smsFactory = mainAccount.getSmsFactory();
                    final Map<String, String> smsParams = new HashMap<String, String>();
                    smsParams.put("To", to); // Replace with a valid phone number
                    smsParams.put("From", phoneNumber); // Replace with a valid phone number in your account
                    smsParams.put("Body", body);
                    sms = smsFactory.create(smsParams);
                }
            }
        }

        else if (null != sms)
            return Response.ok().entity(sms.getStatus()).build();
        else
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
}
