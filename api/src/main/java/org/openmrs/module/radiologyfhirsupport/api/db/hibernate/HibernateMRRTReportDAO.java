package org.openmrs.module.radiologyfhirsupport.api.db.hibernate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.openmrs.Encounter;
import org.openmrs.module.radiologyfhirsupport.MRRTReport;
import org.openmrs.module.radiologyfhirsupport.api.db.MRRTReportDAO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by devmaany on 27/10/16.
 */
public class HibernateMRRTReportDAO implements MRRTReportDAO {
    protected final Log log = LogFactory.getLog(this.getClass());

    private SessionFactory sessionFactory;

    /**
     * @param sessionFactory the sessionFactory to set
     */
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * @return the sessionFactory
     */
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    @Override
    public MRRTReport getById(int id) {
        return (MRRTReport) sessionFactory.getCurrentSession().get(MRRTReport.class, id);
    }
    //TODO implement this method
    @Override
    public MRRTReport getByUUID(String uuid) {
        return null;
    }

    @Override
    public MRRTReport getByEncounterUUID(String encounterUUID) {
        MRRTReport report = null;
        String hql = "FROM org.openmrs.module.radiologyfhirsupport.MRRTReport N WHERE N.encounter.uuid = :encounter_uuid";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("encounter_uuid",encounterUUID);
        List<MRRTReport> list = (List<MRRTReport>)query.list();
        return list.get(0);
    }

    @Override
    public List<MRRTReport> getByPatientId(Integer patientId) {
        List<MRRTReport> allReports = getAll();
        List<MRRTReport> reports = new ArrayList<MRRTReport>();
        for(MRRTReport report: allReports){
            if(report.encounter.getPatient().getId()==patientId) {
                reports.add(report);
            }
        }
        return reports;
    }

    @Override
    public MRRTReport getByEncounter(Encounter encounter) {
        MRRTReport report = null;
        String hql = "FROM org.openmrs.module.radiologyfhirsupport.MRRTReport M WHERE M.encounter = :encounter";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("encounter",encounter);
        List<MRRTReport> list = (List<MRRTReport>)query.list();
        return list.get(0);
    }

    @Override
    public List<MRRTReport> getAll() {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(MRRTReport.class);
        return criteria.list();
    }

    @Override
    public int saveOrUpdate(MRRTReport report) {
        sessionFactory.getCurrentSession().saveOrUpdate(report);
        return report.getId();
    }

    @Override
    public MRRTReport deleteById(Integer id) {
        MRRTReport report = getById(id);
        sessionFactory.getCurrentSession().delete(report);
        return report;
    }

    @Override
    public MRRTReport delete(MRRTReport report) {
        sessionFactory.getCurrentSession().delete(report);
        return report;
    }
}
