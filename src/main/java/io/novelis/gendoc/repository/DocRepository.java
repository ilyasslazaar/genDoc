package io.novelis.gendoc.repository;

import io.novelis.gendoc.domain.Doc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Doc entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocRepository extends JpaRepository<Doc, Long> {

}
