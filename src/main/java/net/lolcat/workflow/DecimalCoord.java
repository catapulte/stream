package net.lolcat.workflow;

public class DecimalCoord {

    private double decLat;

    private double decLng;

    public DecimalCoord(String dmLat, String dmLng) {
        this(getDegreeCoord(dmLat), getDegreeCoord(dmLng));
    }

    public DecimalCoord(double dmLat, double dmLng) {
        decLat = getDecimalCoord(dmLat);
        decLng = getDecimalCoord(dmLng);
    }

    private static double getDecimalCoord(double coord) {
        int degree = (int) coord / 100;
        return degree + (coord - degree * 100) / 60;
    }

    private static double getDegreeCoord(String dmCoord) {
        if (dmCoord.endsWith("S") || dmCoord.endsWith("W")) {
            return 0 - Float.valueOf(dmCoord.substring(0, dmCoord.length() - 1));
        }
        return Float.valueOf(dmCoord.substring(0, dmCoord.length() - 1));

    }

    public double[] getDecimalCoord() {
        return new double[]{decLat, decLng};
    }

}