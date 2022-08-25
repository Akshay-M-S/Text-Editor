import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.*;

public class test extends JFrame implements ActionListener{
//JFrame works like the main window where components like labels, buttons, textfields are added to create a GUI.
//ActionListener: to define what should be done when an user performs certain operation
 JTextArea textArea; //this class is a multi-line region that displays text. Allows editing of multiple line texts.
 JScrollPane scrollPane; //to make scrollable view of a component
 JLabel fontLabel;	//used to display a short string or an image icon
 JSpinner fontSizeSpinner; //for font size spinner
 JButton fontColorButton;	//text color button
 JComboBox fontBox;	//including all the possible font types
 JFrame f;	//for GUI
 
 JMenuBar menuBar;
 JMenu fileMenu;
 JMenu editMenu;
 JMenuItem newItem;
 JMenuItem openItem;
 JMenuItem saveItem;
 JMenuItem printItem;
 
 JMenuItem cutItem;
 JMenuItem copyItem;
 JMenuItem pasteItem;
 JMenuItem Close;

 test(){//constructor
  this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  this.setTitle("Text Editor");
  this.setSize(500, 500); //size of the window
  this.setLayout(new FlowLayout());//FlowLayout() class: to arrange the components in a line, one after another (in a flow)
  //this.setLocationRelativeTo(null);
  
  textArea = new JTextArea();
  textArea.setLineWrap(true);
  textArea.setWrapStyleWord(true);
  textArea.setFont(new Font("Ariel",Font.PLAIN,20));//default font setting
  
  scrollPane = new JScrollPane(textArea);//scrollable view of text
  scrollPane.setPreferredSize(new Dimension(450,450)); //size of window
  scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);//setting vertical scrollbar
  
  fontLabel = new JLabel("Font: ");
  
  fontSizeSpinner = new JSpinner(); //font spinner
  fontSizeSpinner.setPreferredSize(new Dimension(50,25));
  fontSizeSpinner.setValue(20);// default
  fontSizeSpinner.addChangeListener(new ChangeListener() {

   @Override
   public void stateChanged(ChangeEvent e) {
    
    textArea.setFont(new Font(textArea.getFont().getFamily(),Font.PLAIN,(int) fontSizeSpinner.getValue())); 
   }
   
  });
  
  fontColorButton = new JButton("Color");
  fontColorButton.addActionListener(this);
  
  String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();//import fonts library
  
  fontBox = new JComboBox(fonts);
  fontBox.addActionListener(this);
  fontBox.setSelectedItem("Arial");
  
  // ------ menubar ------
  
   menuBar = new JMenuBar();
   newItem = new JMenuItem("New");
   fileMenu = new JMenu("File");
   openItem = new JMenuItem("Open");
   saveItem = new JMenuItem("Save");
   printItem = new JMenuItem("print");
   
   newItem.addActionListener(this);
   openItem.addActionListener(this);
   saveItem.addActionListener(this);
   printItem.addActionListener(this);
   
   fileMenu.add(newItem);
   fileMenu.add(openItem);
   fileMenu.add(saveItem);
   fileMenu.add(printItem);
   menuBar.add(fileMenu);
  
// Create amenu for menu
    editMenu = new JMenu("Edit");

   // Create menu items
    cutItem = new JMenuItem("cut");
    copyItem = new JMenuItem("copy");
    pasteItem = new JMenuItem("paste");

   // Add action listener
   cutItem.addActionListener(this);
   copyItem.addActionListener(this);
   pasteItem.addActionListener(this);

   editMenu.add(cutItem);
   editMenu.add(copyItem);
   editMenu.add(pasteItem);
   
   Close = new JMenuItem("close");

   Close.addActionListener(this);
   
   menuBar.add(editMenu);
   menuBar.add(Close);
  // ------ /menubar ------
   
  this.setJMenuBar(menuBar);
  this.add(fontLabel);
  this.add(fontSizeSpinner);
  this.add(fontColorButton);
  this.add(fontBox);
  this.add(scrollPane);
  this.setVisible(true);
 }
 
 @Override
 public void actionPerformed(ActionEvent e) {

     if (e.getSource()==cutItem) {
         textArea.cut();
     }
     else if (e.getSource()==copyItem) {
         textArea.copy();
     }
     else if (e.getSource()==pasteItem) {
         //textArea.paste();
    	 onPaste();

     }
     else if(e.getSource()==newItem) {
    	 textArea.setText("");
     }
     else if (e.getSource()==Close) {
    	 System.exit(0);//JFrame.setVisible(false);
     }
     
  if(e.getSource()==fontColorButton) {
   JColorChooser colorChooser = new JColorChooser();
   
   Color color = colorChooser.showDialog(null, "Choose a color", Color.black);
   
   textArea.setForeground(color);
  }
  
  if(e.getSource()==fontBox) {
   textArea.setFont(new Font((String)fontBox.getSelectedItem(),Font.PLAIN,textArea.getFont().getSize()));
  }
  
  if(e.getSource()==openItem) {
   JFileChooser fileChooser = new JFileChooser();
   fileChooser.setCurrentDirectory(new File("."));
   FileNameExtensionFilter filter = new FileNameExtensionFilter("Text files", "txt");
   fileChooser.setFileFilter(filter);
   
   int response = fileChooser.showOpenDialog(null);
   
   if(response == JFileChooser.APPROVE_OPTION) {
    File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
    Scanner fileIn = null;
    
    try {
     fileIn = new Scanner(file);
     if(file.isFile()) {
      while(fileIn.hasNextLine()) {
       String line = fileIn.nextLine()+"\n";
       textArea.append(line);
      }
     }
    } catch (FileNotFoundException e1) {
     e1.printStackTrace();
    }
    finally {
     fileIn.close();
    }
   }
  }
  if(e.getSource()==saveItem) {
   JFileChooser fileChooser = new JFileChooser();
   fileChooser.setCurrentDirectory(new File("."));
   
   int response = fileChooser.showSaveDialog(null);
   
   if(response == JFileChooser.APPROVE_OPTION) {
    File file;
    PrintWriter fileOut = null;
    
    file = new File(fileChooser.getSelectedFile().getAbsolutePath());
    try {
     fileOut = new PrintWriter(file);
     fileOut.println(textArea.getText());
    } 
    catch (FileNotFoundException e1) {
     e1.printStackTrace();
    }
    finally {
     fileOut.close();
    }   
   }
  }
  if(e.getSource()==printItem) {
	  try {
          // print the file
          textArea.print();
      }
      catch (Exception evt) {
          JOptionPane.showMessageDialog(f, evt.getMessage());
      }

  }  
 }
 private void onPaste(){
	    Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
	    Transferable t = c.getContents(this);
	    if (t == null)
	        return;
	    try {
	        textArea.append((String) t.getTransferData(DataFlavor.stringFlavor));
	    } catch (Exception e){
	        e.printStackTrace();
	    }//try
	}//onPaste
 public static void main(String[] args) {
		// TODO Auto-generated method stub
		new test();
	}
}
