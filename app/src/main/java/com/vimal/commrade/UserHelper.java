package com.vimal.commrade;

public class UserHelper {
    String Name,Mobile,Gender;
    public UserHelper(){};

    public UserHelper(String name, String mobile, String gender) {
        Name = name;
        Mobile = mobile;
        Gender = gender;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }
}
