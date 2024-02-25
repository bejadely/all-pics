package com.malgn.allpics.domain.tag.repository;

import com.malgn.allpics.domain.tag.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository  extends JpaRepository<Tag, Long> {
    Optional<Tag> findByTagName(String obj);

    void deleteByTagName(String tagName);
}
