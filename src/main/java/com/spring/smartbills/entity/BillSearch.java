package com.spring.smartbills.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Getter@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "bill")
public class BillSearch {
    @Id
    private String id;

    @Column(nullable = false)
    private String title;

    @CreatedDate
    private Date createdOn;

    @Column(nullable = false)
    private String category;

//    @Field(type = FieldType.Date, pattern = "yyyy-MM-dd")
//    private LocalDate duedate;

    private String venderName;

}
