public class Main {
    public static void main(String[] args) {
        // Basic input-output console

        // To Do -- Turn all the various different iteration of structures into
        // class objects to simplify selection and variable assignment

        // Stage 1
        // Needed parameters: Structure class, tech base, structure type, construction factor,
        // number of hexes, maximum height

        // Get structure type
        System.out.print("Please select the type of structure: hanger (1), building (2), fortress (3) ");
        int StrType = Integer.parseInt(System.console().readLine());

        // Get tech base for later calculations in stage 2
        System.out.print("Please select the technology base: Inner Sphere(1), Clan(2) ");
        int StrTech = Integer.parseInt(System.console().readLine());

        // Check chosen type and generate appropriate class selection
        String ClassOut = switch (StrType) {
            case 1 -> "light(1), medium(2), heavy(3), hardened(4)";
            case 2 -> "light(1), medium(2), heavy(3)";
            case 3 -> "medium(2), heavy(3), hardened(4)";
            default -> "You broke something";
        };

        // Get structure class
        System.out.print("\nSelect class: " + ClassOut + " ");
        int StrClass = Integer.parseInt(System.console().readLine());

        // Check chosen class and output appropriate CF amount for type and class
        // Also find maximum hexes and levels for later output
        int cfMin = 0, cfMax = 0, HexMax = 0, LevelMax = 0;
        switch (StrType) {
            case 1:
                switch (StrClass) {
                    case 1 -> {
                        cfMin = 1;
                        cfMax = 8;
                        HexMax = 10;
                        LevelMax = 7;
                    }
                    case 2 -> {
                        cfMin = 9;
                        cfMax = 20;
                        HexMax = 14;
                        LevelMax = 10;
                    }
                    case 3 -> {
                        cfMin = 21;
                        cfMax = 45;
                        HexMax = 18;
                        LevelMax = 13;
                    }
                    case 4 -> {
                        cfMin = 46;
                        cfMax = 75;
                        HexMax = 20;
                        LevelMax = 14;
                    }
                }
                break;
            case 2:
                switch (StrClass) {
                    case 1 -> {
                        cfMin = 1;
                        cfMax = 15;
                        HexMax = 6;
                        LevelMax = 5;
                    }
                    case 2 -> {
                        cfMin = 16;
                        cfMax = 40;
                        HexMax = 8;
                        LevelMax = 8;
                    }
                    case 3 -> {
                        cfMin = 41;
                        cfMax = 90;
                        HexMax = 10;
                        LevelMax = 10;
                    }
                }
                break;
            case 3:
                switch (StrClass) {
                    case 2 -> {
                        cfMin = 16;
                        cfMax = 40;
                        HexMax = 12;
                        LevelMax = 15;
                    }
                    case 3 -> {
                        cfMin = 41;
                        cfMax = 90;
                        HexMax = 15;
                        LevelMax = 20;
                    }
                    case 4 -> {
                        cfMin = 91;
                        cfMax = 150;
                        HexMax = 20;
                        LevelMax = 30;
                    }
                }
                break;
        }

        // Get number of hexes
        System.out.print("\nSelect number of hexes: (2-" + HexMax + ") ");
        int StrHex = Integer.parseInt(System.console().readLine());

        // Get max height
        System.out.print("\nSelect max height: (1-" + LevelMax + ") ");
        int StrHeight = Integer.parseInt(System.console().readLine());

        // Get construction factor
        System.out.print("\nSelect construction factor: (" + cfMin + "-" + cfMax + ") ");
        int StrCF = Integer.parseInt(System.console().readLine());

        // Do initial per hex capacity calculations
        int HexWeight;
        if (StrType == 1) {
            HexWeight = (int) ((Math.ceil((double) StrHeight / 4)) * 300);
        } else {
            HexWeight = StrCF * StrHeight;
        }
        int StrWeight = StrHex * HexWeight;

        // Output available tonnage
        System.out.println("The maximum weight per hex is " +
                String.format("%,d", HexWeight) + " tons.");
        System.out.println("The maximum weight of the entire structure is " +
                String.format("%,d", StrWeight) + " tons.");


        // Stage 2
        // Needed inputs: engine type, desired MP, operating range (if applicable)
        // Will also calculate minimum crew requirements

        // Get motive type
        System.out.print("Please enter the motive type: Ground(1), Air(2), Water-surface(3), Water-submersible(4) ");
        int StrMotive = Integer.parseInt(System.console().readLine());

        double MoveMax = switch (StrMotive) {
            case 1 -> 2;
            case 2, 4 -> 4;
            case 3 -> 3;
            default -> 0;
        };

        // Get engine type
        System.out.print("Please enter the engine type: Steam(1), Internal Combustion(2), Fuel Cell(3), Fission(4), Fusion(5) ");
        int StrEngine = Integer.parseInt(System.console().readLine());

        // Get movement speed
        System.out.print("Please enter desired maximum movement in increments of 0.25. Maximum value: "
                + MoveMax + " ");
        double StrMove = Double.parseDouble(System.console().readLine());

        // Variables and switch statements for determining the multiplier numbers for the
        // various types of engine and motive types
        double Steam = 0, ICE = 0, FuelCell = 0, Fission = 0, Fusion = 0, Ground = 0, Air = 0, Surface = 0, Submersible = 0;

        if (StrTech == 1) {
            Ground = 4;
            Air = 5;
            Surface = 2;
            Submersible = 3.5;
            switch (StrMotive) {
                case 1, 3:
                    Steam = 6;
                    ICE = 3;
                    FuelCell = 4;
                    Fission = 3;
                    Fusion = 2;
                    break;
                case 2:
                    Steam = 6;
                    ICE = 3;
                    FuelCell = 4.4;
                    Fission = 3;
                    Fusion = 2;
                    break;
                case 4:
                    Steam = 7;
                    ICE = 3.2;
                    FuelCell = 5;
                    Fission = 3;
                    Fusion = 2.2;
                    break;
            }
        } else if (StrTech == 2) {
            Ground = 3.5;
            Air = 4;
            Surface = 1.8;
            Submersible = 3.5;
            switch (StrMotive) {
                case 1, 3:
                    Steam = 7;
                    ICE = 3;
                    FuelCell = 4;
                    Fission = 4;
                    Fusion = 1.8;
                    break;
                case 2:
                    Steam = 7;
                    ICE = 3;
                    FuelCell = 4.2;
                    Fission = 4;
                    Fusion = 1.8;
                    break;
                case 4:
                    Steam = 8;
                    ICE = 3;
                    FuelCell = 4.4;
                    Fission = 4;
                    Fusion = 2;
                    break;
            }
        }

        double[] PowerMults = {0, Steam, ICE, FuelCell, Fission, Fusion};
        double[] MotiveMults = {0, Ground, Air, Surface, Submersible};
        double[] StrMults = {0, 0.3, 0.5, 1};
        double[] FuelMults = {0, 0.04, 0.02, 0.02};

        // Formula for system weights:
        // Power system: (hexes * levels) * MP * power system multiplier
        // Motive system: (hexes * levels) * motive type multiplier * structure type multiplier (hangers 0.3, buildings 0.5, fortress 1.0)
        // Fuel: range in 100km * fuel multiplier * power system weight

        double PowerWeight = (StrHex * StrHeight) * StrMove * PowerMults[StrEngine];
        double MotiveWeight = (StrHex * StrHeight) * MotiveMults[StrMotive] * StrMults[StrType];
        double FuelWeight = 0;

        if (StrEngine == 1 || StrEngine == 2 || StrEngine == 3) {
            System.out.print("Please enter desired maximum range: (in hundreds of kilometers) ");
            int StrRange = Integer.parseInt(System.console().readLine());
            FuelWeight = StrRange * FuelMults[StrEngine] * PowerWeight;
        }

        System.out.println("Total weight of power system is " +
                String.format("%1$,.2f",PowerWeight) + " tons.");

        System.out.println("Total weight of motive system is " +
                String.format("%1$,.2f",MotiveWeight) + " tons.");

        if (FuelWeight != 0) {
            System.out.println("Total weight of fuel is " +
                    String.format("%1$,.2f", FuelWeight) + " tons.");
        }

        // Determine crew per hex requirements based on structure class and motive type
        int HexCrew = 0;
        HexCrew = switch (StrType) {
            case 1 -> switch (StrMotive) {
                case 1, 3 -> 2;
                case 2, 4 -> 3;
                default -> HexCrew;
            };
            case 2 -> switch (StrMotive) {
                case 1, 3 -> 3;
                case 2, 4 -> 4;
                default -> HexCrew;
            };
            case 3 -> switch (StrMotive) {
                case 1 -> 4;
                case 3 -> 5;
                case 4 -> 6;
                default -> HexCrew;
            };
            default -> 0;
        };
        int BaseCrew = HexCrew * StrHex;
        int Officers = (int) Math.ceil((double) BaseCrew / 10);
        int TotalCrew = BaseCrew + Officers;

        System.out.print("Crew requirements:\n" +
                TotalCrew + " total crew.\n" +
                BaseCrew + " base crew members with " + Officers +" officers.");
    }
}