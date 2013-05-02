/**
 * Programmers: Chase McCowan & Ed Broxson 
 * Date: 02/20/2013 
 * Purpose: Build GUI using GridBagLayout and handle answer checking
 */
package chemistry;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.AttributedString;
import java.text.DecimalFormat;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

public class FreezePointDepression extends JApplet{

    private final static boolean shouldFill = true;
    private final static boolean RIGHT_TO_LEFT = false;
    private final String DELTA = "<html>\u0394T<sub>f</sub></html>";
    private final String DEGREE = "i";
    private final String MOLALITY = "m";        
    private final String WATER_CONSTANT = "<html>K<sub>f</sub></html>";
    private final String WATER_FREEZE = "1.86\u2070C kg/mol";
    private static int height, width;
    private static String periodic, help, helpContent, beaker1 = "0\u2070C", beaker2 = "-7.54\u2070C", beaker3 = "-22.36\u2070C";
    protected static JTable table;
    private static GasChamber bt = new GasChamber();
    private static JDialog dialog, helpDialog;
    private static JLabel imageLabel;
    private static JPanel periodicPanel;
    private static JTextArea helpTextArea;
    private static DecimalFormat format = new DecimalFormat("#,##0.000");
    private static DecimalFormat fiveSF = new DecimalFormat("#,##0.0000");
    
    private static JComboBox knownComboBox, unknownComboBox;                    
    private static JSlider slider;                                              
    private static JLabel correctLabel, correctLabel2, incorrectLabel, incorrectLabel2; 
    
    public static void main(String[] args){
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setIconImage(new ImageIcon("src/layout/pscclogo.jpg").getImage());
        frame.setResizable(false);
        frame.setSize(1000, 600);
        frame.setLocationRelativeTo(null);
        JApplet applet = new FreezePointDepression();
        applet.setPreferredSize(new Dimension(1000, 600));
        applet.setBackground(new Color(12, 66, 116));
        applet.init();
        frame.getContentPane().add(applet);
        frame.pack();
        frame.setVisible(true);
    }
    
    public FreezePointDepression(){
        
    }

