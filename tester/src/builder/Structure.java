package builder;

import java.util.Objects;

public class Structure {

    // Arrays for storing the different values
    private final String[] typeArray = {"Hanger", "Building", "Fortress"};
    private final String[] techArray = {"Inner Sphere", "Clan"};
    private final String[] engineArray = {"Steam", "Internal Combustion (ICE)", "Fuel Cell", "Fission", "Fusion"};
    private final String[][] motiveArray = {
            {"Ground", "Air", "Water - Surface", "Water - Submersible"},
            {"Ground", "Water - Surface", "Water - Submersible"}
    };
    private final String[][] classArray = {
            {"Light", "Medium", "Heavy", "Hardened"},
            {"Medium", "Heavy", "Hardened"}
    };

    private final double[] strTypeMults = {0.3, 0.5, 1};
    private final double[][][] powerSystemMults = {
            {   // Inner Sphere
                    {6, 6, 6, 7},       // Steam, only submersible is different
                    {3, 3, 3, 3.2},     // Internal Combustion, only submersible is different
                    {4, 4.4, 4, 5},     // Fuel Cell
                    {3, 3, 3, 3},       // Fission, all are the same
                    {2, 2, 2, 2.2}      // Fusion, only submersible is different
            },
            {   // Clan
                    {7, 7, 7, 8},       // Steam, only submersible is different
                    {3, 3, 3, 3},       // Internal Combustion, all are the same
                    {4, 4.2, 4, 4.4},   // Fuel Cell
                    {4, 4, 4, 4},       // Fission, all are the same
                    {1.8, 1.8, 1.8, 2}  // Fusion, only submersible is different
            }
    };
    private final double[][] motiveMults = {
            {4, 5, 2, 3.5},     // Inner Sphere
            {3.5, 4, 1.8, 3.5}  // Clan
    };
    private final double[] fuelMults = {
        0.04, 0.02, 0    // Fuel weights, only for Steam, ICE, and Fuel Cell. ICE and Fuel Cell are the same
    };
    private final int[][] crewMults = {
            {2, 3, 2, 3},
            {3, 4, 3, 4},
            {4, 0, 5, 6}
    };


    // Variables for selected values
    // Final value variables
    private String type;
    private String tech;
    private String strClass;
    private String motive;
    private String engine;
    private int numHexes;
    private int height;
    private int conFactor;
    private int hexWeight;
    private int totalWeight;
    private int range;
    private int crew;
    private int officers;
    private int totalCrew;
    private double motiveWeight;
    private double engineWeight;
    private double fuelWeight;
    private double moveSpeed;
    private double remainingWeight;
    private double motivePerHex;
    private double powerPerHex;

    // Construction only variables
    private int cfMin;
    private int cfMax;
    private int levelMax;
    private int hexMax;
    private int motiveIndex;
    private int powerIndex;
    private int[] crewType;
    private int finalCrewMult;
    private double moveMax;
    private double strWeightMult;
    private double[][] techBasePowerMults;
    private double[] enginePowerMults;
    private double[] motiveTechMults;
    private double fuelMult;
    private double finalPowerMult;
    private double finalMotiveMult;
    private String[] availableClasses;
    private String[] availableMotives;


    // Default constructor to establish a structure with basic settings
    public Structure() {
        SetType(typeArray[0]);
        SetTech(techArray[0]);
        SetStrClass(classArray[0][0]);
        SetNumHexes(2);
        SetHeight(1);
        SetCf(cfMin);
        SetEngine(engineArray[0]);
        SetMotive(motiveArray[0][0]);
        SetMoveSpeed(0.0);
        CalculateWeights();
    }


