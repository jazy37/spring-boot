package com.jazy.customer;


import jakarta.persistence.*;

@Table(name = "roles")
@Entity
public class Role {
    @Id
    @SequenceGenerator(
            name = "role_id_seq",
            sequenceName = "roles_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "role_id_seq"
    )
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ERole name;

    public Role() {

    }

    public Role(ERole name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ERole getName() {
        return name;
    }

    public void setName(ERole name) {
        this.name = name;
    }


}
