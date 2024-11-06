package builder;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class StructureEditor extends JFrame {
    private JPanel structurePane;
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
    private JTextField name;

    public static Structure userStructure = new Structure();

    // Listeners
    // Listener for structure type
    ActionListener typeBox = new ActionListener() {
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
    };

    // Listener for tech base
    ActionListener techBox = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            userStructure.SetTech((String) techBase.getSelectedItem());
            Calculate();
        }
    };

    // Listener for class
    ActionListener classBox = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            userStructure.SetStrClass((String) structureClass.getSelectedItem());
            if (structureClass.getSelectedItem() == "Hardened" && motive.getSelectedItem() == "Air") {
                userStructure.SetMotive("Ground");
            }
            Calculate();
        }
    };

    // Listener for hex spinner
    ChangeListener hexSpin = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            userStructure.SetNumHexes((Integer) structureSize.getValue());
            Calculate();
        }
    };

    // Listener for height spinner
    ChangeListener heightSpin = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            userStructure.SetHeight((Integer) height.getValue());
            Calculate();
        }
    };

    // Listener for construction factor
    ChangeListener cfSpin = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            userStructure.SetCf((Integer) conFactor.getValue());
            Calculate();
        }
    };

    // Listener for power system
    ActionListener powerBox = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            FuelToggle();
            userStructure.SetEngine((String) powerSystem.getSelectedItem());
            Calculate();
        }
    };

    // Listener for motive system
    ActionListener motiveBox = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            userStructure.SetMotive((String) motive.getSelectedItem());
            Calculate();
        }
    };

    // Listener for movement
    ChangeListener moveSpin = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            userStructure.SetMoveSpeed((Double) movement.getValue());
            Calculate();
        }
    };

    // Listener for fuel
    ChangeListener fuelSpin = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            userStructure.SetRange((Integer) fuel.getValue());
            Calculate();
        }
    };


    public StructureEditor() {
        JTabbedPane tabbedPane = new JTabbedPane();

        setTitle("Mobile Structure Builder");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        tabbedPane.addTab("Structure", null, structurePane);
        tabbedPane.addTab("Equipment", null, equipmentPane);
        setContentPane(tabbedPane);
        pack();
        setJMenuBar(menuBar());
        //setLocationRelativeTo(null);
        Calculate();

        CreateEquipmentLists();
        AddEquipmentListeners();
    }

    // Function to add listeners
    private void AddListeners() {
        structureType.addActionListener(typeBox);
        techBase.addActionListener(techBox);
        structureClass.addActionListener(classBox);
        powerSystem.addActionListener(powerBox);
        motive.addActionListener(motiveBox);
        structureSize.addChangeListener(hexSpin);
        height.addChangeListener(heightSpin);
        conFactor.addChangeListener(cfSpin);
        fuel.addChangeListener(fuelSpin);
        movement.addChangeListener(moveSpin);
    }

    // Function to remove listeners
    private void RemoveListeners() {
        structureType.removeActionListener(typeBox);
        techBase.removeActionListener(techBox);
        structureClass.removeActionListener(classBox);
        powerSystem.removeActionListener(powerBox);
        motive.removeActionListener(motiveBox);
        structureSize.removeChangeListener(hexSpin);
        height.removeChangeListener(heightSpin);
        conFactor.removeChangeListener(cfSpin);
        fuel.removeChangeListener(fuelSpin);
        movement.removeChangeListener(moveSpin);
    }


    // Function to enable or disable fuel spinner and output fields based on power system selection
    private void FuelToggle() {
        Object engine = powerSystem.getSelectedItem();
        fuel.setEnabled(engine == "Steam" || engine == "Internal Combustion (ICE)" || engine == "Fuel Cell");
        fuelWeightLabel.setVisible(engine == "Steam" || engine == "Internal Combustion (ICE)" || engine == "Fuel Cell");
        fuelWeight.setVisible(engine == "Steam" || engine == "Internal Combustion (ICE)" || engine == "Fuel Cell");
        if (!(engine == "Steam" || engine == "Internal Combustion (ICE)" || engine == "Fuel Cell")) {
            userStructure.SetRange(0);
        }
    }

    // Function to call calculate weights and set UI selectors to new values based on selections
    public void Calculate() {
        RemoveListeners();
        userStructure.CalculateWeights();
        userStructure.SetName(name.getText());

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
        motive.setSelectedItem(userStructure.GetMotive());

        // Combo box for type
        structureType.setModel(new DefaultComboBoxModel(userStructure.GetAvailableTypes()));
        structureType.setSelectedItem(userStructure.GetType());

        // Extra field setters for loading and resetting
        techBase.setSelectedItem(userStructure.GetTech());
        powerSystem.setSelectedItem(userStructure.GetEngine());
        name.setText(userStructure.GetName());

        userStructure.CalculateWeights();

        SetFields();
        AddListeners();
    }

    public void SetFields() {
        hexWeight.setText(userStructure.GetHexWeight() + " Tons");
        totalWeight.setText(userStructure.GetTotalWeight() + " Tons");
        powerWeight.setText(userStructure.GetPowerWeight() + " Tons");
        motiveWeight.setText(userStructure.GetMotiveWeight() + " Tons");
        remainingWeight.setText(userStructure.GetRemainingWeight() + " Tons");
        officers.setText(userStructure.GetOfficers() + " Officers");
        crew.setText(userStructure.GetCrew() + " Crew");
        fuelWeight.setText(String.format(String.format("%.1f", userStructure.GetFuelWeight())) + " Tons");
        powerPerHex.setText(userStructure.GetPowerPerHex() + " Tons per hex");
        motivePerHex.setText(userStructure.GetMotivePerHex() + " Tons per hex");
    }

    // Getter for object serialization
    public Structure GetUserStructure() {
        return userStructure;
    }

    // Setter for loading object
    public void SetUserStructure(Structure input) {
        userStructure = new Structure(input);
    }

    // Create menu bar
    public JMenuBar menuBar() {
        // Create menu bar
        JMenuBar menuBar;
        JMenu menu;
        JMenuItem menuItem;

        menuBar = new JMenuBar();

        menu = new JMenu("File");
        menuBar.add(menu);

        menuItem = new JMenuItem("Save");
        menuItem.addActionListener(e -> Save());
        menu.add(menuItem);

        menuItem = new JMenuItem("Load");
        menuItem.addActionListener(e -> {
            Load();
            Calculate();
        });
        menu.add(menuItem);

        menuItem = new JMenuItem("Reset");
        menuItem.addActionListener(e -> {
            Reset();
            Calculate();
        });
        menu.add(menuItem);

        return menuBar;
    }

    // Save structure
    public void Save() {
        Structure str = GetUserStructure();

        try {
            FileOutputStream fileOut = new FileOutputStream("structures/" + userStructure.GetName() + ".ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(str);
            out.close();
            fileOut.close();
            System.out.println("Saved successfully");
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    // Load structure
    public void Load() {
        try {
            FileInputStream fileIn = getFileInputStream();
            ObjectInputStream in = new ObjectInputStream(fileIn);
            Structure structure = (Structure) in.readObject();
            in.close();
            fileIn.close();
            SetUserStructure(structure);
            Calculate();
            FuelToggle();
            System.out.println("Loaded");
        } catch (IOException i) {
            i.printStackTrace();
        } catch (ClassNotFoundException c) {
            System.out.println("Structure class not found");
            c.printStackTrace();
        }
    }

    private FileInputStream getFileInputStream() throws FileNotFoundException {
        FileDialog dialog = new FileDialog((Frame)null, "Select structure to load");
        dialog.setMode(FileDialog.LOAD);
        String relativePath = "structures";
        File currentDirectory = new File(".");
        File targetDirectory = new File(currentDirectory, relativePath);
        dialog.setDirectory(targetDirectory.getAbsolutePath());
        dialog.setFile("*.ser");
        dialog.setVisible(true);
        String file = dialog.getFile();
        dialog.dispose();

        return new FileInputStream("structures/" + file);
    }

    // Function to reset all fields to default
    public void Reset() {
        userStructure = null;
        userStructure = new Structure();
    }

/* Division between the structure panel functions and equipment panel functions. Will play around with separating into separate files later. */

    private JList equipmentList;
    private JList addedEquipment;
    private JPanel equipmentPane;
    private JButton removeButton;
    private JButton removeAll;
    private JButton addButton;

    DefaultListModel<Object> leftModel = new DefaultListModel<>();
    DefaultListModel<Object> rightModel = new DefaultListModel<>();

    private void AddEquipmentListeners() {
        addButton.addActionListener(e -> {
            for (Object selectedValue:equipmentList.getSelectedValuesList()) {
                leftModel.addElement(selectedValue);
            }
        });

        removeButton.addActionListener(e -> {
            for (Object selectedValue:addedEquipment.getSelectedValuesList()) {
                leftModel.removeElement(selectedValue);
            }
        });

        removeAll.addActionListener(e -> {
            leftModel.removeAllElements();
        });
    }

    private void CreateEquipmentLists() {
        rightModel.addElement("SRM 2");
        rightModel.addElement("Small Laser");
        rightModel.addElement("AC/2");

        equipmentList.setModel(rightModel);
        addedEquipment.setModel(leftModel);
    }
}
