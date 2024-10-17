import javax.swing.*;

public class StructureEditor extends JFrame {
    private JPanel contentPane;
    private JComboBox structureType;
    private JComboBox techBase;
    private JComboBox structureClass;
    private JSpinner structureSize;
    private JSpinner spinner1;
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JSpinner spinner2;
    private JSpinner spinner3;
    private JSpinner spinner4;

    public StructureEditor() {
        setTitle("Mobile Structure Builder");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(contentPane);
        pack();
        setLocationRelativeTo(null);


    }
}
