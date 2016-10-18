FROM maany/openmrs-platform:1.11.4

# Get radiology-fhir-support modules dependencies

RUN curl -L \
    "http://mavenrepo.openmrs.org/nexus/service/local/repositories/modules/content/org/openmrs/module/fhir-omod/0.91/fhir-omod-0.91.jar" \
    -o "${OPENMRS_MODULES}/fhir-0.91.omod"

COPY maven/*.omod ${OPENMRS_MODULES}/


