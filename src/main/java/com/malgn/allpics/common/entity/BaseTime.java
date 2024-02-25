package com.malgn.allpics.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;


@ToString
@Getter
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public class BaseTime {

    @CreatedDate
    @Column(updatable = false)
    public LocalDateTime createdDate;

    @LastModifiedDate
    public LocalDateTime lastModifiedDate;
    public LocalDateTime deletedDate;
}
