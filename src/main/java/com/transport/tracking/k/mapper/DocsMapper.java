package com.transport.tracking.k.mapper;

import com.transport.tracking.model.Docs;
import com.transport.tracking.response.DocsVO;

import java.util.ArrayList;
import java.util.List;

public class DocsMapper {

    public static DocsVO toVO(Docs entity) {
        if (entity == null) return null;

        DocsVO vo = new DocsVO();
        vo.setDocnum(entity.getDocnum());
        vo.setTrailer(entity.getTrailer());
        vo.setAllDrivers(entity.getAllDrivers());
        vo.setAllVehClass(entity.getAllVehClass());
        vo.setDriverList(entity.getDriverList());
        vo.setVehClassList(entity.getVehClassList());
        vo.setPriorityOrder(entity.getPriorityOrder());
        vo.setPriority(entity.getPriority());
        vo.setFromTime(entity.getFromTime());
        vo.setToTime(entity.getToTime());
        vo.setAvailDays(entity.getAvailDays());
        vo.setCarrier(entity.getCarrier());
        vo.setMiscpickflg(entity.getMiscpickflg());
        vo.setRouteCode(entity.getRouteCode());
        vo.setVehClassAssoc(entity.getVehClassAssoc());
        vo.setRouteBgColor(entity.getRouteBgColor());
        vo.setRouteCodeDesc(entity.getRouteCodeDesc());
        vo.setAdrescode(entity.getAdrescode());
        vo.setAdresname(entity.getAdresname());
        vo.setCarrierColor(entity.getCarrierColor());
        vo.setSite(entity.getSite());
        vo.setRouteColor(entity.getRouteColor());
        vo.setRouteTag(entity.getRouteTag());
        vo.setRouteTagFRA(entity.getRouteTagFRA());
        vo.setDocdate(entity.getDocdate() != null ? entity.getDocdate().toString() : null);
        vo.setDlvystatus(entity.getDlvystatus());
        vo.setDoctype(entity.getDoctype());
        vo.setMovtype(entity.getMovtype());
        vo.setDocinst(entity.getDocinst());
        vo.setBpcode(entity.getBpcode());
        vo.setPtheader(entity.getPtheader());
        vo.setPtlink(entity.getPtlink());
        vo.setBpname(entity.getBpname());
        vo.setCpycode(entity.getCpycode());
        vo.setNbpack(entity.getNbpack());
        vo.setNetweight(entity.getNetweight());
        vo.setWeightunit(entity.getWeightunit());
        vo.setVolume(entity.getVolume());
        vo.setVolume_unit(entity.getVolume_unit());
        vo.setDscode(entity.getDscode());
        vo.setDrivercode(entity.getDrivercode());
        vo.setAddlig1(entity.getAddlig1());
        vo.setAddlig2(entity.getAddlig2());
        vo.setAddlig3(entity.getAddlig3());
        vo.setPoscode(entity.getPoscode());
        vo.setCity(entity.getCity());
        vo.setStatecode(entity.getStatecode());
        vo.setCountrycode(entity.getCountrycode());
        vo.setCountryname(entity.getCountryname());
        vo.setLat(parseDouble(entity.getGps_x()));
        vo.setLng(parseDouble(entity.getGps_y()));
        vo.setDepdate(entity.getDepdate());
        vo.setDeptime(entity.getDeptime());
        vo.setArvdate(entity.getArvdate());
        vo.setArvtime(entity.getArvtime());
        vo.setVehicleCode(entity.getVehicleCode());
        vo.setVehicleplate(entity.getVehicleplate());
        vo.setTripno(entity.getTripno());
        vo.setDlvmode(entity.getDlvmode());
        vo.setVrcode(entity.getVrcode());
        vo.setVrseq(entity.getVrseq());
        vo.setSeq(entity.getSeq());
        vo.setSchedtype(entity.getSchedtype());
        vo.setLoadBay(entity.getLoadBay());
        vo.setTailGate(entity.getTailGate());
        vo.setBPServiceTime(entity.getBPServiceTime());
        vo.setServiceTime(entity.getServiceTime());
        vo.setWaitingTime(entity.getWaitingTime());
        vo.setStackHeight(entity.getStackHeight());
        vo.setVehType(entity.getVehType());
        vo.setTimings(entity.getTimings());
        vo.setPacking(entity.getPacking());
        vo.setHeight(entity.getHeight());
        vo.setLoadingOrder(entity.getLoadingOrder());
        vo.setPrelistCode(entity.getPrelistCode());
        vo.setAroutecodeDesc(entity.getAroutecodeDesc());
        vo.setAprodCategDesc(entity.getAprodCategDesc());
        vo.setAvehClassListDesc(entity.getAvehClassListDesc());
        vo.setSkills(entity.getSkills());

        return vo;
    }

    public static List<DocsVO> toVOList(List<Docs> entities) {
        List<DocsVO> list = new ArrayList<>();
        for (Docs d : entities) {
            list.add(toVO(d));
        }
        return list;
    }

    private static double parseDouble(String value) {
        try {
            return value != null ? Double.parseDouble(value) : 0;
        } catch (Exception e) {
            return 0;
        }
    }
}

