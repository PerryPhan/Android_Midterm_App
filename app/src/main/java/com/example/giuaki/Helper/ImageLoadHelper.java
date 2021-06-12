package com.example.giuaki.Helper;

import android.os.AsyncTask;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

public class ImageLoadHelper extends AsyncTask<String, Integer, RequestCreator> {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected RequestCreator doInBackground(String... imageUri) {
        return Picasso.get().load(imageUri[0]);
    }

    @Override
    protected void onPostExecute(RequestCreator requestCreator) {
        super.onPostExecute(requestCreator);
    }
}
