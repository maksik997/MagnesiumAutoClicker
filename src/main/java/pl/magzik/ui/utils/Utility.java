package pl.magzik.ui.utils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.AbstractDocument;

public class Utility {

    public static JTextField createIntegerTextField() {
        JTextField textField = new JTextField("0", 5);
        textField.setBorder(new EmptyBorder(2, 5, 2, 5));
        textField.setHorizontalAlignment(JTextField.RIGHT);
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new IntegerDocumentFilter());

        return textField;
    }

    public static JTextField createIntegerTextField(String title) {
        JTextField textField = new JTextField("0", 8);
        textField.setBorder(new TitledBorder(
                new EmptyBorder(2, 5, 2, 5),
                title
        ));
        textField.setHorizontalAlignment(JTextField.RIGHT);
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new IntegerDocumentFilter());

        return textField;
    }

}
