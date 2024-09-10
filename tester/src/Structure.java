public class Structure {

    // Arrays for storing the different values
    private final String[] typeArray = {"hanger", "building", "fortress"};
    private final String[] techArray = {"Inner Sphere", "Clan"};

    private final double[] strMults = {0.3, 0.5, 1};

    // Variables for selected values
    private String type;
    private String tech;

    private double strWeightMult;

    public Structure() {}

    public void setType(int type) {
        int temp = type - 1;
        if (temp > 2 || temp < 0) {
            System.out.println("Invalid input, using default: Building(2)");
            temp = 1;
        }
        this.type = typeArray[temp];
        this.strWeightMult = strMults[temp];
    }
    public String getType(){
        return this.type;
    }

    public void setTech(int tech) {
        int temp = tech - 1;
        if (temp < 0 || temp > 1) {
            System.out.println("Invalid input, using default: Inner Sphere(1)");
            temp = 0;
        }
        this.tech = techArray[temp];
    }
    public String getTech() {
        return this.tech;
    }

    public double getStrWeightMult() {
        return this.strWeightMult;
    }
}
