import java.util.Date;

/**
 * Created by Administrator on 3/3/2015.
 */
public class Commit {
    private String name;
    private String hash;
    private Date date;
    private boolean isPushed;

    public Commit(String name, String hash, Date date, boolean isPushed) {
        this.name = name;
        this.hash = hash;
        this.date = date;
        this.isPushed = isPushed;
    }
}
