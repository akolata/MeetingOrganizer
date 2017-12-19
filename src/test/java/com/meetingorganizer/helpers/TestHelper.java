package com.meetingorganizer.helpers;

import com.meetingorganizer.domain.User;

public class TestHelper {

    public static User sampleUser() {
        User user = new User();
        user.setFirstName("first");
        user.setLastName("last");
        user.setEmail("mail@domain.com");
        user.setPassword("pass");

        return user;
    }
}
