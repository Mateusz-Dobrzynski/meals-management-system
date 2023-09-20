package org.lookout_studios.meals_management_system.meals_management_system;

public class FridgeCreationRequest {
    private int userId;
    private String fridgeName;
    private String userToken;
    private String refreshToken;
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public String getFridgeName() {
        return fridgeName;
    }
    public void setFridgeName(String fridgeName) {
        this.fridgeName = fridgeName;
    }
    public String getUserToken() {
        return userToken;
    }
    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }
    public String getRefreshToken() {
        return refreshToken;
    }
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
