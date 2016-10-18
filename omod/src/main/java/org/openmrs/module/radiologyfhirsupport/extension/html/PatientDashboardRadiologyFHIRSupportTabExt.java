package org.openmrs.module.radiologyfhirsupport.extension.html;

import org.openmrs.module.radiologyfhirsupport.RadiologyFHIRPriviliges;
import org.openmrs.module.web.extension.PatientDashboardTabExt;

/**
 * Created by devmaany on 18/10/16.
 */
public class PatientDashboardRadiologyFHIRSupportTabExt extends PatientDashboardTabExt {
    @Override
    public String getTabName() {
        return "radiologyfhirsupport.patientDashboardForm.tabs.radfhir";
    }

    @Override
    public String getTabId() {
        return "patientDashboardRadiologyFHIRSupportTab";
    }

    @Override
    public String getRequiredPrivilege() {
        return RadiologyFHIRPriviliges.VIEW_PATIENT_DASHBOARD_RADIOLOGY_FHIR_SUPPORT_TAB;
    }

    @Override
    public String getPortletUrl() {
        return "patientDashboardRadiologyFHIRSupportTab.portlet";
    }
}
