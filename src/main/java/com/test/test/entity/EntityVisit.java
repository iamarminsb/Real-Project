package com.test.test.entity;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Component
@Table(name = "visit")
public class EntityVisit extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String daromasrafi;
    private String sabeghe;
    private String elat;
    @Enumerated(EnumType.STRING)
    private Status status;
    private String docText;
    private String secText;
    private Boolean isActive;
    private LocalDateTime scheduledAt;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
    @OneToMany(mappedBy = "visit",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<EntityDefect> entityDefects;
    @OneToMany(mappedBy = "entityVisit",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<TeethStatus> teethStatuses;

    public void add(EntityDefect entityDefect1) throws NullPointerException {
        if (entityDefects==null){
            entityDefects=new ArrayList<>();
        }
        entityDefects.add(entityDefect1);


    }
    public void add(TeethStatus teethStatus1) throws NullPointerException {
        if (teethStatuses==null){
            teethStatuses=new ArrayList<>();
        }
        teethStatuses.add(teethStatus1);


    }
}
