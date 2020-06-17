package org.fujionclinical.api.model.impl;

import org.fujionclinical.api.model.core.IPeriod;
import org.fujionclinical.api.model.core.IPostalAddress;

import java.util.ArrayList;
import java.util.List;

public class PostalAddress implements IPostalAddress {

    private final List<String> lines = new ArrayList<>();

    private PostalAddressUse use;

    private PostalAddressType type;

    private String city;

    private String district;

    private String state;

    private String postalCode;

    private String country;

    private IPeriod period;

    @Override
    public PostalAddressUse getUse() {
        return use;
    }

    @Override
    public void setUse(PostalAddressUse use) {
        this.use = use;
    }

    @Override
    public PostalAddressType getType() {
        return type;
    }

    @Override
    public void setType(PostalAddressType type) {
        this.type = type;
    }

    @Override
    public List<String> getLines() {
        return lines;
    }

    @Override
    public String getCity() {
        return city;
    }

    @Override
    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String getDistrict() {
        return district;
    }

    @Override
    public void setDistrict(String district) {
        this.district = district;
    }

    @Override
    public String getState() {
        return state;
    }

    @Override
    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String getPostalCode() {
        return postalCode;
    }

    @Override
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    @Override
    public String getCountry() {
        return country;
    }

    @Override
    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public IPeriod getPeriod() {
        return period;
    }

    @Override
    public void setPeriod(IPeriod period) {
        this.period = period;
    }

    @Override
    public String toString() {
        return asString();
    }

}
