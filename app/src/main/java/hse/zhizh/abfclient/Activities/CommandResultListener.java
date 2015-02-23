package hse.zhizh.abfclient.Activities;

/**
 * Получатель извещения о конце выполнения команды
 *
 * Created by E-Lev on 07.01.2015.
 */
public interface CommandResultListener {

    public void onCommandExecuted(int commandId, boolean success);

}
