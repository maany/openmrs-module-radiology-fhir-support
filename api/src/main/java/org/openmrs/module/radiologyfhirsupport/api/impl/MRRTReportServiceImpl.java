package org.openmrs.module.radiologyfhirsupport.api.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.radiologyfhirsupport.MRRTReport;
import org.openmrs.module.radiologyfhirsupport.api.MRRTReportService;
import org.openmrs.module.radiologyfhirsupport.api.db.MRRTReportDAO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by devmaany on 27/10/16.
 */
public class MRRTReportServiceImpl extends BaseOpenmrsService implements MRRTReportService{

    protected final Log log = LogFactory.getLog(this.getClass());

    private MRRTReportDAO dao;

    /**
     * @param dao the dao to set
     */
    public void setDao(MRRTReportDAO dao) {
        this.dao = dao;
    }

    /**
     * @return the dao
     */
    public MRRTReportDAO getDao() {
        return dao;
    }


    @Override
    @Transactional(readOnly = true)
    public MRRTReport getById(int id) {
        return dao.getById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MRRTReport> getAll() {
        return dao.getAll();
    }

    @Override
    @Transactional(readOnly = true)
    public MRRTReport getByEncounterUUID(String encounterUUID) {
        return dao.getByEncounterUUID(encounterUUID);
    }

    @Override
    @Transactional
    public int saveOrUpdate(MRRTReport report) {
        return dao.saveOrUpdate(report);
    }

    @Override
    @Transactional
    public String create(MRRTReport report) {
        dao.saveOrUpdate(report);
        return report.getEncounter().getUuid();
    }

    @Override
    @Transactional
    public MRRTReport delete(int id) {
        return dao.deleteById(id);
    }

    @Override
    @Transactional
    public MRRTReport delete(MRRTReport report) {
        return dao.delete(report);
    }
}
