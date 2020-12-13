package com.github.jinahya.datagokr.api.b090041.lrsrcldinfoservice.client.message.value;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.time.LocalDate;

@XmlAccessorType(XmlAccessType.FIELD)
@Setter
@Getter
@ToString
@Slf4j
public class Item {

    public LocalDate getLunarDate() {
        return LocalDate.of(Integer.parseInt(lunYear), Integer.parseInt(lunMonth), Integer.parseInt(lunDay));
    }

    public LocalDate getSolarDate() {
        return LocalDate.of(Integer.parseInt(solYear), Integer.parseInt(solMonth), Integer.parseInt(solDay));
    }

    public boolean isLeapMonth() {
        return "윤".equals(lunLeapmonth);
    }

    @XmlElement
    private String lunYear;

    @XmlElement
    private String lunMonth;

    @XmlElement
    private String lunDay;

    @XmlElement
    private String solYear;

    @XmlElement
    private String solMonth;

    @XmlElement
    private String solDay;

    @XmlElement
    private String solLeapyear;

    @XmlElement
    private String lunLeapmonth;

    @XmlElement
    private String solWeek;

    @XmlElement
    private String lunSecha;

    @XmlElement(required = false)
    private String lunWolgeon;

    @XmlElement
    private String lunIljin;

    @XmlElement
    private int lunNday;

    @XmlElement
    private int solJd;
}
