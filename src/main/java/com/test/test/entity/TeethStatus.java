package com.test.test.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Component
@Table(name = "teethstatus")
public class TeethStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Teeth teeth;
    private int numberOfTeeths;
    @ManyToOne(fetch = FetchType.EAGER,cascade ={CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    @JoinColumn(name = "entityvisit_id")
    private EntityVisit entityVisit;
}
