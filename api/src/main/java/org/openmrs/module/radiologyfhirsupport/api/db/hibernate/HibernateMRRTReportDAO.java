package org.openmrs.module.radiologyfhirsupport.api.db.hibernate;

import org.openmrs.Encounter;
import org.openmrs.module.radiologyfhirsupport.MRRTReport;
import org.openmrs.module.radiologyfhirsupport.api.db.MRRTReportDAO;

import java.util.List;

/**
 * Created by devmaany on 27/10/16.
 */
public class HibernateMRRTReportDAO implements MRRTReportDAO {
    @Override
    public MRRTReport getById(int id) {
        return null;
    }

    @Override
    public MRRTReport getByUUID(String uuid) {
        return null;
    }

    @Override
    public MRRTReport getByEncounterUUID(String encounterUUID) {
        return null;
    }

    @Override
    public MRRTReport getByEncounter(Encounter encounter) {
        return null;
    }

    @Override
    public List<MRRTReport> getAll() {
        return null;
    }

    @Override
    public int saveOrUpdate(MRRTReport report) {
        return 0;
    }

    @Override
    public MRRTReport deleteById(Integer id) {
        return null;
    }

    @Override
    public MRRTReport delete(MRRTReport report) {
        return null;
    }
}
