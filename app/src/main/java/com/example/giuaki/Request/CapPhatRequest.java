package com.example.giuaki.Request;

import com.example.giuaki.Helper.RequestHelper;
import com.example.giuaki.WebService;

import java.util.concurrent.ExecutionException;

public class CapPhatRequest {
    private RequestHelper requestHelper;

    public CapPhatRequest() {
        requestHelper = new RequestHelper();
    }

    public String doGet(String method) {
        String[] request = {"get", String.format("http://%s/CapPhatController-%s", WebService.host(), method)};
        String response = "";
        try {
            response = new RequestHelper().execute(request).get();
        }
        catch(ExecutionException e){
            response = e.getMessage();
        }
        catch(InterruptedException e){
            response = e.getMessage();
        }
        return response;
    }

}
