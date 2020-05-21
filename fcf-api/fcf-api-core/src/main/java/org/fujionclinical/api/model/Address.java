package org.fujionclinical.api.model;

import org.fujion.common.DateRange;
import org.fujion.common.MiscUtil;

import java.util.List;

public class Address {

    private AddressCategory category;

    private List<String> lines;

    private String city;

    private String district;

    private String state;

    private String postalCode;

    private String country;

    private DateRange period;

    public AddressCategory getCategory() {
        return category;
    }

    public void setCategory(AddressCategory category) {
        this.category = category;
    }

    public boolean hasCategory() {
        return category != null;
    }

    public List<String> getLines() {
        return lines;
    }

    public void setLines(List<String> lines) {
        this.lines = lines;
    }

    public void addLines(String... lines) {
        if (lines != null && lines.length > 0) {
            this.lines = MiscUtil.ensureList(this.lines);
            this.lines.addAll(MiscUtil.toList(lines));
        }
    }

    public boolean hasLines() {
        return lines != null && !lines.isEmpty();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public boolean hasCity() {
        return city != null;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public boolean hasDistrict() {
        return district != null;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public boolean hasState() {
        return state != null;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public boolean hasPostalCode() {
        return postalCode != null;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public boolean hasCountry() {
        return country != null;
    }

    public DateRange getPeriod() {
        return period;
    }

    public void setPeriod(DateRange period) {
        this.period = period;
    }

    public boolean hasPeriod() {
        return period != null;
    }

    public String toString() {
        return null;
    }

    public enum AddressCategory {HOME, WORK, TEMP, OLD, BILLING}

}
