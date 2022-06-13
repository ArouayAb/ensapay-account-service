package ensa.ebanking.accountservice.Enums;

public enum ValidRecharge {
    RECHARGE_10("10"),
    RECHARGE_20("20"),
    RECHARGE_25("25"),
    RECHARGE_30("30"),
    RECHARGE_50("50"),
    RECHARGE_100("100"),
    RECHARGE_200("200"),
    RECHARGE_300("300");

    public final String label;

    ValidRecharge(String label) {
        this.label = label;
    }

    static public boolean isMember(String aName) {
        ValidRecharge[] aFruits = ValidRecharge.values();
        for (ValidRecharge vRecharge : aFruits)
            if (vRecharge.label.equals(aName))
                return true;
        return false;
    }
}
