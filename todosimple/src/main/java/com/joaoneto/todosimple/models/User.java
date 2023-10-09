package com.joaoneto.todosimple.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import com.joaoneto.todosimple.services.CreateUser;
import com.joaoneto.todosimple.services.UpdateUser;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

@Entity
@Table (name = "user")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    @Getter
    @Setter
    private Long id;

    @Column(name = "username", length = 50, unique = true, nullable = false)
    @NotNull(groups = UpdateUser.class)
    @NotEmpty(groups = UpdateUser.class)
    @Size(min = 4, max = 50)
    @Getter
    @Setter
    private String username;

    @Column(name = "password", length = 50, nullable = false)
    @NotNull(groups = {UpdateUser.class, CreateUser.class})
    @NotEmpty(groups = {UpdateUser.class, CreateUser.class})
    @Size(min = 6, max = 50)
    @Getter
    @Setter
    private String password;

    //private List<Task> tasks = new ArrayList<>();

    public User() {
    }

    public User(Long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result *= prime + ((id == null) ? 0 : this.id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof User)) return false;
        if (obj == null) return false;


        User other = (User) obj;

        if (this.id == null) {
            if (other.id != null) return false;
        } else if (!this.id.equals(other.id)) return false;
        return Objects.equals(this.id, other.id) && Objects.equals(this.username, other.username) && Objects.equals(this.password, other.password);
    }
}