    /**
     * Build components for GUI and add listeners
     */
    @Override
    public void init() {
        JPanel pane = new JPanel();
        if (RIGHT_TO_LEFT) {
            pane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        }
        //declare variables 
        JPanel periodHelpPanel, knownComboPanel, unknownComboPanel, sliderPanel,
                tablePanel, boxPanel, checkResetButtonPanel, resultPanel, 
                correctPanel, pauseLabelPanel;
        JButton assistButton, helpButton, goButton,
                resetButton, checkAnswerButton;
        JLabel gasDiffuseLabel, enterLabel, knownComboBoxLabel,
                unknownComboBoxLabel, tempLabel, pauseLabel, sigfigLabel, sigfigLabel2;
        JScrollPane jsp;
        JList <String> knownList, unknownList;

        //make raisedBevel border 
        Border raisedBevel = BorderFactory.createRaisedSoftBevelBorder();

        //set pane layout to grid bag layout 
        pane.setLayout(new GridBagLayout());

        //set pane color 
        pane.setBackground(new Color(12, 66, 116));

        //make grid bag constraints object 
        GridBagConstraints c = new GridBagConstraints();

        //natural height, maximum width 
        if (shouldFill) {
            c.fill = GridBagConstraints.HORIZONTAL;
        }

        ///***************************TITLE**************************
        //title label 
        gasDiffuseLabel = new JLabel("Freeze Point Depression");

        //set font for label 
        gasDiffuseLabel.setFont(new Font("Verdana", Font.BOLD, 35));

        //set foreground color for label 
        gasDiffuseLabel.setForeground(Color.white);

        //set grid bag layout constraints 
        c.insets = new Insets(10, 10, 10, 10);
        c.ipadx = 0;
        c.ipady = 0;
        c.weightx = 0.5;
        c.weighty = 0;
        c.gridwidth = 15;
        c.gridheight = 1;
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;

        //add panel and contraints to pane 
        pane.add(gasDiffuseLabel, c);

        ///******PERIODIC TABLE BUTTON AND HELP BUTTON***************

        //make grid bag constraints object 
        GridBagConstraints periodicGBC = new GridBagConstraints();

        //panel for periodButton and helpButton 
        periodHelpPanel = new JPanel();

        //set panel// color 
        periodHelpPanel.setBackground(new Color(203, 228, 38));

        //set border for button 
        periodHelpPanel.setBorder(raisedBevel);

        //make buttons 
        assistButton = new JButton("Assistance");
        helpButton = new JButton("Help");

        //set grid bag layout constraints 
        //set fill 
        periodicGBC.fill = GridBagConstraints.HORIZONTAL;

        //set padding - this one is good to see how other insets work, the green background makes it easy to see changes
        periodicGBC.insets = new Insets(0, 150, 0, 20);

        //set weight 
        periodicGBC.weightx = 0.5;

        //set grid position 
        periodicGBC.gridx = 3;
        periodicGBC.gridy = 0;

        //set height and width of grid cell 
        periodicGBC.ipadx = 0;
        periodicGBC.ipady = 0;

        //set anchor point within cell 
        periodicGBC.anchor = GridBagConstraints.EAST;

        //add buttons to panel 
        periodHelpPanel.add(assistButton);
        periodHelpPanel.add(helpButton);

        //add panel and contraints to pane 
        pane.add(periodHelpPanel, periodicGBC);

        ///*******DRAWING START BOX, RACE BOX, GATE AND FINISH*********

        //make grid bag constraints object 
        GridBagConstraints boxGBC = new GridBagConstraints();

        //make boxPanel and set layout to gridbaglayout 
        boxPanel = new JPanel();

        boxPanel.setBackground(new Color(12, 66, 116));


        //initialize bt and make it focusable  
        bt.init();
        bt.setFocusable(true);
        Dimension dim = new Dimension(800, 200);                                
        bt.setPreferredSize(dim);  

        //add bt object to boxPanel 
        boxPanel.add(bt);

        // initialize particleFill from GasChamber using variables from 
         // combo boxes
//        bt.particleFill(0, 0);

        //set grid bag layout constraints 
        boxGBC.insets = new Insets(10, 10, 10, 10);
        boxGBC.ipadx = 500;                                              
        boxGBC.ipady = 200;                                              
        boxGBC.weightx = 0.5;
        boxGBC.gridwidth = 15;                                            
        boxGBC.gridheight = 1;                         
        boxGBC.fill = GridBagConstraints.BOTH;
        boxGBC.gridx = 0;
        boxGBC.gridy = 1;

        //add panel and contraints to pane 
        pane.add(boxPanel, boxGBC);

        ///*****KNOWN COMBO BOX***********

        //make grid bag constraints object 
        GridBagConstraints knownGBC = new GridBagConstraints();

        //new jpanel for the known molecules combobox 
        knownComboPanel = new JPanel(new BorderLayout());

        //set panel color 
        knownComboPanel.setBackground(new Color(203, 228, 38));

        //set border for button 
        knownComboPanel.setBorder(raisedBevel);

        //create known combo box list label 
        knownComboBoxLabel = new JLabel("<html>Mols<sub> </sub>NaCl</html>");

        //set font for label 
        knownComboBoxLabel.setFont(new Font("Verdana", Font.BOLD, 15));

        //set foreground color for label 
        knownComboBoxLabel.setForeground(new Color(12, 66, 116));

        //create array of list data for JList 
        String[] knownComboBoxListData = {"0.912 mols",
            "1.418 mols", "2.160 mols",};

        //create combobox and add list data to it 
        knownComboBox = new JComboBox(knownComboBoxListData);

        //create a courses list 
        knownList = new JList<>(knownComboBoxListData);

        //set visible amount of rows in JList to 1 
        knownList.setVisibleRowCount(1);



        //set grid bag layout constraints 
        knownGBC.fill = GridBagConstraints.NONE;
        knownGBC.insets = new Insets(0, 0, 0, 80); // 0 25 0 90
        knownGBC.weightx = 0.0;
        knownGBC.gridwidth = 1;
        knownGBC.gridx = 2;
        knownGBC.gridy = 3;
        knownGBC.ipadx = 130; // 200
        knownGBC.ipady = 0;

        //add combo box label to panel 
        knownComboPanel.add(knownComboBoxLabel, BorderLayout.NORTH);

        //add combo box label to panel 
        knownComboPanel.add(knownComboBox, BorderLayout.SOUTH);

        //add panel to pane 
        pane.add(knownComboPanel, knownGBC);

        ///*********UNKNOWN/KNOWN COMBO BOX***************

        //make grid bag constraints object 
        GridBagConstraints unknownGBC = new GridBagConstraints();

        //create jpanel for the unknown molecules combobox 
        unknownComboPanel = new JPanel(new BorderLayout());

        //set panel color 
        unknownComboPanel.setBackground(new Color(203, 228, 38));

        //set border for button 
        unknownComboPanel.setBorder(raisedBevel);
                
        //create known combo box list label
        unknownComboBoxLabel = new JLabel("<html>Mols Li<sub>2</sub>S</html>");

        //set font for label
        unknownComboBoxLabel.setFont(new Font("Verdana", Font.BOLD, 15));

        //set foreground color for label
        unknownComboBoxLabel.setForeground(new Color(12, 66, 116));

        //create array of list data for JList
        String[] unknownComboBoxListData = {"1.803 mols",
            "2.031 mols", "2.633 mols"};

        //create combobox and add data to it
        unknownComboBox = new JComboBox(unknownComboBoxListData);

        //create a list
        unknownList = new JList<>(unknownComboBoxListData);

        //set visible amount of rows in combo box to 1
        unknownList.setVisibleRowCount(1);

        //set grid bag layout constraints
        unknownGBC.fill = GridBagConstraints.NONE;
        unknownGBC.insets = new Insets(0, 40, 0, 150); // 0 0 0 75
        unknownGBC.weightx = 0.0;
        unknownGBC.gridwidth = 1;                                               // AEB I added
        unknownGBC.gridx = 3;
        unknownGBC.gridy = 3;
        unknownGBC.ipadx = 130; // 30
        unknownGBC.ipady = 0;

        //add combo box label to panel
        unknownComboPanel.add(unknownComboBoxLabel, BorderLayout.NORTH);

        //add combo box to panel
        unknownComboPanel.add(unknownComboBox, BorderLayout.SOUTH);

        //add panel to pane
        pane.add(unknownComboPanel, unknownGBC);

        ///*******TEMPERATURE/REACTION RATE +- SLIDER**************

        //make grid bag constraints object
        GridBagConstraints tempGBC = new GridBagConstraints();

        //make new panel
        sliderPanel = new JPanel(new BorderLayout());

        //set panel color
        sliderPanel.setBackground(new Color(12, 66, 116));

        //make slider
        slider = new JSlider();

        //set slider color
        slider.setBackground(new Color(12, 66, 116));

        //make temperature label
        tempLabel = new JLabel("< - Temperature + >");

        //set font for label
        tempLabel.setFont(new Font("Verdana", Font.BOLD, 15));

        //set foreground color for label
        tempLabel.setForeground(Color.white);

        //set foreground color for slider
        slider.setForeground(Color.white);

        //set slider constraints
        slider.setSize(100, 20);
        slider.setValue(40);
        slider.setMinimum(-10);
        slider.setMaximum(90);
        slider.setMajorTickSpacing(10);
        slider.setMinorTickSpacing(5);
        slider.setPaintTicks(true);

        //set grid bag layout constraints
        tempGBC.fill = GridBagConstraints.HORIZONTAL;
        tempGBC.insets = new Insets(0, 130, 0, 110); // 0 100 0 10
        tempGBC.weightx = .0;
        tempGBC.gridwidth = 1;
        tempGBC.gridx = 0;
        tempGBC.gridy = 3;
        tempGBC.ipadx = 0; // was 60
        tempGBC.ipady = 0;

        //add slider to panel
        sliderPanel.add(tempLabel, BorderLayout.NORTH);
        sliderPanel.add(slider, BorderLayout.CENTER);

        //add button to pane
        pane.add(sliderPanel, tempGBC);

//        ///****************GO BUTTON********************
//
//        //make grid bag constraints object
//        GridBagConstraints goGBC = new GridBagConstraints();
//
//        //create go button
//        goButton = new JButton("GO");
//
//        //set button color
//        goButton.setBackground(new Color(203, 228, 38));
//
//        //set font for label
//        goButton.setFont(new Font("Verdana", Font.BOLD, 20));
//
//        //set foreground color for label
//        goButton.setForeground(Color.darkGray);
//
//        //set border for button
//        goButton.setBorder(raisedBevel);
//
//        //set grid bag layout constraints
//        goGBC.insets = new Insets(0, 0, 0, 30);
//        goGBC.fill = GridBagConstraints.VERTICAL;
//        goGBC.weightx = 50;
//        goGBC.weighty = 0;
//        goGBC.gridwidth = 2;
//        goGBC.gridx = 9;
//        goGBC.gridy = 3;
//        goGBC.ipadx = 30;
//        goGBC.ipady = 0;
//
//        //add button to pane
//        pane.add(goButton, goGBC);

        ///****************TABLE**********************

        //make grid bag constraints object
        GridBagConstraints tableGBC = new GridBagConstraints();

        //create panel for table
        tablePanel = new JPanel(new BorderLayout());

        //set panel color
        tablePanel.setBackground(new Color(12, 66, 116));

        //create label for table
        enterLabel = new JLabel("Use the data in the table to "
                + "compute Van't Hoff Factor and Molality...");

        //set font for label
        enterLabel.setFont(new Font("Verdana", Font.BOLD, 20));

        //set foreground color for label
        enterLabel.setForeground(Color.white);

        //create arrays for column headings and table data
        String[] colHeading = {"Calculation", "Beaker 1: Water", "Beaker 2: Water/Sodium", "Beaker 3: Water/Lithium Sulfide"};
        String[][] data = {{"<html>\u0394T<sub>f</sub></html>", "", "", ""},{"i", "", "", ""},{"m", "", "", ""},{"<html>K<sub>f</sub></html>", "", "", ""}};

        //create table
        table = new JTable(new MyTableModel(data, colHeading));
        
        table.setRowHeight(20);
        
        //keep columns from being moved around
        table.getTableHeader().setReorderingAllowed(false);
        
        //set sizes of columns then keep them from being resized
        for(int i = 0; i < 4; i++){
            table.getColumnModel().getColumn(i).setMinWidth(243);
            table.getColumnModel().getColumn(i).setMaxWidth(243);
        
        }
        table.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );

