package com.dhanush.SoloSync.Dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileDto
{
    private Long id;
    private String fullname;
    private String email;
    private String password;
    private String profileImageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
