package com.example.tims_project.model;

public class ModelUser {
    String FullName, UserEmail,search, PhoneNumber,image, isOwner, Type,isTenant,uid;
    public ModelUser() {
    }

    public String getType() {
        return Type;
    }

    public ModelUser(String FullName, String UserEmail, String search, String PhoneNumber, String image,
                     String isOwner, String Type, String isTenant ,String uid) {
        this.FullName = FullName;
        this.UserEmail = UserEmail;
        this.search = search;
        this.PhoneNumber = PhoneNumber;
        this.image = image;
        this.isOwner = isOwner;
        this.Type = Type;
        this.uid = uid;
    }

    public void setType(String type) {
        this.Type = type;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        this.FullName = fullName;
    }

    public String getUserEmail() {
        return UserEmail;
    }

    public void setUserEmail(String userEmail) {
        this.UserEmail = userEmail;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.PhoneNumber = phoneNumber;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIsOwner() {
        return isOwner;
    }

    public void setIsOwner(String isOwner) {
        this.isOwner = isOwner;
    }

    public String getIsTenant() {
        return isTenant;
    }

    public void setIsTenant(String isTenant) {
        this.isTenant = isTenant;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}

