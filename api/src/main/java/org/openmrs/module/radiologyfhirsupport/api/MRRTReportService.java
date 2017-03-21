package org.openmrs.module.radiologyfhirsupport.api;

import org.openmrs.Encounter;
import org.openmrs.module.radiologyfhirsupport.MRRTReport;
import org.openmrs.module.radiologyfhirsupport.MRRTTemplate;

import java.util.List;

/**
 * Created by devmaany on 27/10/16.
 */
public interface MRRTReportService {
    /**
     *
     * @param id
     * @return
     */
    MRRTReport getById(int id);

    /**
     *
     * @return
     */
    /**
     *
     * @return
     */
    List<MRRTReport> getAll();

    /**
     *
     * @param templateId
     * @return
     */
    List<MRRTReport> getByTemplate(MRRTTemplate template);
    /**
     *
     * @param encounterUUID
     * @return
     */
    MRRTReport getByEncounterUUID(String encounterUUID);

    /**
     *
     * @param encounter
     * @return
     */
    MRRTReport getByEncounter(Encounter encounter);

    /**
     *
     * @param patientId
     * @return
     */
    List<MRRTReport> getByPatientId(Integer patientId);
    /**
     *
     * @param report
     * @return
     */
    int saveOrUpdate(MRRTReport report);

    /**
     *
     * @param report
     * @return uuid of the {@link org.openmrs.Encounter} created
     */
    String create(MRRTReport report);
    /**
     *
     * @param id
     * @return
     */
    /**
     *
     * @param id
     * @return
     */
    MRRTReport delete(int id);

    /**
     *
     * @param report
     * @return
     */
    MRRTReport delete(MRRTReport report);
}
