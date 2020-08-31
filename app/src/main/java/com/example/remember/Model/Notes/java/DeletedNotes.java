package com.example.remember.Model.Notes.java;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


@Entity(tableName = "deletedNotes")
public class DeletedNotes{
    @PrimaryKey(autoGenerate = true)
    private int newId;
    @ColumnInfo(name = "key")
        private int previousID;

    @ColumnInfo(name = "title")
        private String title;

    @ColumnInfo(name = "content")
        private String content;

    @ColumnInfo(name = "Created")
        private String created;
    @ColumnInfo(name = "Deleted")
    private String deleted;

    public DeletedNotes(int previousID,String title, String content, String created,String deleted) {
        this.previousID = previousID;
        this.title = title;
        this.content = content;
        this.created = created;
        this.deleted= deleted;
    }

    @Ignore
    public DeletedNotes() {
    }


    public int getNewId() {
        return newId;
    }

    public void setNewId(int newId) {
        this.newId = newId;
    }

    public int getPreviousID() {
        return previousID;
    }

    public void setPreviousID(int previousID) {
        this.previousID = previousID;
    }

    public int getId() {
            return previousID; }
            public void setId(int id) {
            this.previousID = id;
        }
        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getTimestamp() {
            return created;
        }

        public void setTimestamp(String timestamp) {
            this.created = timestamp;
        }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }
}
