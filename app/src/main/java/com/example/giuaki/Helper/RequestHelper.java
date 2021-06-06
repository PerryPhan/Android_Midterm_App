package com.example.giuaki.Helper;

import android.os.AsyncTask;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Objects;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

// Extends sẽ có 3 tham biến được gọi lần lượt ở <doInBackGround, onProgresspUpdate, onPostExecute>
public class RequestHelper extends AsyncTask<String, Integer, String> {

    private RequestBody formBody;

    /**
     * Truyền formBody từ bên ngoài vào trong class
     * @param formBody An external RequestBody
     */
    public void setRequestBody(RequestBody formBody) {
        this.formBody = formBody;
    }

    /**
     * Generate formBody theo HashMap được truyền từ bên ngoài
     * @param hashMap is a form input map which is sent to web service
     */
    public void buildRequestBody(HashMap<String, String> hashMap) {
        FormBody.Builder builder = new FormBody.Builder();
        for(String key: hashMap.keySet()) {
            builder.add(key, Objects.requireNonNull(hashMap.get(key)));
        }
        this.formBody = builder.build();
    }

    /**
     * Phương thức này được thực hiện trước khi thực hiện httprequest
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    /**
     * Phương thức doInBackground sẽ được thực hiện khi đang thực hiện httprequest, giống kiểu chạy ngầm
     * phương thức doInBackground sẽ có kiểu dữ liệu bằng với key thứ 3 ở trên
     * @param urls array biến để truyền vào thực hiện request
     * @return A JSON string
     */
    @Override
    protected String doInBackground(String... urls) {
        OkHttpClient client = new OkHttpClient();

        Request request = null;
        if (urls[0].equalsIgnoreCase("get")) {
            request = new Request.Builder()
                    .url(urls[1])
                    .build();
        }
        else if (urls[0].equalsIgnoreCase("post")) {
            request = new Request.Builder()
                    .url(urls[1])
                    .post(this.formBody)
                    .build();
        }
        try  {
            assert request != null;
            Response response = client.newCall(request).execute();
            return Objects.requireNonNull(response.body()).string();
        }
        catch (IOException error){
            return error.getMessage();
        }
    }

    /**
     * Phương thức này được thực hiện khi quá trình request chưa xong thì trong thời gian này sẽ làm gì
     * @param values
     */
    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    /**
     * Phương thức này được thực hiện khi đã hoàn thành httprequest
     * @param string
     */
    @Override
    protected void onPostExecute(String string) {
        // Khi execute xong
        super.onPostExecute(string);
    }
}
