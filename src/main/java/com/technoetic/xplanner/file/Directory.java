package com.technoetic.xplanner.file;

import com.technoetic.xplanner.domain.Identifiable;

import java.util.Set;

public class Directory implements Identifiable {
    private int id;
    private String name;
    private Set subdirectories;
    private Set files;
    private Directory parent;

    public int getId() {
        return id;
    }

    protected void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Directory getParent() {
        return parent;
    }

    public void setParent(Directory parent) {
        this.parent = parent;
    }

    public Set getSubdirectories() {
        return subdirectories;
    }

    public void setSubdirectories(Set subdirectories) {
        this.subdirectories = subdirectories;
    }

    public Set getFiles() {
        return files;
    }

    public void setFiles(Set files) {
        this.files = files;
    }
}
