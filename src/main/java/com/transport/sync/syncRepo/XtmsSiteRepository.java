package com.transport.sync.syncRepo;

import com.transport.sync.syncModel.XtmsSite;
import org.springframework.data.repository.CrudRepository;

public interface XtmsSiteRepository extends CrudRepository<XtmsSite, String> {
//    List<XtmsSite> findByOrderBySiteNameAsc();
//    List<XtmsSite> findByFcysho(String fcysho);
//    List<XtmsSite> findByCry(String cry);
    XtmsSite findBySiteId(String siteId);
//    @Query("select s from XtmsSite s where s.siteId IN (:siteIds)")
//    List<XtmsSite> findBySiteIds(@Param("siteIds") List<String> siteIds);
}
