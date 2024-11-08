package com.example.bakelink;

public class User {

        private String email;
        private String userType;

        public User() {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
        }

        public User(String email, String userType) {
            this.email = email;
            this.userType = userType;
        }

        public String getEmail() {
            return email;
        }

        public String getUserType() {
            return userType;
        }


}