        //make tool tip
        table.setToolTipText("Calculate van't hoff factor and molality "
                + "then insert into table");

        //make table not enabled to be edited
        table.setEnabled(false);
        

        //create scrollpane for table
        jsp = new JScrollPane(table);

        //set grid bag layout constraints
        tableGBC.fill = GridBagConstraints.BOTH;
        tableGBC.insets = new Insets(0, 33, 0, 37); // 50 10 100 17
        tableGBC.weightx = 0;
        tableGBC.weighty = 0;
        tableGBC.gridx = 0;
        tableGBC.gridy = 5;
        tableGBC.gridwidth = 15;
        tableGBC.gridheight = 1;
        tableGBC.ipadx = 50; // 50
        tableGBC.ipady = 78; // 78

        //add label to panel
        tablePanel.add(enterLabel, BorderLayout.NORTH);

        //add scrollpane to panel
        tablePanel.add(jsp);

        //add panel to pane
        pane.add(tablePanel, tableGBC);

        //add dimensions for table, need to be changed with ipadx and ipady
        Dimension tableDim = new Dimension(970, 78); // 970, 78
        table.setPreferredSize(tableDim);
        
        //set columns to centered text
        DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();    
        dtcr.setHorizontalAlignment(SwingConstants.CENTER);  
        for(int i = 0; i < 4; i++){
            TableColumn col = table.getColumnModel().getColumn(i); 
            col.setCellRenderer(dtcr); 
        }       
        
