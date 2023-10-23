package com.joaoneto.todosimple.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.joaoneto.todosimple.models.enums.ProfileEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table (name = "user")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @Column(name = "username", length = 100, unique = true, nullable = false)
    @NotBlank
    @Size(min = 4, max = 50)
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "password", length = 255, nullable = false)
    @NotBlank
    @Size(min = 1, max = 255)
    private String password;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Task> tasks = new ArrayList<>();

    @Column(name = "profile", nullable = false)
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_profile")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Set<Integer> profiles = new HashSet<>();

    public Set<ProfileEnum> getProfiles() {
        return this.profiles.stream().map(x -> ProfileEnum.toEnum(x)).collect(Collectors.toSet());
    }

    public void addProfile(ProfileEnum profileEnum) {
        this.profiles.add(profileEnum.getCode());
    }

}