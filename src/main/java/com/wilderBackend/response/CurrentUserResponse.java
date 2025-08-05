package com.wilderBackend.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class CurrentUserResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String fullName;
    private Long organizationId;
    private String logoUrl;
    private String organizationName;
    private Long projectId;
    private String phoneNumber;
    private String role;
    private List<Permission> permissions;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Permission {
        private String accessId;

        private String accessSlug;

        private String permissionId;

        private String permissionSlug;
    }

    // Instance initializer block to dynamically set fullName
    {
        if (firstName != null || lastName != null) {
            fullName = (firstName != null ? firstName : "") + (lastName != null ? " " + lastName : "");
        }

    }
}
