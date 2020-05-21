package org.fujionclinical.api.encounter;

import org.fujion.common.DateRange;
import org.fujionclinical.api.location.ILocation;
import org.fujionclinical.api.model.IDomainObject;
import org.fujionclinical.api.model.IPerson;
import org.fujionclinical.api.model.Identifier;

import java.util.List;

public interface IEncounter extends IDomainObject {

    DateRange getPeriod();

    String getStatus();

    List<IPerson> getParticipants();

    ILocation getLocation();

}
