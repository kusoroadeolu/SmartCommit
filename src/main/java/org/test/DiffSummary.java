package org.test;

import org.eclipse.jgit.diff.DiffEntry;

public class DiffSummary {
    private String changeType;
    private String oldPath;
    private String newPath;
    private int linesAdded;
    private int linesDeleted;

    public DiffSummary(){

    }

    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

    public String getOldPath() {
        return oldPath;
    }

    public void setOldPath(String oldPath) {
        this.oldPath = oldPath;
    }

    public String getNewPath() {
        return newPath;
    }

    public void setNewPath(String newPath) {
        this.newPath = newPath;
    }

    public int getLinesAdded() {
        return linesAdded;
    }

    public void setLinesAdded(int linesAdded) {
        this.linesAdded = linesAdded;
    }

    public int getLinesDeleted() {
        return linesDeleted;
    }

    public void setLinesDeleted(int linesDeleted) {
        this.linesDeleted = linesDeleted;
    }

    //Map a diff entry to a diff summary
    public void modifyDiffEntry(DiffEntry diffEntry){
        this.oldPath = diffEntry.getOldPath();
        this.newPath = diffEntry.getNewPath();
        this.changeType = diffEntry.getChangeType().name();
    }
}
