package builder;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
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
    private JLabel hexWeight;
    private JLabel crew;
    private JLabel totalWeight;
    private JLabel powerWeight;
    private JLabel motiveWeight;
    private JLabel remainingWeight;
    private JLabel fuelWeight;
    private JLabel officers;
    private JLabel fuelWeightLabel;
    private JLabel powerPerHex;
    private JLabel motivePerHex;

    private final Structure userStructure = new Structure();

    public StructureEditor() {
        setTitle("Mobile Structure Builder");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(contentPane);
        pack();
        setLocationRelativeTo(null);

        Calculate();
        SetFields();


        // Listeners
        // Listener for structure type
        structureType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userStructure.SetType((String) structureType.getSelectedItem());
                if (structureType.getSelectedItem() == "Fortress" && structureClass.getSelectedItem() == "Light") {
                    userStructure.SetStrClass("Medium");
                }
                if (structureType.getSelectedItem() == "Fortress" && motive.getSelectedItem() == "Air") {
                    userStructure.SetMotive("Ground");
                }
                Calculate();
            }
        });

        // Listener for tech base
        techBase.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userStructure.SetTech((String) techBase.getSelectedItem());
                Calculate();
            }
        });

        // Listener for class
        structureClass.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userStructure.SetStrClass((String) structureClass.getSelectedItem());
                Calculate();
            }
        });

        // Listener for hex spinner
        structureSize.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                userStructure.SetNumHexes((Integer) structureSize.getValue());
                Calculate();
            }
        });

        // Listener for height spinner
        height.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                userStructure.SetHeight((Integer) height.getValue());
                Calculate();
            }
        });

        // Listener for construction factor
        conFactor.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                userStructure.SetCf((Integer) conFactor.getValue());
                Calculate();
            }
        });

        // Listener for power system
        powerSystem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FuelToggle();
                userStructure.SetEngine((String) powerSystem.getSelectedItem());
                Calculate();
            }
        });

        // Listener for motive system
        motive.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userStructure.SetMotive((String) motive.getSelectedItem());
                Calculate();
            }
        });

        // Listener for range
        fuel.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                userStructure.SetRange((Integer) fuel.getValue());
                Calculate();
            }
        });

        // Listener for movement
        movement.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                userStructure.SetMoveSpeed((Double) movement.getValue());
                Calculate();
            }
        });

        // Listener for fuel
        fuel.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                userStructure.SetRange((Integer) fuel.getValue());
                Calculate();
            }
        });
    }


    // Function to enable or disable fuel spinner and output fields based on power system selection
    private void FuelToggle() {
        Object engine = powerSystem.getSelectedItem();
        fuel.setEnabled(engine == "Steam" || engine == "Internal Combustion (ICE)" || engine == "Fuel Cell");
        fuelWeightLabel.setVisible(engine == "Steam" || engine == "Internal Combustion (ICE)" || engine == "Fuel Cell");
        fuelWeight.setVisible(engine == "Steam" || engine == "Internal Combustion (ICE)" || engine == "Fuel Cell");
        if (engine == "Steam" || engine == "Internal Combustion (ICE)" || engine == "Fuel Cell") {
            userStructure.SetRange(0);
        }
    }

    // Function to call calculate weights and set UI selectors to new values based on selections
    private void Calculate() {
        userStructure.CalculateWeights();

        // Model setups for fields that have changing parameters
        // Check for current values that are out of bounds and adjust
        int hexValue;
        int heightValue;
        int cfValue;
        double speedValue;

        // Number of hexes
        if (userStructure.GetNumHexes() > userStructure.GetHexMax()) {
            hexValue = userStructure.GetHexMax();
            userStructure.SetNumHexes(hexValue);
        } else {
            hexValue = userStructure.GetNumHexes();
        }

        // Height
        if (userStructure.GetHeight() > userStructure.GetLevelMax()) {
            heightValue = userStructure.GetLevelMax();
            userStructure.SetHeight(heightValue);
        } else {
            heightValue = userStructure.GetHeight();
        }

        // Construction factor
        if (userStructure.GetCf() > userStructure.GetCfMax()) {
            cfValue = userStructure.GetCfMax();
            userStructure.SetCf(cfValue);
        } else if (userStructure.GetCf() < userStructure.GetCfMin()) {
            cfValue = userStructure.GetCfMin();
            userStructure.SetCf(cfValue);
        } else {
            cfValue = userStructure.GetCf();
        }

        // Movement
        if (userStructure.GetSpeed() > userStructure.GetMoveMax()) {
            speedValue = userStructure.GetMoveMax();
            userStructure.SetMoveSpeed(speedValue);
        } else {
            speedValue = userStructure.GetSpeed();
        }


        // Spinner for number of hexes
        structureSize.setModel(new SpinnerNumberModel(hexValue, 2, userStructure.GetHexMax(), 1));

        // Spinner for fuel
        fuel.setModel(new SpinnerNumberModel(userStructure.GetRange(), 0, 40000, 100));

        // Spinner for height
        height.setModel(new SpinnerNumberModel(heightValue, 1, userStructure.GetLevelMax(), 1));

        // Spinner for CF
        conFactor.setModel(new SpinnerNumberModel(cfValue, userStructure.GetCfMin(), userStructure.GetCfMax(), 1));

        // Spinner for movement
        movement.setModel(new SpinnerNumberModel(speedValue, 0, userStructure.GetMoveMax(), 0.25));

        // Combo box for class
        structureClass.setModel(new DefaultComboBoxModel(userStructure.GetAvailableClasses()));
        structureClass.setSelectedItem(userStructure.GetClass());

        // Combo box for motive
        motive.setModel(new DefaultComboBoxModel(userStructure.GetAvailableMotives()));
        structureClass.setSelectedItem(userStructure.GetMotive());

        userStructure.CalculateWeights();

        SetFields();
    }

    private void SetFields() {
        hexWeight.setText(userStructure.GetHexWeight() + " Tons");
        totalWeight.setText(userStructure.GetTotalWeight() + " Tons");
        powerWeight.setText(userStructure.GetPowerWeight() + " Tons");
        motiveWeight.setText(userStructure.GetMotiveWeight() + " Tons");
        remainingWeight.setText(userStructure.GetRemainingWeight() + " Tons");
        officers.setText(userStructure.GetOfficers() + " Officers");
        crew.setText(userStructure.GetCrew() + " Crew");
        fuelWeight.setText(userStructure.GetFuelWeight() + " Tons");
        powerPerHex.setText(userStructure.GetPowerPerHex() + " Tons per hex");
        motivePerHex.setText(userStructure.GetMotivePerHex() + " Tons per hex");
    }
}
