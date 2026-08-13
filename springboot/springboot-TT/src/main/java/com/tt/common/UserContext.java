package com.tt.common;

public class UserContext {

    private static final ThreadLocal<String> USER_ID = new ThreadLocal<>();
    private static final ThreadLocal<String> USER_NAME = new ThreadLocal<>();
    private static final ThreadLocal<String> ROLE = new ThreadLocal<>();

    public static void set(String userId, String userName, String role) {
        USER_ID.set(userId);
        USER_NAME.set(userName);
        ROLE.set(role);
    }

    public static String getUserId() {
        return USER_ID.get();
    }

    public static String getUserName() {
        return USER_NAME.get();
    }

    public static String getRole() {
        return ROLE.get();
    }

    public static void clear() {
        USER_ID.remove();
        USER_NAME.remove();
        ROLE.remove();
    }
}
