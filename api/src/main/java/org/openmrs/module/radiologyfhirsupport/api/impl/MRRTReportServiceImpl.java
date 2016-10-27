package org.openmrs.module.radiologyfhirsupport.api.impl;

import org.openmrs.module.radiologyfhirsupport.MRRTReport;
import org.openmrs.module.radiologyfhirsupport.api.MRRTReportService;

import java.util.List;

/**
 * Created by devmaany on 27/10/16.
 */
public class MRRTReportServiceImpl implements MRRTReportService{
    @Override
    public MRRTReport getById(int id) {
        return null;
    }

    @Override
    public List<MRRTReport> getAll() {
        return null;
    }

    @Override
    public MRRTReport getByEncounterUUID(String encounterUUID) {
        return null;
    }

    @Override
    public int saveOrUpdate(MRRTReport report) {
        return 0;
    }

    @Override
    public String create(MRRTReport report) {
        return null;
    }

    @Override
    public MRRTReport delete(int id) {
        return null;
    }

    @Override
    public MRRTReport delete(MRRTReport report) {
        return null;
    }
}
