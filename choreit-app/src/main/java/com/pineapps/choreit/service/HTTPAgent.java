package com.pineapps.choreit.service;

import com.pineapps.choreit.domain.Response;
import com.pineapps.choreit.domain.ResponseStatus;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;

import static com.pineapps.choreit.util.Log.logWarn;

public class HTTPAgent {
    private final DefaultHttpClient httpClient;

    public HTTPAgent() {
        httpClient = new DefaultHttpClient();
    }

    public Response<String> fetch(String uri) {
        try {
            HttpGet httpGet = new HttpGet(uri);
            HttpResponse response = httpClient.execute(httpGet);
            String responseContent = IOUtils.toString(response.getEntity().getContent());
            return new Response<String>(ResponseStatus.success, responseContent);
        } catch (Exception e) {
            logWarn(e.toString());
            return new Response<String>(ResponseStatus.failure, null);
        }
    }

    public Response<String> postJSONRequest(String uri, String json) {
        try {
            HttpPost httpPost = new HttpPost(uri);
            StringEntity entity = new StringEntity(json, "UTF-8");
            entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            httpPost.setEntity(entity);
            httpPost.setHeader(HTTP.CONTENT_TYPE, "application/json");
            httpPost.setHeader("Accept", "application/json");

            HttpResponse response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_CREATED) {
                return new Response<String>(ResponseStatus.failure, null);
            }
            String responseContent = IOUtils.toString(response.getEntity().getContent());
            return new Response<String>(ResponseStatus.success, responseContent);
        } catch (Exception e) {
            logWarn(e.toString());
            return new Response<String>(ResponseStatus.failure, null);
        }
    }
}
