package pl.magzik.ui.utils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.AbstractDocument;

/**
 * Utility class. Creates Swing components.
 * */
public class Utility {

    /**
     * Creates {@link JTextField} that support only positive integers. <b>It is happy method</b>.
     * @return {@link JTextField} created JTextField
     * */
    public static JTextField createUnsignedIntegerTextField() {
        JTextField textField = new JTextField("0", 5);
        textField.setBorder(new EmptyBorder(2, 5, 2, 5));
        textField.setHorizontalAlignment(JTextField.RIGHT);
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new UnsignedIntegerDocumentFilter());

        return textField;
    }

    /**
     * Creates {@link JTextField} that support only positive integers. <b>It is happy method</b>.
     * @param title Title of text field.
     * @return {@link JTextField} created JTextField
     * */
    public static JTextField createUnsignedIntegerTextField(String title) {
        JTextField textField = new JTextField("0", 8);
        textField.setBorder(new TitledBorder(
                new EmptyBorder(2, 5, 2, 5),
                title
        ));
        textField.setHorizontalAlignment(JTextField.RIGHT);
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new UnsignedIntegerDocumentFilter());

        return textField;
    }

    /**
     * Creates {@link JTextField} that support both positive and negative integers. <b>It is sad method</b>.
     * @param title Title of text field.
     * @return {@link JTextField} created JTextField
     * */
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
