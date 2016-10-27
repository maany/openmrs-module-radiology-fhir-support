package org.openmrs.module.radiologyfhirsupport.api;

import org.openmrs.module.radiologyfhirsupport.MRRTReport;

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
     * @param encounterUUID
     * @return
     */
    MRRTReport getByEncounterUUID(String encounterUUID);
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
