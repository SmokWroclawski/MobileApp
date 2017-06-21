package ovh.olo.smok.smokwroclawski.Github;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;

import ovh.olo.smok.smokwroclawski.Activity.MainActivity;
import ovh.olo.smok.smokwroclawski.Object.SensorConfigData;

/**
 * Created by Michal on 2017-05-31.
 */

public class GithubReader extends AsyncTask {

    private OkHttpClient client;
    private String url = "https://raw.githubusercontent.com/SmokWroclawski/SmokWroclawskiApp/master/api_keys.txt"; //
    private GithubFileParser githubFileParser;
    private ProgressDialog pd;

    public GithubReader() {
        client = new OkHttpClient();
        githubFileParser = new GithubFileParser();
    }

    private boolean read() {
        try {
            MainActivity.instance.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    pd = ProgressDialog.show(MainActivity.instance, "", "Receiving data from Github...",true);
                }
            });
            Request request = new Request.Builder().url(url).build();
            Response response = getResponse(request);
            MainActivity.instance.setSensorConfigDatas(
                    githubFileParser.parse(
                            response.body().string()
                    )
            );

            return true;
        } catch (IOException e) {
            pd.dismiss();
            Toast.makeText(MainActivity.instance, "Failed! Cannot receive data from Gihub!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return false;
        }
    }

    private Response getResponse(Request request) throws IOException {
        return client.newCall(request).execute();
    }

    @Override
    protected Object doInBackground(Object[] params) {
        read();
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        myHandler.sendEmptyMessage(0);
    }

    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    pd.dismiss();
                    MainActivity.instance.startFindDevices(false);
                    break;
                default:
                    break;
            }
        }
    };
}
