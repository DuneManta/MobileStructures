package builder;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        // Launch GUI window
        SwingUtilities.invokeLater(() -> {
            StructureEditor structureEditor = new StructureEditor();
            structureEditor.setVisible(true);
        });
    }
}