    // Functions for determining attributes not set directly by user.
    // Determine the values for the construction factor, level, and hex maximums
    private void SetCfAndHex() {
        if (strClass == null || type == null) {
            return;
        }
        switch (type) {
            case "Hanger":
                switch (strClass) {
                    case "Light" -> {
                        cfMin = 1;
                        cfMax = 8;
                        hexMax = 10;
                        levelMax = 7;
                    }
                    case "Medium" -> {
                        cfMin = 9;
                        cfMax = 20;
                        hexMax = 14;
                        levelMax = 10;
                    }
                    case "Heavy" -> {
                        cfMin = 21;
                        cfMax = 45;
                        hexMax = 18;
                        levelMax = 13;
                    }
                    case "Hardened" -> {
                        cfMin = 46;
                        cfMax = 75;
                        hexMax = 20;
                        levelMax = 14;
                    }
                }
                break;
            case "Building":
                switch (strClass) {
                    case "Light" -> {
                        cfMin = 1;
                        cfMax = 15;
                        hexMax = 6;
                        levelMax = 5;
                    }
                    case "Medium" -> {
                        cfMin = 16;
                        cfMax = 40;
                        hexMax = 8;
                        levelMax = 8;
                    }
                    case "Heavy" -> {
                        cfMin = 41;
                        cfMax = 90;
                        hexMax = 10;
                        levelMax = 10;
                    }
                }
                break;
            case "Fortress":
                switch (strClass) {
                    case "Medium" -> {
                        cfMin = 16;
                        cfMax = 40;
                        hexMax = 12;
                        levelMax = 15;
                    }
                    case "Heavy" -> {
                        cfMin = 41;
                        cfMax = 90;
                        hexMax = 15;
                        levelMax = 20;
                    }
                    case "Hardened" -> {
                        cfMin = 91;
                        cfMax = 150;
                        hexMax = 20;
                        levelMax = 30;
                    }
                }
                break;
        }
    }

    // Determine max move speed from chosen motive
    private void setMoveMax() {
        switch (motive) {
            case "Ground":
                moveMax = 2;
                break;
            case "Air", "Water - Submersible":
                moveMax = 4;
                break;
            case "Water - Surface":
                moveMax = 3;
                break;
        }
    }

    // Calculate final weights of motive, power, fuel, the remaining capacity per hex, and crew requirements
    // Formula for system weights:
    // Power system: (hexes * levels) * MP * power system multiplier
    // Motive system: (hexes * levels) * motive type multiplier * structure type multiplier
    // Fuel: range in 100km * fuel multiplier * power system weight
    public void CalculateWeights() {
        // Calculate weight capacity
        if (Objects.equals(type, "Hanger")) {
            hexWeight = (int) ((Math.ceil((double) height / 4)) * 300);
        } else {
            hexWeight = conFactor * height;
        }
        totalWeight = hexWeight * numHexes;

        // Calculate engine weight
        engineWeight = (numHexes * height) * moveSpeed * finalPowerMult;
        engineWeight = RoundUpToHalf(engineWeight, 1);
        powerPerHex = RoundUpToHalf(engineWeight, numHexes);

        // Calculate motive weight
        motiveWeight = (numHexes * height) * finalMotiveMult * strWeightMult;
        motiveWeight = RoundUpToHalf(motiveWeight, 1);
        motivePerHex = RoundUpToHalf(motiveWeight, numHexes);

        // Calculate minimum crew
        crew = finalCrewMult * numHexes;
        officers = (int) Math.ceil((double) crew / 10);
        totalCrew = crew + officers;

        // Calculate fuel weight
        fuelWeight = range * fuelMult * engineWeight;

        // Calculate remaining tonnage space after distributing motive and power system weights
        remainingWeight = hexWeight - RoundUpToHalf(engineWeight, numHexes) - RoundUpToHalf(motiveWeight, numHexes);
    }

    private double RoundUpToHalf(double num1, double num2) {
        double roundedTotal = num1 / num2;
        if (roundedTotal % 0.5 != 0) {
            double offset = 0.5 - (roundedTotal % 0.5);
            roundedTotal += offset;
        }
        return roundedTotal;
    };

    private void SetTechMults() {
        if (motive == null || engine == null) {
            return;
        }
        enginePowerMults = techBasePowerMults[powerIndex];
        finalPowerMult = enginePowerMults[motiveIndex];
        finalMotiveMult = motiveTechMults[motiveIndex];
    }


