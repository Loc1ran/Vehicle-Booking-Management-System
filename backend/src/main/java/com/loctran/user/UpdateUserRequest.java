package com.loctran.user;

public record UpdateUserRequest( String name ) {
    public boolean hasChanges(User user) {
        return name() != null && !name().equals(user.getName());
    }
}