        //set default values of cells with numbers
        table.setValueAt(0, 1, 1);
        table.setValueAt(0, 2, 1);
        table.setValueAt(WATER_FREEZE, 3, 1);
        table.setValueAt(WATER_FREEZE, 3, 2);
        table.setValueAt(WATER_FREEZE, 3, 3);

        ///*****************CORRECT AND INCORRECT LABELS******************

        correctLabel = new JLabel("Beaker 2 Correct");
        correctLabel2 = new JLabel("Beaker 3 Correct");
        //make grid bag constraints object
        GridBagConstraints correctGBC = new GridBagConstraints();

        resultPanel = new JPanel(new BorderLayout());
        correctPanel = new JPanel();
        correctPanel.setLayout(new BoxLayout(correctPanel, BoxLayout.Y_AXIS));

        //set panel color
        resultPanel.setBackground(new Color(12, 66, 116));
        correctPanel.setBackground(new Color(12, 66, 116));

        //make correct labels
        correctLabel.setFont(new Font("Verdana", Font.BOLD, 16));
        correctLabel.setForeground(Color.GREEN);
        correctLabel.setVisible(false);

        correctLabel2.setFont(new Font("Verdana", Font.BOLD, 16));
        correctLabel2.setForeground(Color.GREEN);
        correctLabel2.setVisible(false);

        //make incorrect labels
        incorrectLabel = new JLabel("Beaker 2 Incorrect");
        incorrectLabel2 = new JLabel("Beaker 3 Incorrect");

