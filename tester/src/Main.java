//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        // Basic input-output console

        // To Do -- Turn all the various different iteration of structures into
        // class objects to simplify selection and variable assignment

        // Stage 1
        // Needed parameters: Structure class, structure type, construction factor,
        // number of hexes, maximum height

        // Get structure type
        System.out.print("Please select the type of structure: hanger (1), building (2), fortress (3) ");
        int StrType = Integer.parseInt(System.console().readLine());

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
        int cfMin = 0;
        int cfMax = 0;
        int HexMax = 0;
        int LevelMax = 0;
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
        // Needed inputs: 
    }
}