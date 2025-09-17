package com.transport.sync.syncRepo;

import com.transport.sync.syncModel.DocumentCfg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocumentCfgRepository extends JpaRepository<DocumentCfg, Integer> {
    @Query("SELECT d FROM DocumentCfg d WHERE d.xDocTyp = :xDocTyp AND d.xDocument = :xDocument")
    Optional<DocumentCfg> findByDocTypAndDocument(@Param("xDocTyp") String xDocTyp,
                                                  @Param("xDocument") Short xDocument);
}
