package com.infoworks.lab.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.it.soul.lab.sql.entity.Entity;
import org.springframework.data.annotation.*;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.time.LocalDateTime;
import java.util.Map;

public class Auditable<ID, VERSION> extends Entity implements Externalizable {

    @CreatedDate //@Field(name = "created_date", type = FieldType.Date)
    LocalDateTime createdDate;

    @LastModifiedDate //@Field(name = "last_modified_date", type = FieldType.Date)
    LocalDateTime lastModifiedDate;

    @CreatedBy //@Field(name = "created_by", type = FieldType.Object)
    Username createdBy;

    @LastModifiedBy //@Field(name = "last_modified_by", type = FieldType.Object)
    Username lastModifiedBy;

    @Version @JsonIgnore
    private VERSION version;

    public VERSION getVersion() {
        return version;
    }

    public void setVersion(VERSION version) {
        this.version = version;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(marshallingToMap(true));
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        Map<String, Object> data = (Map<String, Object>) in.readObject();
        unmarshallingFromMap(data, true);
    }
}
