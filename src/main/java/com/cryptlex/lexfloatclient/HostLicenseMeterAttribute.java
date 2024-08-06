package com.cryptlex.lexfloatclient;
import java.math.BigInteger;

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
    public BigInteger totalUses;

    /**
     * The gross uses of the meter attribute. 
     */
    public BigInteger grossUses;

    public HostLicenseMeterAttribute(String name, long allowedUses, BigInteger totalUses, BigInteger grossUses) {
        this.name = name;
        this.allowedUses = allowedUses;
        this.totalUses = totalUses;
        this.grossUses = grossUses;
    }
}
