package hse.zhizh.abfclient.Model;

import java.util.Date;

/**
 * Created by E-Lev on 23.02.2015.
 */
public class Commit {

    private String name;
    private String hash;
    private String committer;
    private Date date;
    private boolean pushed;

    public Commit(String name, String hash, String committer, Date date, boolean pushed) {
        this.name = name;
        if (name.endsWith("\n") && name.length() > 1) {
            this.name = name.substring(0, name.length() - 2);
        }
        this.hash = hash;
        this.committer = committer;
        this.date = date;
        this.pushed = pushed;
    }

    public String getName()   { return name;  }
    public String getHash()   { return hash;  }
    public String getCommitter()   { return committer;  }
    public Date getDate()     { return date;  }
    public boolean isPushed() { return pushed;}

}
