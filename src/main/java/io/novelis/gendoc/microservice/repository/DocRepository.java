package io.novelis.gendoc.microservice.repository;

import io.novelis.gendoc.microservice.domain.Doc;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Doc entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocRepository extends JpaRepository<Doc, Long> {

}
