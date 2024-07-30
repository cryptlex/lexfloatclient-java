package com.cryptlex.lexfloatclient;

public class HostLicenseMeterAttribute {

    /**
     * The name of the meter attribute.
     */
    public String name;

    /**
     * The allowed uses of the meter attribute. A value of -1 indicates unlimited allowed uses.
     */
    public long allowedUses;

    /**
     * The total uses of the meter attribute.
     */
    public long totalUses;

    /**
     * The gross uses of the meter attribute. 
     */
    public long grossUses;

    public HostLicenseMeterAttribute(String name, long allowedUses, long totalUses, long grossUses) {
        this.name = name;
        this.allowedUses = allowedUses;
        this.totalUses = totalUses;
        this.grossUses = grossUses;
    }
}
