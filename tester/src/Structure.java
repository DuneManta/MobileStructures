import java.util.Objects;

public class Structure {

    // Arrays for storing the different values
    private final String[] typeArray = {"hanger", "building", "fortress"};
    private final String[] techArray = {"Inner Sphere", "Clan"};
    private final String[] classArray = {"light", "medium", "heavy", "hardened"};
    private final String[] motiveArray = {"ground", "air", "surface", "submersible"};
    private final String[] engineArray = {"steam", "ICE", "fuelCell", "fission", "fusion"};

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

    // Construction only variables
    private int cfMin;
    private int cfMax;
    private int levelMax;
    private int hexMax;
    private int motiveIndex;
    private int[] crewType;
    private int finalCrewMult;
    private double moveMax;
    private double strWeightMult;
    private double[][] techBasePowerMults;
    private double[] motiveTechMults;
    private double fuelMult;
    private double finalPowerMult;
    private double finalMotiveMult;


    // Empty constructor to establish a structure
    public Structure() {}


    // Functions for determining attributes not set directly by user.
    // Determine the values for the construction factor, level, and hex maximums
    public void SetCfAndHex() {
        switch (type) {
            case "hanger":
                switch (strClass) {
                    case "light" -> {
                        cfMin = 1;
                        cfMax = 8;
                        hexMax = 10;
                        levelMax = 7;
                    }
                    case "medium" -> {
                        cfMin = 9;
                        cfMax = 20;
                        hexMax = 14;
                        levelMax = 10;
                    }
                    case "heavy" -> {
                        cfMin = 21;
                        cfMax = 45;
                        hexMax = 18;
                        levelMax = 13;
                    }
                    case "hardened" -> {
                        cfMin = 46;
                        cfMax = 75;
                        hexMax = 20;
                        levelMax = 14;
                    }
                }
                break;
            case "building":
                switch (strClass) {
                    case "light" -> {
                        cfMin = 1;
                        cfMax = 15;
                        hexMax = 6;
                        levelMax = 5;
                    }
                    case "medium" -> {
                        cfMin = 16;
                        cfMax = 40;
                        hexMax = 8;
                        levelMax = 8;
                    }
                    case "heavy" -> {
                        cfMin = 41;
                        cfMax = 90;
                        hexMax = 10;
                        levelMax = 10;
                    }
                }
                break;
            case "fortress":
                switch (strClass) {
                    case "medium" -> {
                        cfMin = 16;
                        cfMax = 40;
                        hexMax = 12;
                        levelMax = 15;
                    }
                    case "heavy" -> {
                        cfMin = 41;
                        cfMax = 90;
                        hexMax = 15;
                        levelMax = 20;
                    }
                    case "hardened" -> {
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
            case "ground":
                moveMax = 2;
                break;
            case "air", "submersible":
                moveMax = 4;
                break;
            case "surface":
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
        if (Objects.equals(type, "hanger")) {
            hexWeight = (int) ((Math.ceil((double) height / 4)) * 300);
        } else {
            hexWeight = conFactor * height;
        }
        totalWeight = hexWeight * numHexes;

        // Calculate engine weight
        engineWeight = (numHexes * height) * moveSpeed * finalPowerMult;

        // Calculate motive weight
        motiveWeight = (numHexes * height) * finalMotiveMult * strWeightMult;

        // Get input for range and calculate fuel weight if needed
        if (fuelMult != 0) {
            System.out.print("Please enter desired maximum range: (in hundreds of kilometers) ");
            range = Integer.parseInt(System.console().readLine());
            fuelWeight = range * fuelMult * engineWeight;
        }

        // Calculate minimum crew
        crew = finalCrewMult * numHexes;
        officers = (int) Math.ceil((double) crew / 10);
        totalCrew = crew + officers;

        // Calculate remaining tonnage space after distributing motive and power system weights
        remainingWeight = hexWeight - RoundUpToHalf(engineWeight, numHexes) - RoundUpToHalf(motiveWeight, numHexes);

        // Call method to output values to user
        OutputValues();
    }

    private double RoundUpToHalf(double num1, double num2) {
        double roundedTotal = num1 / num2;
        if (roundedTotal % 0.5 != 0) {
            double offset = 0.5 - (roundedTotal % 0.5);
            roundedTotal += offset;
        }
        return roundedTotal;
    };

    // Output values of structure to user
    private void OutputValues() {
        System.out.println("The maximum weight per hex is " +
                String.format("%,d", hexWeight) + " tons.");
        System.out.println("The maximum weight of the entire structure is " +
                String.format("%,d", totalWeight) + " tons.");
        System.out.println("Total weight of power system is " +
                String.format("%1$,.2f", engineWeight) + " tons.");
        System.out.println("Total weight of motive system is " +
                String.format("%1$,.2f", motiveWeight) + " tons.");
        System.out.println("Remaining weight after distributing power and motive systems is " +
                String.format("%1$,.2f", remainingWeight) + " tons.");
        if (fuelWeight != 0) {
            System.out.println("Total weight of fuel is " +
                    String.format("%1$,.2f", fuelWeight) + " tons.");
        }
        System.out.print("Crew requirements:\n" +
                totalCrew + " total crew.\n" +
                crew + " base crew members with " + officers +" officers.");
    }


    // Setters, handle invalid inputs and set to defaults
    public void SetType(int type) {
        int temp = type - 1;
        if (temp > 2 || temp < 0) {
            System.out.println("Invalid input, using default: Building(2)");
            temp = 1;
        }
        this.type = typeArray[temp];
        this.strWeightMult = strTypeMults[temp];
        crewType = crewMults[temp];
    }

    public void SetTech(int tech) {
        int temp = tech - 1;
        if (temp < 0 || temp > 1) {
            System.out.println("Invalid input, using default: Inner Sphere(1)");
            temp = 0;
        }
        this.tech = techArray[temp];
        techBasePowerMults = powerSystemMults[temp];
        motiveTechMults = motiveMults[temp];
    }

    public void SetStrClass(int iClass) {
        int temp = iClass - 1;
        if (temp < 0 || temp > 3) {
            System.out.println("Invalid input, using default: medium(2)");
            temp = 1;
        }
        strClass = classArray[temp];
        SetCfAndHex();
    }

    public void SetMotive(int motive) {
        int temp = motive - 1;
        if (temp < 0 || temp > 3) {
            System.out.println("Invalid input, using default: ground(1)");
            temp = 0;
        }
        this.motive = motiveArray[temp];
        setMoveMax();
        finalMotiveMult = motiveTechMults[temp];
        motiveIndex = temp;
        finalCrewMult = crewType[temp];
    }

    public void SetMoveSpeed(double speed) {
        double temp = speed;
        if (temp < 0 || temp > moveMax || temp % 0.25 != 0) {
            System.out.println("Invalid input, using default: 1");
            temp = 1;
        }
        moveSpeed = temp;
    }

    public void SetEngine(int engine) {
        int temp = engine - 1;
        if (temp < 0 || temp > 4) {
            System.out.println("Invalid input, using default: fusion(5)");
            temp = 4;
        }
        this.engine = engineArray[temp];
        if (temp == 0) {
            fuelMult = fuelMults[0];
        } else if (temp == 1 || temp == 2) {
            fuelMult = fuelMults[1];
        } else {
            fuelMult = fuelMults[2];
        }
        double[] enginePowerMults = techBasePowerMults[temp];
        finalPowerMult = enginePowerMults[motiveIndex];
    }

    public void SetNumHexes(int hexes) {
        int temp = hexes;
        if (temp < 2 || temp > hexMax) {
            System.out.println("Invalid input, using default: 2");
            temp = 2;
        }
        numHexes = temp;
    }

    public void SetHeight(int height) {
        int temp = height;
        if (temp < 1 || temp > levelMax) {
            System.out.println("Invalid input, using default: 1");
            temp = 1;
        }
        this.height = temp;
    }

    public void SetCf(int cf) {
        int temp = cf;
        if (temp < cfMin || temp > cfMax) {
            System.out.println("Invalid input, using default: " + cfMin);
            temp = cfMin;
        }
        conFactor = temp;
    }


    // Getters
    public String GetType(){
        return type;
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
}
