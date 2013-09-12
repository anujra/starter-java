package com.twilio.resources;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import org.eclipse.jetty.continuation.Continuation;
import org.eclipse.jetty.continuation.ContinuationSupport;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class AsyncResource {

    /**
     * This interface wraps the async computation we will wait on below.
     */
    public static interface AsyncResponseHandler {
        /**
         * Get a {@link ListenableFuture} that represents the result of the async computation.
         * @return the {@link ListenableFuture}
         */
        public ListenableFuture<Response> processAsync();

        /**
         * What to return in case the computation times out.
         * @return the {@link Response} that represents a timeout
         */
        public Response getContinuationExpiredResponse();
    }

    private final Continuation continuation;
    private final long timeout;

    public AsyncResource(final HttpServletRequest request, final long timeout) {
        this(ContinuationSupport.getContinuation(request), timeout);
    }

    /**
     * Constructor.
     *
     * @param continuation The {@link Continuation}
     * @param timeout How long you want to give the async computation before you time out
     */
    public AsyncResource(final Continuation continuation, final long timeout) {
        this.continuation = continuation;
        this.timeout = timeout;
    }

    /**
     * Wrapper for Dropwizard resource methods to handle Futures as responses using Jetty Continuations transparently.
     *
     * Jetty continuations work by redispatching the request and passing information via HttpServletRequest attributes.
     *
     * On the initial dispatch of the method, this tests for the presence of a request attribute. Because this is the
     * initial request, the attribute is not set.
     *
     * We create a Continuation and call suspend(), this tells Jetty that when this request method is completed, suspend it.
     * We also register a onComplete callback on the Future is running on another thread to resume() Continuation some time
     * in the future. We return null to Jetty and let it suspend the current request. At this point of time no Jetty threads
     * are actively handling this request.
     *
     * When the future is completed, we set the result of the future into the continuation object and call continuation.resume()
     * This tells Jetty that the Future is done, and re-dispatch the request.
     *
     * The Request is re-dispatched by Jetty, everything starts from top-down again. This time, request attribute would have
     * been set. We get a response computed previously in the Future and just return that.
     *
     * Caveats: Everything goes through marshalling and unmarshalling, so there is some additional overhead.
     *
     * As the request is dispatched twice, when using this, you should wrap your method in this as much as possible.
     * Anything outside of AsyncResource will be executed twice, once for each dispatch.
     *
     * @param handler Wrapper for the async computation. As much as possible of your resource computation should be wrapped in this
     * @return The {@link Response} representing either the result of the computation or a timeout
     */
    public Response process(final AsyncResponseHandler handler) {
        @SuppressWarnings("unchecked")
        // Pull the result of the async computation out of the JAX-RS request
        final ListenableFuture<Response> resultFuture = (ListenableFuture<Response>) this.continuation.getAttribute("resultFuture");

        // Result being non-null indicates that this is the second dispatch of this request
        // Two possibilities here: the continuation expired, or the async thing we were waiting on returned successfully
        if (resultFuture != null) {
            if (this.continuation.isExpired()) {
                resultFuture.cancel(true);
                return handler.getContinuationExpiredResponse();
            }
            else {
                try {
                    return resultFuture.get();
                } catch (final Exception e) {
                    return Response.serverError().entity(e).build();
                }
            }
        }

        // If we've made it here, this is the first time this request has been dispatched, so wire up the async computation
        this.continuation.setTimeout(this.timeout);
        this.continuation.suspend();

        try {
            // This future represents the result of whatever async computation we're waiting on
            final ListenableFuture<Response> processAsyncFuture = handler.processAsync();
            this.continuation.setAttribute("resultFuture", processAsyncFuture);

            Futures.addCallback(processAsyncFuture, new FutureCallback<Response>() {
                @Override
                public void onSuccess(final Response result) {
                    // Re-dispatch this request
                    AsyncResource.this.continuation.resume();
                }

                @Override
                public void onFailure(final Throwable t) {
                    // Trying to resume an expired continuation will throw an IllegalStateException
                    // canceling this future in the case of an expired continuation will get us to this point
                    if (!AsyncResource.this.continuation.isExpired()) {
                        AsyncResource.this.continuation.resume();
                    }
                }
            });
        }
        catch (final Exception e) {
            // If anything went wrong, we need to resume the continuation
            this.continuation.setAttribute("resultFuture", Futures.immediateFailedFuture(e));
            this.continuation.resume();
        }

        // Feels dirty, but this is the way you're supposed to do it, promise
        return null;
    }

    /**
     * Converts a ningResponse to JaxRS Response
     * @param response ningResponse
     * @return javax.ws.rs.core.Response
     * @throws java.io.IOException
     */
    public static Response.ResponseBuilder ningToJaxRsResponse(com.ning.http.client.Response response) throws IOException {

        /// build the response, set the statusCode and body
        Response.ResponseBuilder builder = Response.status(response.getStatusCode()).entity(response.getResponseBodyAsBytes());

        /// set the headers
        for (Map.Entry<String, List<String>> header : response.getHeaders()) {
            List<String> values = header.getValue();
            for (String val : values) {
                builder.header(header.getKey(), val);
            }
        }
        return builder;
    }
}