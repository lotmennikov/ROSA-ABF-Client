package hse.zhizh.abfclient.Model;

/**
 * Created by EvgenyMac on 12.03.15.
 * Класс для сущности ссылок проекта. Ссылка необходимо для получения из нее
 * номера SHA коммита для создания сборки
 */
public class ProjectRef {
    private String sha;
    private String ref;

    public ProjectRef(String sha,String ref){
        this.sha = sha; this.ref = ref;
    }

    public String getRef() {return ref;}
    public String getSha(){ return sha;}
}
