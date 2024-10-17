import javax.swing.*;
import java.util.Objects;


public class Main {
    public static void main(String[] args) {

        // Launch GUI window
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                StructureEditor structureEditor = new StructureEditor();
                structureEditor.setVisible(true);
            }
        });

        // Old code for console version of project below.


//        // Basic input-output console
//
//        // To Do -- Turn all the various different iteration of structures into
//        // class objects to simplify selection and variable assignment
//
//        // Stage 1
//        // Needed parameters: Structure class, tech base, structure type, construction factor,
//        // number of hexes, maximum height
//
//        // Create structure object
//        Structure newStruct = new Structure();
//
//        // Get structure type
//        System.out.print("Please select the type of structure: hanger (1), building (2), fortress (3) ");
//        newStruct.SetType(Integer.parseInt(System.console().readLine()));
//
//        // Get tech base for later calculations in stage 2
//        System.out.print("Please select the technology base: Inner Sphere(1), Clan(2) ");
//        newStruct.SetTech(Integer.parseInt(System.console().readLine()));
//
//        // Check chosen type and generate appropriate class selection
//        String ClassOut = "";
//        switch (newStruct.GetType()) {
//            case "hanger" -> ClassOut = "light(1), medium(2), heavy(3), hardened(4)";
//            case "building" -> ClassOut = "light(1), medium(2), heavy(3)";
//            case "fortress" -> ClassOut = "medium(2), heavy(3), hardened(4)";
//        }
//
//        // Get structure class
//        System.out.print("\nSelect class: " + ClassOut + " ");
//        newStruct.SetStrClass(Integer.parseInt(System.console().readLine()));
//
//        // Get number of hexes
//        System.out.print("\nSelect number of hexes: (2-" + newStruct.GetHexMax() + ") ");
//        newStruct.SetNumHexes(Integer.parseInt(System.console().readLine()));
//
//        // Get max height
//        System.out.print("\nSelect max height: (1-" + newStruct.GetLevelMax() + ") ");
//        newStruct.SetHeight(Integer.parseInt(System.console().readLine()));
//
//        // Get construction factor
//        System.out.print("\nSelect construction factor: (" + newStruct.GetCfMin() + "-" + newStruct.GetCfMax() + ") ");
//        newStruct.SetCf(Integer.parseInt(System.console().readLine()));
//
//        // Stage 2
//        // Needed inputs: engine type, desired MP, operating range (if applicable)
//        // Will also calculate minimum crew requirements
//
//        // Get motive type
//        System.out.print("Please enter the motive type: Ground(1), Air(2), Water-surface(3), Water-submersible(4) ");
//        newStruct.SetMotive(Integer.parseInt(System.console().readLine()));
//
//        // Get engine type
//        System.out.print("Please enter the engine type: Steam(1), Internal Combustion(2), Fuel Cell(3), Fission(4), Fusion(5) ");
//       newStruct.SetEngine(Integer.parseInt(System.console().readLine()));
//
//        // Get movement speed
//        System.out.print("Please enter desired maximum movement in increments of 0.25. Maximum value: "
//                + newStruct.GetMoveMax() + " ");
//        newStruct.SetMoveSpeed(Double.parseDouble(System.console().readLine()));
//
//        // Call method to calculate weights and output values
//        newStruct.CalculateWeights();
    }
}