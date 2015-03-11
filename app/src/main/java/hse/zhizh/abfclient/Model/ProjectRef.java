package hse.zhizh.abfclient.Model;

/**
 * Created by EvgenyMac on 12.03.15.
 * Класс для сущности ссылок проекта. Ссылка необходимо для получения из нее
 * номера SHA коммита для создания сборки
 */
public class ProjectRef {
    private String sha;

    public ProjectRef(String sha){
        this.sha = sha;
    }

    public String getSha(){ return sha;}
}
