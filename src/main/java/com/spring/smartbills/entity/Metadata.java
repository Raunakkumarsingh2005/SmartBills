package com.spring.smartbills.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Metadata {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String uniqueFileName;

    @Column(nullable = false)
    private String title;

    @CreatedDate
    private Date createdOn;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String filePath;

    private LocalDate duedate;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean reminderSent;

    private String venderName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner")
    @JsonIgnore
    private User owner;

}

