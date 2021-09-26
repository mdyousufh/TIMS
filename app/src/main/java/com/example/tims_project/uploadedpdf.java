package com.example.tims_project;

public class uploadedpdf {

    String name,url;

    public uploadedpdf() {
    }

    public uploadedpdf(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
