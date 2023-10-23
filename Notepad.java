package notepad;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

class Notepad extends JFrame {
    private JTextPane textPane;
    private int stickyNoteCount = 1;
    private JPanel stickyPanel;
    private JPanel remindersPanel;
    private JLabel timeLabel; // Time clock label

    public Notepad() {
        setTitle("Notepad with Sticky Pad and Reminders");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        getContentPane().setBackground(new Color(240, 240, 255)); // Light Blue background color
        textPane = new JTextPane();
        textPane.setBackground(new Color(255, 230, 240)); // Really Light Pink text background color
        add(new JScrollPane(textPane), BorderLayout.CENTER);

        // Top panel with buttons
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(173, 216, 230)); // Sky Blue color for top panel
        JButton boldButton = new JButton("Bold");
        boldButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boldAction();
            }
        });

        JButton italicButton = new JButton("Italic");
        italicButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                italicAction();
            }
        });

        JButton colorButton = new JButton("Font Color");
        colorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeFontColor();
            }
        });

        JButton newStickyNoteButton = new JButton("Create Sticky Note");
        newStickyNoteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createStickyNote();
            }
        });

        JButton addReminderButton = new JButton("Add Reminder");
        addReminderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addReminder();
            }
        });

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveToFile();
            }
        });

        JButton loadButton = new JButton("Load");
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadFromFile();
            }
        });

        topPanel.add(boldButton);
        topPanel.add(italicButton);
        topPanel.add(colorButton);
        topPanel.add(newStickyNoteButton);
        topPanel.add(addReminderButton);
        topPanel.add(saveButton);
        topPanel.add(loadButton);

        // Initialize timeLabel and set its position
        timeLabel = new JLabel();
        timeLabel.setHorizontalAlignment(JLabel.RIGHT);
        timeLabel.setForeground(Color.BLACK);
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        updateTime();
        JPanel timePanel = new JPanel(new BorderLayout());
        timePanel.add(timeLabel, BorderLayout.EAST);
        topPanel.add(timePanel); // Add timeLabel to the top panel

        add(topPanel, BorderLayout.NORTH);
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(new Color(240, 240, 255));

        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(new Color(240, 240, 255)); // Really Light Pink color
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        stickyPanel = new JPanel();
        stickyPanel.setLayout(new BoxLayout(stickyPanel, BoxLayout.Y_AXIS));
        stickyPanel.add(new JLabel("Sticky Notes:"));

        remindersPanel = new JPanel();
        remindersPanel.setLayout(new BoxLayout(remindersPanel, BoxLayout.Y_AXIS));
        remindersPanel.add(new JLabel("Reminders:"));

        leftPanel.add(stickyPanel);
        rightPanel.add(remindersPanel);

        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);

        setSize(800, 600);
        setLocationRelativeTo(null);
    }

    private void updateTime() {
		// TODO Auto-generated method stub
		
	}

	private void createStickyNote() {
        JTextArea stickyNote = new JTextArea(5, 10);
        stickyNote.setLineWrap(true);
        stickyNote.setWrapStyleWord(true);
        stickyNote.setBackground(new Color(255, 255, 150)); // Set sticky note color
        stickyNote.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JScrollPane scrollPane = new JScrollPane(stickyNote);
        stickyPanel.add(scrollPane);
        stickyNote.setText("Sticky Note " + stickyNoteCount + ":\n");
        stickyNoteCount++;
        revalidate();
    }

    private void addReminder() {
        String reminderText = JOptionPane.showInputDialog("Enter Reminder:");
        if (reminderText != null && !reminderText.trim().isEmpty()) {
            JCheckBox checkBox = new JCheckBox(reminderText);
            remindersPanel.add(checkBox);
            revalidate();
        }
    }

    private void boldAction() {
        MutableAttributeSet attrs = new SimpleAttributeSet();
        StyleConstants.setBold(attrs, !StyleConstants.isBold(attrs));
        textPane.setCharacterAttributes(attrs, false);
    }

    private void italicAction() {
        MutableAttributeSet attrs = new SimpleAttributeSet();
        StyleConstants.setItalic(attrs, !StyleConstants.isItalic(attrs));
        textPane.setCharacterAttributes(attrs, false);
    }

    private void changeFontColor() {
        Color selectedColor = JColorChooser.showDialog(null, "Choose Font Color", Color.BLACK);
        if (selectedColor != null) {
            MutableAttributeSet attrs = new SimpleAttributeSet();
            StyleConstants.setForeground(attrs, selectedColor);
            textPane.setCharacterAttributes(attrs, false);
        }
    }

    private void saveToFile() {
        JFileChooser fileChooser = new JFileChooser();
        int response = fileChooser.showSaveDialog(null);
        if (response == JFileChooser.APPROVE_OPTION) {
            try (PrintWriter writer = new PrintWriter(fileChooser.getSelectedFile())) {
                writer.write(textPane.getText());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadFromFile() {
        JFileChooser fileChooser = new JFileChooser();
        int response = fileChooser.showOpenDialog(null);
        if (response == JFileChooser.APPROVE_OPTION) {
            try (BufferedReader reader = new BufferedReader(new FileReader(fileChooser.getSelectedFile()))) {
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
                textPane.setText(content.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Notepad notepad = new Notepad();
                notepad.setVisible(true);
            }
        });
    }
}
