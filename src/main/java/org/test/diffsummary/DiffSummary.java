package org.test.diffsummary;

public class DiffSummary {
    private String changeType;
    private String oldPath;
    private String newPath;
    private int linesAdded;
    private int linesDeleted;




    private DiffSummary(Builder builder){
        this.changeType = builder.changeType;
        this.linesAdded = builder.linesAdded;
        this.linesDeleted = builder.linesDeleted;
        this.newPath = builder.newPath;
        this.oldPath = builder.oldPath;
    }


    public static Builder builder(){
        return new Builder();
    }

    public String getChangeType() {
        return changeType;
    }

    public String getOldPath() {
        return oldPath;
    }


    public String getNewPath() {
        return newPath;
    }


    public int getLinesAdded() {
        return linesAdded;
    }


    public int getLinesDeleted() {
        return linesDeleted;
    }

    public static class Builder {
        private String changeType;
        private String oldPath;
        private String newPath;
        private int linesAdded;
        private int linesDeleted;

        private Builder(){

        }

        public Builder changeType(String changeType){
            this.changeType = changeType;
            return this;
        }

        public Builder oldPath(String oldPath){
            this.oldPath = oldPath;
            return this;
        }

        public Builder newPath(String newPath){
            this.newPath = newPath;
            return this;
        }

        public Builder linesAdded(int linesAdded){
            this.linesAdded = linesAdded;
            return this;
        }

        public Builder linesDeleted(int linesDeleted){
            this.linesDeleted = linesDeleted;
            return this;
        }


        public DiffSummary build(){
            return new DiffSummary(this);
        }
    }






}
