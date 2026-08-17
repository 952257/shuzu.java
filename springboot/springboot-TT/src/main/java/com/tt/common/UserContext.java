package com.tt.common;

public class UserContext {

    private static final ThreadLocal<String> USER_ID = new ThreadLocal<>();
    private static final ThreadLocal<String> USER_NAME = new ThreadLocal<>();
    private static final ThreadLocal<String> ROLE = new ThreadLocal<>();
    private static final ThreadLocal<String> STORE_ID = new ThreadLocal<>();

    public static void set(String userId, String userName, String role, String storeId) {
        USER_ID.set(userId);
        USER_NAME.set(userName);
        ROLE.set(role);
        STORE_ID.set(storeId);
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

    public static String getStoreId() {
        return STORE_ID.get();
    }

    public static boolean isAdmin() {
        return "ADMIN".equals(ROLE.get());
    }

    public static void requireAdmin() {
        if (!isAdmin()) {
            throw new ServiceException(ServiceExceptionEnum.FORBIDDEN);
        }
    }

    public static void clear() {
        USER_ID.remove();
        USER_NAME.remove();
        ROLE.remove();
        STORE_ID.remove();
    }
}
