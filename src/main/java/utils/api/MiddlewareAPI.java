package utils.api;

import org.apache.commons.text.StringEscapeUtils;

import java.io.IOException;
import java.util.logging.Level;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import utils.LocProperties;
import utils.log.Log;

public class MiddlewareAPI extends CommonAPI {

    protected final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    protected String middlewareURL = LocProperties.getProperties().getProperty("middlewareURL");
    protected String initEndpoint = "\\init";
    protected String executeEndpoint = "\\execute";
    protected String cleanupEndpoint = "\\cleanup";

    public MiddlewareAPI() throws IOException {
        super();
    }

    public void postMiddlewareInit() throws IOException {
        Log.log(Level.FINE, "Post Middleware Init");
        RequestBody bodyCreated = RequestBody.create(JSON, "");
        executeMiddlewareRequest(bodyCreated, initEndpoint);
    }

    public void postMiddlewareExecute(String step) throws IOException {
        Log.log(Level.FINE, "Post Middleware Execute step: " + step);
        String body = toJSON(step);
        RequestBody bodyCreated = RequestBody.create(JSON, body);
        executeMiddlewareRequest(bodyCreated, executeEndpoint);
    }

    public void postMiddlewareCleanup() throws IOException {
        Log.log(Level.FINE, "Post Middleware Init");
        RequestBody bodyCreated = RequestBody.create(JSON, "");
        executeMiddlewareRequest(bodyCreated, cleanupEndpoint);
    }

    private String toJSON(String step) {
        String string2json = "{\"step\":" + "\"" + StringEscapeUtils.escapeJava(step) + "\"}";
        Log.log(Level.FINE, string2json);
        return string2json;
    }

    private void executeMiddlewareRequest(RequestBody requestBody, String endpoint)
            throws IOException {
        Request request = new Request.Builder()
                .url(middlewareURL + endpoint)
                .addHeader("Content-Type", "application/json")
                .post(requestBody)
                .build();
        Log.log(Level.FINE, "RE: " + request.toString());
        Response response = httpClient.newCall(request).execute();
        Log.log(Level.FINE, "Code: " + String.valueOf(response.code()));
        response.close();
    }
}
