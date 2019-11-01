package com.otlab.here;

// FriendListItem을 이루는 정보

public class FriendItem {
    private String UserId;
    private String UserName;

    FriendItem(String userid, String username){
        UserId = userid;
        UserName = username;
    }
    public String getUserId() {
        return UserId;
    }
    public String getUserName() {
        return UserName;
    }
    public void setUserId(String userId) {
        UserId = userId;
    }
    public void setUserName(String userName) {
        UserName = userName;
    }

}
