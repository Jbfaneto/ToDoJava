package com.joaoneto.todosimple.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;


@Entity
@Table(name = "task")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Data
public class Task implements Serializable{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", unique = true)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false, updatable = false)
    private User user;

    @Column(name="description", length = 255, nullable = false)
    @NotBlank
    @Size(min = 4, max = 255)
    private String description;

}
