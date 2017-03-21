package org.openmrs.module.radiologyfhirsupport.api.db;

import org.openmrs.Encounter;
import org.openmrs.module.radiologyfhirsupport.MRRTReport;
import org.openmrs.module.radiologyfhirsupport.MRRTTemplate;

import java.util.List;

/**
 * Created by devmaany on 27/10/16.
 */
public interface MRRTReportDAO {

    /**
     *
     * @param id
     * @return
     */
    public MRRTReport getById(int id);

    /**
     *
     * @param uuid
     * @return
     */
    public MRRTReport getByUUID(String uuid);

    /**
     *
     * @param encounterUUID
     * @return
     */
    MRRTReport getByEncounterUUID(String encounterUUID);

    /**
     *
     * @param patientId
     * @return
     */
    List<MRRTReport> getByPatientId(Integer patientId);

    /**
     *
     * @param encounter
     * @return
     */
    MRRTReport getByEncounter(Encounter encounter);

    /**
     *
     * @param
     */
    public List<MRRTReport> getAll();

    /**
     *
     * @param templateId
     * @return
     */
    List<MRRTReport> getByTemplate(MRRTTemplate template);
    /**
     *
     * @param report
     * @return
     */
    public int saveOrUpdate(MRRTReport report);

    /**
     *
     * @param id
     */
    public MRRTReport deleteById(Integer id);

    /**
     *
     * @param report
     * @return
     */
    public MRRTReport delete(MRRTReport report);
}
