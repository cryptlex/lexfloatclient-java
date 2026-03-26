package com.cryptlex.lexfloatclient;

public class HostFeatureEntitlement {
    /**
     * The name of the feature.
     */
    public String featureName;

    /**
     * The display name of the feature.
     */
    public String featureDisplayName;

    /**
     * Effective value of the feature. Contains the overridden value if set at the license level;
     * otherwise, the entitlement set value.
     */
    public String value;

    /**
     * Default value of the feature defined in the entitlement set; empty for features not inherited
     * from an entitlement set.
     */
    public String baseValue;

    /**
     * The timestamp at which the license feature entitlement will expire.
     */
    public long expiresAt;
}
