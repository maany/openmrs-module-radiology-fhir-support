package org.openmrs.module.radiologyfhirsupport;

import org.openmrs.BaseOpenmrsObject;
import org.openmrs.Encounter;
import org.openmrs.User;

import javax.persistence.*;
import java.sql.Clob;
import java.util.Date;

/**
 * Created by devmaany on 27/10/16.
 */
@Entity(name = "MRRTReport")
@Table(name = "radiology_mrrt_report")
public class MRRTReport extends BaseOpenmrsObject{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @JoinColumn(name = "mrrt_template" )
    @OneToOne()
    private MRRTTemplate mrrtTemplate;

    @JoinColumn(name = "encounter")
    @OneToOne(fetch = FetchType.EAGER)
    public Encounter encounter;

    // TODO this field would be replaced by changesets in future implementations
    @Lob
    @Column(name = "xml")
    private Clob xml;

    /*
	* The fields below are added to support OpenMRS versions <1.11.6 where Hibernate Configuration for this class would fail because of TRUNK-4841
	*/

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "creator")
    protected User creator;

    @Column(name = "date_created", nullable = false)
    private Date dateCreated;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "changed_by")
    private User changedBy;

    @Column(name = "date_changed")
    private Date dateChanged;

    @Column(name = "voided", nullable = false)
    private Boolean voided = Boolean.FALSE;

    @Column(name = "date_voided")
    private Date dateVoided;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "voided_by")
    private User voidedBy;

    @Column(name = "void_reason", length = 255)
    private String voidReason;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public MRRTTemplate getMrrtTemplate() {
        return mrrtTemplate;
    }

    public void setMrrtTemplate(MRRTTemplate mrrtTemplate) {
        this.mrrtTemplate = mrrtTemplate;
    }

    public Encounter getEncounter() {
        return encounter;
    }

    public void setEncounter(Encounter encounter) {
        this.encounter = encounter;
    }

    public Clob getXml() {
        return xml;
    }

    public void setXml(Clob xml) {
        this.xml = xml;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public User getChangedBy() {
        return changedBy;
    }

    public void setChangedBy(User changedBy) {
        this.changedBy = changedBy;
    }

    public Date getDateChanged() {
        return dateChanged;
    }

    public void setDateChanged(Date dateChanged) {
        this.dateChanged = dateChanged;
    }

    public Boolean getVoided() {
        return voided;
    }

    public void setVoided(Boolean voided) {
        this.voided = voided;
    }

    public Date getDateVoided() {
        return dateVoided;
    }

    public void setDateVoided(Date dateVoided) {
        this.dateVoided = dateVoided;
    }

    public User getVoidedBy() {
        return voidedBy;
    }

    public void setVoidedBy(User voidedBy) {
        this.voidedBy = voidedBy;
    }

    public String getVoidReason() {
        return voidReason;
    }

    public void setVoidReason(String voidReason) {
        this.voidReason = voidReason;
    }
}
