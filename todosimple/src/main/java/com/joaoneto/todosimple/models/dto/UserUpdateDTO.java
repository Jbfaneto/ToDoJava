package com.joaoneto.todosimple.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserUpdateDTO {
    private Long id;
    @NotBlank
    @Size(min = 1, max = 255)
    private String password;
}