        incorrectLabel.setFont(new Font("Verdana", Font.BOLD, 16));
        incorrectLabel.setForeground(Color.RED);
        incorrectLabel.setVisible(false);

        incorrectLabel2.setFont(new Font("Verdana", Font.BOLD, 16));
        incorrectLabel2.setForeground(Color.RED);
        incorrectLabel2.setVisible(false);

        //set grid bag layout constraints
        correctGBC.anchor = GridBagConstraints.WEST;
        correctGBC.insets = new Insets(0, 20, 0, 0);
        correctGBC.weightx = 1;
        correctGBC.gridx = 2;
        correctGBC.gridy = 7;
        correctGBC.gridwidth = 1;
        correctGBC.ipadx = 0;
        correctGBC.ipady = 0;

        correctPanel.add(correctLabel);
        correctPanel.add(incorrectLabel);
        correctPanel.add(correctLabel2);
        correctPanel.add(incorrectLabel2);

        resultPanel.add(correctPanel, BorderLayout.CENTER);

        resultPanel.setVisible(true);

        pane.add(resultPanel, correctGBC);

        ///*****************PAUSE LABEL***********************************
        
        //make grid bag constraints object
        GridBagConstraints pauseGBC = new GridBagConstraints();

        //make pauseLabelPanel
        pauseLabelPanel = new JPanel(new BorderLayout());
        
        //set panel color
        pauseLabelPanel.setBackground(new Color(12, 66, 116));
        
        //make pauseLabel
        pauseLabel = new JLabel("**Pause/Play simulation"
                + " with spacebar or mouseclick in gray box.");
        sigfigLabel = new JLabel("***Enter Molality using 4 significant figures.");
        sigfigLabel2 = new JLabel("***Enter all other values using 4 significant figures.");

        //set font color to white
        pauseLabel.setForeground(Color.WHITE);
        sigfigLabel.setForeground(Color.WHITE);
        sigfigLabel2.setForeground(Color.WHITE);

        //set grid bag layout constraints
        pauseGBC.anchor = GridBagConstraints.EAST;
        pauseGBC.insets = new Insets(0, 20, 0, 0);
        pauseGBC.weightx = 1;
        pauseGBC.gridx = 0;
        pauseGBC.gridy = 7;
        pauseGBC.gridwidth = 2;
        pauseGBC.ipadx = 0;
        pauseGBC.ipady = 0;

        //add pause and sigfig labels to pause panel
        pauseLabelPanel.add(pauseLabel,BorderLayout.NORTH);
        pauseLabelPanel.add(sigfigLabel,BorderLayout.CENTER);
        pauseLabelPanel.add(sigfigLabel2,BorderLayout.SOUTH);
        
        //add pauseLabel to pane
        pane.add(pauseLabelPanel, pauseGBC);

        ///*****************CHECK ANSWER AND RESET BUTTONS****************

        //make grid bag constraints object
        GridBagConstraints answerGBC = new GridBagConstraints();

        //create panel for reset and answer buttons
        checkResetButtonPanel = new JPanel();

        //set panel color
        checkResetButtonPanel.setBackground(new Color(12, 66, 116));

        //create reset and answer buttons
        resetButton = new JButton("Reset");
        checkAnswerButton = new JButton("Check Answer");

        //set grid bag layout constraints
        answerGBC.anchor = GridBagConstraints.CENTER;
        answerGBC.insets = new Insets(0, 0, 0, 0);
        answerGBC.weightx = 1;
        answerGBC.gridx = 3;
        answerGBC.gridy = 7;
        answerGBC.gridwidth = 2;
        answerGBC.ipadx = 0;
        answerGBC.ipady = 0;

        //add buttons to panel
        checkResetButtonPanel.add(resetButton);
        checkResetButtonPanel.add(checkAnswerButton);

        //add panel to pane
        pane.add(checkResetButtonPanel, answerGBC);
        
