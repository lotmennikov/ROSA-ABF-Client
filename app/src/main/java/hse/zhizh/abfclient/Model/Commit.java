package hse.zhizh.abfclient.Model;

import java.util.Date;

/**
 * Created by E-Lev on 23.02.2015.
 */
public class Commit {

    private String name;
    private String hash;
    private Date date;
    private boolean pushed;

    public Commit(String name, String hash, Date date, boolean pushed) {
        this.name = name;
        this.hash = hash;
        this.date = date;
        this.pushed = pushed;
    }

    public String getName()   { return name;  }
    public String getHash()   { return hash;  }
    public Date getDate()     { return date;  }
    public boolean isPushed() { return pushed;}

}
