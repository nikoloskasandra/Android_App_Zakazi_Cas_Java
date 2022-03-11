package com.proba_mpp.zakazi_cas.Model;

public class User {

    private String full_name;
    private String age;
    private String phone;
    private String email;
    private String profile_image;
    private String self_description;
    private String location;

    public User(){

    }


    public User(String full_name, String age, String phone, String email, String profile_image, String self_description, String location) {
        this.full_name = full_name;
        this.age = age;
        this.phone = phone;
        this.email = email;
        this.profile_image = profile_image;
        this.self_description = self_description;
        this.location = location;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getSelf_description() {
        return self_description;
    }

    public void setSelf_description(String self_description) {
        this.self_description = self_description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
