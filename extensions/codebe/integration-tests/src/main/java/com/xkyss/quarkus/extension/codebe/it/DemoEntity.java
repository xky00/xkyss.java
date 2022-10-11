package com.xkyss.quarkus.extension.codebe.it;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class DemoEntity {
    @Id
    public Integer id;

    public String name;
}
