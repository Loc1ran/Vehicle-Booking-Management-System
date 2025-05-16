package com.loctran.User;

public record UpdateUserRequest( String name ) {
    public boolean hasChanges(User user) {
        return name() != null && !name().equals(user.getName());
    }
}
