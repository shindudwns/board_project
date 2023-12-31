package com.example.board_project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserJoinDto {
    private String loginId;
    private String password;
    private String name;
    private String phoneNumber;
    private String username;
}