        /**LISTENERS*/
        // listener for check answer button 
        checkAnswerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //stops editing table and saves current data
                if (table.isEditing()){
                    table.getCellEditor().stopCellEditing();
                }
                //get the values from user input cells and compare them to 
                // correct values
                try{
                    
                
                correctLabel.setVisible(false);
                correctLabel2.setVisible(false);
                incorrectLabel.setVisible(false);
                incorrectLabel2.setVisible(false);
                float answer2 = 0f, answer3 = 0f;
                float mol2a = 2.027f;
                float mol2b = 3.153f;
                float mol2c = 4.801f;
                float vhf2 = 2;
                float mol3a = 4.007f;
                float mol3b = 4.513f;
                float mol3c = 5.853f;
                float vhf3 = 3;
                float cell_1_2, cell_1_3, cell_2_2, cell_2_3;

                cell_1_2 = Float.parseFloat(table.getValueAt(1, 2).toString());
                cell_1_3 = Float.parseFloat(table.getValueAt(1, 3).toString());
                cell_2_2 = Float.parseFloat(table.getValueAt(2, 2).toString());
                cell_2_3 = Float.parseFloat(table.getValueAt(2, 3).toString());
                
                if((knownComboBox.getSelectedIndex() == 0 && cell_1_2 == vhf2 && cell_2_2 == mol2a) || (knownComboBox.getSelectedIndex() == 1 && cell_1_2 == vhf2 && cell_2_2 == mol2b) || (knownComboBox.getSelectedIndex() == 2 && cell_1_2 == vhf2 && cell_2_2 == mol2c)){
                    correctLabel.setVisible(true);
                }else{
                    incorrectLabel.setVisible(true);
                }
                
                if((unknownComboBox.getSelectedIndex() == 0 && cell_1_3 == vhf3 && cell_2_3 == mol3a) || (unknownComboBox.getSelectedIndex() == 1 && cell_1_3 == vhf3 && cell_2_3 == mol3b) || (unknownComboBox.getSelectedIndex() == 2 && cell_1_3 == vhf3 && cell_2_3 == mol3c)){
                    correctLabel2.setVisible(true);
                }else{
                    incorrectLabel2.setVisible(true);
                }
                
                if(knownComboBox.getSelectedIndex() == 0){
                    answer2 = mol2a;
                }else if(knownComboBox.getSelectedIndex() == 1){
                    answer2 = mol2b;
                }else if(knownComboBox.getSelectedIndex() == 2){
                    answer2 = mol2c;
                }

                if(unknownComboBox.getSelectedIndex() == 0){
                    answer3 = mol3a;
                }else if(unknownComboBox.getSelectedIndex() == 1){
                    answer3 = mol3b;
                }else if(unknownComboBox.getSelectedIndex() == 2){
                    answer3 = mol3c;
                }
                               
                JOptionPane.showMessageDialog(null, "The van't hoff factor (i) you entered "
                        + "for Beaker 2, is: " + cell_1_2 + "   the answer is: " + vhf2
                        + "\nThe Molality you entered is: " + cell_2_2 + " the answer is: " + answer2
                        + "\nThe van't hoff factor you entered for selection "
                        + "Beaker 3, is: " + cell_1_3  + " the answer is: " + vhf3
                        + "\nThe Molality you entered is: " + cell_2_3 + " the answer is: " + answer3);
                
                }
                
                catch(NumberFormatException | NullPointerException nfe){
                    
                    JOptionPane.showMessageDialog(null, "Please enter 'i' and 'm'"
                            + " values for Beakers 2 and 3"
                            + " before clicking check answer button.","Answers",JOptionPane.INFORMATION_MESSAGE);
                    
                }

                //focus on beaker box
                bt.requestFocus();
            }
        });
        
        // listener to open the gate and start the race 
