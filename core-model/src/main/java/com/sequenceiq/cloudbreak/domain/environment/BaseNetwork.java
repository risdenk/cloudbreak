package com.sequenceiq.cloudbreak.domain.environment;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import com.fasterxml.jackson.core.type.TypeReference;
import com.sequenceiq.cloudbreak.common.json.Json;
import com.sequenceiq.cloudbreak.common.json.JsonToString;
import com.sequenceiq.cloudbreak.common.json.JsonUtil;
import com.sequenceiq.cloudbreak.domain.ArchivableResource;
import com.sequenceiq.cloudbreak.workspace.model.Workspace;
import com.sequenceiq.cloudbreak.workspace.model.WorkspaceAwareResource;
import com.sequenceiq.cloudbreak.workspace.resource.WorkspaceResource;

@Entity
@Where(clause = "archived = false")
@Table(name = "environment_network")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "network_platform")
public abstract class BaseNetwork implements WorkspaceAwareResource, ArchivableResource {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "environment_network_generator")
    @SequenceGenerator(name = "environment_network_generator", sequenceName = "environment_network_id_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Workspace workspace;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(nullable = false)
    private Environment environment;

    private boolean archived;

    private Long deletionTimestamp = -1L;

    @Convert(converter = JsonToString.class)
    @Column(columnDefinition = "TEXT")
    private Json subnetIds;

    @Convert(converter = JsonToString.class)
    @Column(columnDefinition = "TEXT")
    private Json subnetCidrs;

    private String networkCidr;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RegistrationType registrationType;

    public BaseNetwork() {
        subnetIds = new Json(new HashSet<String>());
        subnetCidrs = new Json(new HashSet<String>());
    }

    @Override
    public void setDeletionTimestamp(Long timestampMillisecs) {
        deletionTimestamp = timestampMillisecs;
    }

    @Override
    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    @Override
    public void unsetRelationsToEntitiesToBeDeleted() {
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public Workspace getWorkspace() {
        return workspace;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setWorkspace(Workspace workspace) {
        this.workspace = workspace;
    }

    @Override
    public WorkspaceResource getResource() {
        return WorkspaceResource.ENVIRONMENT;
    }

    public Json getSubnetIds() {
        return subnetIds;
    }

    public void setSubnetIds(Set<String> subnetIds) {
        this.subnetIds = new Json(subnetIds);
    }

    public String getNetworkCidr() {
        return networkCidr;
    }

    public void setNetworkCidr(String networkCidr) {
        this.networkCidr = networkCidr;
    }

    public RegistrationType getRegistrationType() {
        return registrationType;
    }

    public void setRegistrationType(RegistrationType registrationType) {
        this.registrationType = registrationType;
    }

    public Set<String> getSubnetIdsSet() {
        return subnetIds.getValue() != null ? JsonUtil.jsonToType(subnetIds.getValue(), new TypeReference<>() {
        }) : Collections.emptySet();
    }

    public Json getSubnetCidrs() {
        return subnetCidrs;
    }

    public void setSubnetCidrs(Set<String> subnetCidrs) {
        this.subnetCidrs = new Json(subnetCidrs);
    }

    public Set<String> getSubnetCidrsSet() {
        return subnetCidrs.getValue() != null ? JsonUtil.jsonToType(subnetCidrs.getValue(), new TypeReference<>() {
        }) : Collections.emptySet();
    }

    public boolean isArchived() {
        return archived;
    }

    public Long getDeletionTimestamp() {
        return deletionTimestamp;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
