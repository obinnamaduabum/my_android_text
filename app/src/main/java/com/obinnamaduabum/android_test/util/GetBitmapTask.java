package com.obinnamaduabum.android_test.util;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.obinnamaduabum.android_test.adapter.MovieAdapter.MovieVH;

public class GetBitmapTask extends AsyncTask<String, Void, Bitmap> {

        private MovieVH holder;

        public GetBitmapTask(MovieVH holder) {
            this.holder = holder;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return ImageUtil.getBitmapFromURL(params[0]);
        }

        @Override
        protected void onPostExecute(Bitmap result) {

        }
}
