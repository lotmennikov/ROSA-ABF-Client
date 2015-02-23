package hse.zhizh.abfclient.api;

/**
 * Created by EvgenyMac on 18.01.15.
 *
 * Базовый интерфейс для запросов к API
 */

public interface ApiRequest {
    String sendRequest(String... request_args) throws Exception;
}
