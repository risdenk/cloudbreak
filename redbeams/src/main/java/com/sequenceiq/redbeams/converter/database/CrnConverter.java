package com.sequenceiq.redbeams.converter.database;

import javax.inject.Inject;
import javax.persistence.AttributeConverter;

import org.springframework.stereotype.Component;

import com.sequenceiq.cloudbreak.auth.altus.Crn;
import com.sequenceiq.redbeams.service.crn.CrnService;

@Component
public class CrnConverter implements AttributeConverter<Crn, String> {

    @Inject
    private static CrnService crnService;

    @Inject
    public void init(CrnService crnServiceComponent) {
        crnService = crnServiceComponent;
    }

    @Override
    public String convertToDatabaseColumn(Crn attribute) {
        return attribute.getResource();
    }

    @Override
    public Crn convertToEntityAttribute(String dbData) {
        return crnService.createDatabaseCrnFrom(dbData);
    }
}
