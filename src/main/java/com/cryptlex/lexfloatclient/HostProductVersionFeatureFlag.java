package com.cryptlex.lexfloatclient;

public class HostProductVersionFeatureFlag {
    public String name;

    public Boolean enabled;

    public String data;

    public HostProductVersionFeatureFlag(String name, Boolean enabled, String data) {
        this.name = name;
        this.enabled = enabled;
        this.data = data;
    }
}