package hse.zhizh.abfclient.Session;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;

public interface Session {
    public HttpURLConnection createConnection() throws Exception;
}