//        goButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
////                bt.setGateOpen(true);
////                bt.requestFocus();
//            }
//        });
        
        //create a listener for the help button 
        helpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                bt.requestFocus();
                //make string title variable 
                help = "Help Readme";

                //make string variable for help content 
                helpContent = "This program is designed to show you "
                        + "the concept\nof Freeze Point Depression.\n\n"
                        + "\u0394Tf = Change in Temperature\n"
                        + "i = van’t Hoff factor\n"
                        + "m = Molality\n"
                        + "Kf = Freeze Point Depression for water\n\n"
                        + "Instructions:\n\n"
                        + "Select the mols value from the drop down boxes "
                        + "for\nbeaker 2 and beaker 3.\nThe number of solutes "
                        + "in each beaker will change with\nthe change in mols "
                        + "value.\n\nUse the slider to see changes when the "
                        + "temperature\nheats up or cools down.\n\nWhen you are "
                        + "ready for the quiz, set the temperature\nto its lowest "
                        + "setting and when all three beakers have\nfrozen enter "
                        + "the answers in the table and click the\ncheck answers "
                        + "button.\n\nUsing the Freeze Point Depression information"
                        + ",\navailable by clicking the Assistance button, and the\n"
                        + "information in the table at the bottom of the screen\n"
                        + "you will be able to calculate the van't hoff factor (i)\n"
                        + "and the molality (m) and enter them into the table.\n\n"
                        + "You can then check your answers by using the check\n"
                        + "answer button.";
                        

                //make text field for content 
                helpTextArea = new JTextArea(helpContent);

                //set font for text area 
                helpTextArea.setFont(new Font("Verdana", Font.BOLD, 12));

                //set foreground color for label 
                helpTextArea.setForeground(Color.white);

                //set background for text area 
                helpTextArea.setBackground(new Color(12, 66, 116));

                //set size of text area 
                helpTextArea.setSize(400, 550);

                //set text area to word wrap 
                helpTextArea.setWrapStyleWord(true);

                //make empty border for padding 
                Border emptyBorder = BorderFactory.createEmptyBorder(20, 20, 20, 20);

                //set border for text area 
                helpTextArea.setBorder(emptyBorder);
                
                //set text area to not editable
                helpTextArea.setEditable(false);

                if(helpDialog == null){
                    //make joptionpane object 
                    helpDialog = new JDialog(helpDialog, help);

                    //set layout for dialog 
                    helpDialog.setLayout(new BorderLayout());

                    //add textfield to helpDialog 
                    helpDialog.add(helpTextArea, BorderLayout.CENTER);

                    //set dialog to resizable 
                    helpDialog.setResizable(false);

                    //get width and height of text area 
                    int helpWidth = helpTextArea.getWidth();
                    int helpHeight = helpTextArea.getHeight();

                    //set dialog size 
                    helpDialog.setSize(helpWidth, helpHeight);

                    //make dialog visible 
                    helpDialog.setVisible(true);
                }
                else{
                    helpDialog.requestFocus();
                }
                
                if(!helpDialog.isShowing() && helpDialog != null){
                    //make dialog visible 
                    helpDialog.setVisible(true);
                }
            }
        });
        
        //listener to start mix using particleFill method from GasChamber  
        knownComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                int solutes = 0;
                
                switch(knownComboBox.getSelectedIndex()){
                    case 0:
                        solutes = 3;
                        beaker2 = "-7.54\u2070C";
                        break;
                    case 1:
                        solutes = 5;
                        beaker2 = "-11.73\u2070C";
                        break;
                    case 2:
                        solutes = 7;
                        beaker2 = "-17.87\u2070C";
                }
                
                table.setValueAt("", 1, 2);
                table.setValueAt("", 2, 2);
                
                bt.setNumSolutes1(solutes);

                bt.requestFocus();
            }
        });
        
        //create a listener for the assist button 
        assistButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                bt.requestFocus();
                //make icon object and reference the periodic table image 
                final Icon icon = new javax.swing.ImageIcon(getClass().getResource("FreezePointDepressionAssistance.png"));

                //make string title variable 
                periodic = "Free Point Depression Assistance";

                //make width and height the icon's width and height 
                width = icon.getIconWidth();
                height = icon.getIconHeight();

                //make jpanel for periodic table image 
                periodicPanel = new JPanel();

                //set size of periodicPanel to match image 
                periodicPanel.setSize(width, height);

                //make image label 
                imageLabel = new JLabel(icon);

                if(dialog == null){
                    //make jdialog with title 
                    dialog = new JDialog(dialog, periodic);

                    //set size of dialog to match image 
                    dialog.setSize(width, height);

                    //add imagelabel to dialog 
                    dialog.add(imageLabel);

                    //set dialog to visible 
                    dialog.setVisible(true);
                }
                else{
                    dialog.requestFocus();
                }
                
                if(!dialog.isShowing()){
                    //set dialog to visible 
                    dialog.setVisible(true);
                }
            }
        });
        
        // listener for reset button 
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                knownComboBox.setSelectedIndex(0);
                unknownComboBox.setSelectedIndex(0);
                clearTable(table);
                slider.setValue(40);
                table.setEnabled(false);

                //stops editing table and saves current data
                if (table.isEditing()){
                    table.getCellEditor().stopCellEditing();
                }
                table.setValueAt(DELTA, 0, 0);
                table.setValueAt(DEGREE, 1, 0);
                table.setValueAt(MOLALITY, 2, 0);
                table.setValueAt(WATER_CONSTANT, 3, 0);
                table.setValueAt(0, 1, 1);
                table.setValueAt(0, 2, 1);
                setValuesHidden();
                table.setValueAt(WATER_FREEZE, 3, 1);
                table.setValueAt(WATER_FREEZE, 3, 2);
                table.setValueAt(WATER_FREEZE, 3, 3);
                table.setValueAt("", 1, 2);
                table.setValueAt("", 1, 3);
                correctLabel.setVisible(false);
                correctLabel2.setVisible(false);
                incorrectLabel.setVisible(false);
                incorrectLabel2.setVisible(false);
                bt.loop();
                bt.requestFocus();
                
            }
        });
        
        // listener to update frameRate in GasChamber 
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int value = slider.getValue();
                float cent = (float)Math.sqrt((((value - 90) * -1) / 100.0f) * (((value - 90) * -1) / 100.0f) / 200);
                
                float t = Math.abs((value - 100) / 100.0f);
                
                cent *= t;
                cent -= .02f;
                
                float flit = ((value + 20) / 100.0f);
                
                if(flit < .2){
                    knownComboBox.setEnabled(false);
                    unknownComboBox.setEnabled(false);
                }else{
                    knownComboBox.setEnabled(true);
                    unknownComboBox.setEnabled(true);
                    bt.loop();
                }
                
                float negStr = (1 - flit) * 4f;
