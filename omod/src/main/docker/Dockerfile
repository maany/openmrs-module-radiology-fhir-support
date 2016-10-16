FROM teleivo/openmrs-platform:2.0.0-1

# Get radiology and radiology-fhir-support modules dependencies
RUN curl -L \
    "http://mavenrepo.openmrs.org/nexus/service/local/repositories/modules/content/org/openmrs/module/legacyui-omod/1.2/legacyui-omod-1.2.jar" \
    -o "${OPENMRS_MODULES}/legacyui-1.2.omod"
RUN curl -L \
    "http://mavenrepo.openmrs.org/nexus/service/local/repositories/modules/content/org/openmrs/module/webservices.rest-omod/2.16/webservices.rest-omod-2.16.jar" \
    -o "${OPENMRS_MODULES}/webservices.rest-2.16.omod"
RUN curl -L \
    "http://mavenrepo.openmrs.org/nexus/service/local/repositories/modules/content/org/openmrs/module/fhir-omod/0.91/fhir-omod-0.91.jar" \
    -o "${OPENMRS_MODULES}/fhir-0.91.omod"

COPY maven/*.omod ${OPENMRS_MODULES}/
