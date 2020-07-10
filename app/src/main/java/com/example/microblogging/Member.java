package com.example.microblogging;

public class Member {
    private String name,discription,imageUri;

    public Member() {
    }

    public Member(String name, String discription, String imageUri) {
        this.name = name;
        this.discription = discription;
        this.imageUri = imageUri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
