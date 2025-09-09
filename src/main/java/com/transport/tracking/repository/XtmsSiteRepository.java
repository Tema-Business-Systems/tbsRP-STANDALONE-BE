package com.transport.tracking.repository;

import com.transport.tracking.model.XtmsSite;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface XtmsSiteRepository extends CrudRepository<XtmsSite, String> {
//    List<XtmsSite> findByOrderBySiteNameAsc();
//    List<XtmsSite> findByFcysho(String fcysho);
//    List<XtmsSite> findByCry(String cry);
    XtmsSite findBySiteId(String siteId);
//    @Query("select s from XtmsSite s where s.siteId IN (:siteIds)")
//    List<XtmsSite> findBySiteIds(@Param("siteIds") List<String> siteIds);
}