//                bt.setFRate(value);
                bt.setCenterStr(cent);
                bt.setFlit(flit);
                bt.setNegStr(negStr);                
                System.out.println("Value: " + value);
                System.out.println("NegStr: " + negStr);
                System.out.println("Flit: " + flit);
                System.out.println("CenterStr: " + bt.getCenterStr() + "\n");
                bt.requestFocus();
            }
        });
        
        // listener to start mix using particleFill method from GasChamber 
        unknownComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                int solutes = 0;
                
                
                switch(unknownComboBox.getSelectedIndex()){
                    case 0:
                        solutes = 9;
                        beaker3 = "-22.36\u2070C";
                        break;
                    case 1:
                        solutes = 11;
                        beaker3 = "-25.18\u2070C";
                        break;
                    case 2:
                        solutes = 13;
                        beaker3 = "-32.67\u2070C";
                }
                
                
                table.setValueAt("", 1, 3);
                table.setValueAt("", 2, 3);
                
                bt.setNumSolutes2(solutes);

                bt.requestFocus();
            }
        });
        
        getContentPane().add(pane);
    }

    /**
     * clears table of user input data
     */
    public static void clearTable(final JTable table) {
        for (int i = 0; i < table.getRowCount(); i++) {
            for (int j = 0; j < table.getColumnCount(); j++) {
                table.setValueAt("", i, j);
            }
        }
    }
     
    @Override
    public void start(){

        knownComboBox.setSelectedIndex(0);
        unknownComboBox.setSelectedIndex(0);
        clearTable(table);
        slider.setValue(40);
        table.setEnabled(false);

        //stops editing table and saves current data
        if (table.isEditing()){
            table.getCellEditor().stopCellEditing();
        }
        table.setValueAt(DELTA, 0, 0);
        table.setValueAt(DEGREE, 1, 0);
        table.setValueAt(MOLALITY, 2, 0);
        table.setValueAt(WATER_CONSTANT, 3, 0);
        table.setValueAt(0, 1, 1);
        table.setValueAt(0, 2, 1);
        setValuesHidden();
        table.setValueAt(WATER_FREEZE, 3, 1);
        table.setValueAt(WATER_FREEZE, 3, 2);
        table.setValueAt(WATER_FREEZE, 3, 3);
        table.setValueAt("", 1, 2);
        table.setValueAt("", 1, 3);
        correctLabel.setVisible(false);
        correctLabel2.setVisible(false);
        incorrectLabel.setVisible(false);
        incorrectLabel2.setVisible(false);
        bt.requestFocus();
    }
    
    @Override
    public void destroy(){
        bt.destroy();
        this.destroy();
    }
    
    public void setTableEditable(){
        table.setEnabled(true);
        
        table.isCellEditable(1, 2);
        table.isCellEditable(1, 3);
        table.isCellEditable(2, 2);
        table.isCellEditable(2, 3);

    }
    
    public void setTableNotEditable(){
        table.setEnabled(false);
    }
    
    public void setValuesVisible(){
        table.setValueAt(beaker1, 0, 1);
        table.setValueAt(beaker2, 0, 2);
        table.setValueAt(beaker3, 0, 3);
    }
    
    public void setValuesHidden(){
        table.setValueAt("", 0, 1);
        table.setValueAt("", 0, 2);
        table.setValueAt("", 0, 3);
    }
}
