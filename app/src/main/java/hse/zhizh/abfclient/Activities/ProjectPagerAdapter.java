package hse.zhizh.abfclient.Activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.io.File;

import hse.zhizh.abfclient.Model.Build;
import hse.zhizh.abfclient.Model.Commit;

/**
 * Created by E-Lev on 07.03.2015.
 */
public class ProjectPagerAdapter extends FragmentPagerAdapter {

    CommitsFragment commitsF;
    ContentsFragment contentsF;
    BuildsFragment buildsF;

    public ProjectPagerAdapter(FragmentManager fm) {
        super(fm);
        commitsF = new CommitsFragment();
        contentsF = new ContentsFragment();
        buildsF = new BuildsFragment();
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                return commitsF;
            case 1:
                return contentsF;
            case 2:
                return buildsF;
        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 3;
    }

// ******* refresh fragments *********

    public File getCurrentDir() {
        if (contentsF != null)
            return contentsF.getCurrentDir();
        else
            return null;
    }

    public void refreshContents() {
        contentsF.refresh();
    }

    public void refreshCommits(Commit[] commits) {
        commitsF.refresh(commits);
    }

    public void refreshBuilds(Build[] builds) {
        buildsF.refresh(builds);
    }
}