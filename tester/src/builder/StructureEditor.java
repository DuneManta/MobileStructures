package builder;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StructureEditor extends JFrame {
    private JPanel contentPane;
    private JComboBox structureType;
    private JComboBox techBase;
    private JComboBox structureClass;
    private JComboBox powerSystem;
    private JComboBox motive;
    private JSpinner fuel;
    private JSpinner height;
    private JSpinner structureSize;
    private JSpinner conFactor;
    private JSpinner movement;

    private Structure userStructure = new Structure();

    public StructureEditor() {
        setTitle("Mobile Structure Builder");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(contentPane);
        pack();
        setLocationRelativeTo(null);

        powerSystem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FuelToggle();
                userStructure.SetEngine(powerSystem.getSelectedItem());
            }
        });


        // Spinner for number of hexes
        SpinnerModel sizeModel = new SpinnerNumberModel(2, 2, 20, 1);
        structureSize.setModel(sizeModel);

        // Spinner for fuel
        SpinnerModel fuelModel = new SpinnerNumberModel(0, 0, 100000, 1000);
        fuel.setModel(fuelModel);

        // Spinner for height
        SpinnerModel heightModel = new SpinnerNumberModel(1, 1, 30, 1);
        height.setModel(heightModel);

        // Spinner for CF
        SpinnerModel cfModel = new SpinnerNumberModel(1, 1, 150, 1);
        conFactor.setModel(cfModel);

        // Spinner for movement
        SpinnerModel moveModel = new SpinnerNumberModel(0, 0, 2, 0.25);
        movement.setModel(moveModel);
    }

    // Function to enable or disable fuel spinner based on power system selection
    private void FuelToggle() {
        Object engine = powerSystem.getSelectedItem();
        fuel.setEnabled(engine == "Steam" || engine == "Internal Combustion (ICE)" || engine == "Fuel Cell");
    }
}