    // Setters
    public void SetType(String type) {
        for (int i = 0; i < typeArray.length; i++) {
            if (Objects.equals(typeArray[i], type)) {
                this.type = typeArray[i];
                strWeightMult = strTypeMults[i];
                crewType = crewMults[i];
                if (i == 2) {
                    availableClasses = classArray[1];
                    availableMotives = motiveArray[1];
                } else {
                    availableClasses = classArray[0];
                    availableMotives = motiveArray[0];
                }
                SetCfAndHex();
            }
        }
    }

    public void SetTech(String tech) {
        for (int i = 0; i < techArray.length; i++) {
            if (Objects.equals(techArray[i], tech)) {
                this.tech = techArray[i];
                techBasePowerMults = powerSystemMults[i];
                motiveTechMults = motiveMults[i];
                SetTechMults();
            }
        }
    }

    public void SetStrClass(String iClass) {
        for (int i = 0; i < availableClasses.length; i++) {
            if (Objects.equals(availableClasses[i], iClass)) {
                strClass = availableClasses[i];
                SetCfAndHex();
            }
        }
    }

    public void SetMotive(String motive) {
        for (int i = 0; i < availableMotives.length; i++) {
            if (Objects.equals(availableMotives[i], motive)) {
                this.motive = availableMotives[i];
                setMoveMax();
                finalMotiveMult = motiveTechMults[i];
                motiveIndex = i;
                finalCrewMult = crewType[i];
            }
        }
    }

    public void SetEngine(String engine) {
        for (int i = 0; i < engineArray.length; i++) {
            if (Objects.equals(engineArray[i], engine)) {
                this.engine = engine;
                if (i == 0) {
                    fuelMult = fuelMults[0];
                } else if (i == 1 || i == 2) {
                    fuelMult = fuelMults[1];
                } else {
                    fuelMult = fuelMults[2];
                }
                enginePowerMults = techBasePowerMults[i];
                powerIndex = i;
                finalPowerMult = enginePowerMults[motiveIndex];
            }
        }
    }

    public void SetMoveSpeed(double speed) {
        moveSpeed = speed;
    }

    public void SetNumHexes(int hexes) {
        numHexes = hexes;
    }

    public void SetHeight(int height) {
        this.height = height;
    }

    public void SetCf(int cf) {
        conFactor = cf;
    }

    public void SetRange(int range) {
        this.range = range / 100;
    }


    // Getters
    public String GetClass() {
        return strClass;
    }

    public int GetCfMin() {
        return cfMin;
    }

    public int GetCfMax() {
        return cfMax;
    }

    public int GetHexMax() {
        return hexMax;
    }

    public int GetLevelMax() {
        return levelMax;
    }

    public double GetMoveMax() {
        return moveMax;
    }

    public int GetNumHexes() {
        return numHexes;
    }

    public int GetHeight() {
        return height;
    }

    public int GetCf() {
        return conFactor;
    }

    public int GetHexWeight() {
        return hexWeight;
    }

    public int GetTotalWeight() {
        return totalWeight;
    }

    public String[] GetAvailableClasses() {
        return availableClasses;
    }

    public int GetRange() {
        return range * 100;
    }

    public double GetSpeed() {
        return moveSpeed;
    }

    public double GetPowerWeight() {
        return engineWeight;
    }

    public double GetMotiveWeight() {
        return motiveWeight;
    }

    public double GetRemainingWeight() {
        return remainingWeight;
    }

    public int GetOfficers() {
        return officers;
    }

    public int GetCrew() {
        return crew;
    }

    public double GetFuelWeight() {
        return fuelWeight;
    }

    public String[] GetAvailableMotives() {
        return availableMotives;
    }

    public String GetMotive() {
        return motive;
    }

    public double GetPowerPerHex() {
        return powerPerHex;
    }

    public double GetMotivePerHex() {
        return motivePerHex;
    }
}
