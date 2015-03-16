package hse.zhizh.abfclient.Model;

/**
 * Created by EvgenyMac on 12.03.15.
 *
 * Класс для репозитория проекта, получается из запроса к проектам (JSON: repositories)
 * id инстанса необходим для запроса создания BuildList'a - параметр save_to_repository_id
 */
public class ProjectRepo {
    private int id;
    private String name;

    public ProjectRepo(int id, String name){
        this.id = id;
        this.name = name;
    }

    public int getId() { return id; }
    public String getName() { return name; }
}
