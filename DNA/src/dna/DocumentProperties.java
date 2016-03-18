package dna;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;

import org.jdesktop.swingx.JXComboBox;
import org.jdesktop.swingx.JXTextArea;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

import dna.dataStructures.Document;
import dna.renderer.CoderComboBoxRenderer;

public class DocumentProperties extends JFrame {
	
	private static final long serialVersionUID = 1L;
	String dbfile, title, type, source, section, notes;
	int coder;
	Date date;
	SpinnerDateModel dateModel;
	JSpinner dateSpinner;
	JButton okButton;
	JPanel newArticlePanel;
	JTextField titleField;
	JXTextArea notesArea;
	JXComboBox coderBox, sourceBox, sectionBox, typeBox;
	
	public DocumentProperties(final int documentId) {
		
		this.setTitle("Edit document metadata...");
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		ImageIcon tableEditIcon = new ImageIcon(getClass().getResource(
				"/icons/table_edit.png"));
		this.setIconImage(tableEditIcon.getImage());
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		//Document d = Dna.dna.db.getDocument(documentId);
		Document d = Dna.data.getDocument(documentId);
		this.title = d.getTitle();
		this.date = d.getDate();
		this.coder = d.getCoder();
		this.source = d.getSource();
		this.section = d.getSection();
		this.type = d.getType();
		this.notes = d.getNotes();
		
		JPanel fieldsPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(1, 0, 1, 5);
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		JLabel titleLabel = new JLabel("title", JLabel.RIGHT);
		fieldsPanel.add(titleLabel, gbc);
		
		gbc.gridx++;
		gbc.gridwidth = 2;
		titleField = new JTextField(this.title);
		titleField.setColumns(50);
		fieldsPanel.add(titleField, gbc);
		
		gbc.gridx = 3;
		gbc.gridwidth = 1;
		gbc.insets = new Insets(1, 0, 1, 0);
		Icon okIcon = new ImageIcon(getClass().getResource("/icons/tick.png"));
		okButton = new JButton(okIcon);
		okButton.setToolTipText( "modify the metadata of the current " +
				"document based on the information you entered in this " +
				"window" );
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String title = titleField.getText();
				Date date = (Date)dateSpinner.getValue();
				int coder = (int) coderBox.getModel().getSelectedItem();
				String source = (String) sourceBox.getModel().getSelectedItem();
				String section = (String) sectionBox.getModel().
						getSelectedItem();
				String notes = notesArea.getText();
				String type = (String) typeBox.getModel().getSelectedItem();
				//Dna.dna.db.changeDocument(documentId, title, date, coder, source, section, notes, type);
				Dna.data.getDocument(documentId).setTitle(title);
				Dna.data.getDocument(documentId).setDate(date);
				Dna.data.getDocument(documentId).setCoder(coder);
				Dna.data.getDocument(documentId).setSource(source);
				Dna.data.getDocument(documentId).setSection(section);
				Dna.data.getDocument(documentId).setNotes(notes);
				Dna.dna.gui.documentPanel.documentContainer.changeDocument(
						documentId, title, date, coder, source, section, notes, type);
				Dna.dna.gui.documentPanel.documentContainer.sort();
				int newRow = Dna.dna.gui.documentPanel.documentContainer.
						getRowIndexById(documentId);
				Dna.dna.gui.documentPanel.documentTable.updateUI();
				Dna.dna.gui.documentPanel.documentTable.getSelectionModel().
						setSelectionInterval(newRow, newRow);
				dispose();
			}
		});
		fieldsPanel.add(okButton, gbc);
		
		gbc.gridx = 0;
		gbc.gridy++;
		gbc.insets = new Insets(1, 0, 1, 5);
		JLabel dateLabel = new JLabel("date", JLabel.RIGHT);
		fieldsPanel.add(dateLabel, gbc);
		
		gbc.gridx++;
		dateModel = new SpinnerDateModel();
		dateSpinner = new JSpinner();
		dateModel.setCalendarField( Calendar.DAY_OF_YEAR );
		dateSpinner.setModel( dateModel );
		dateModel.setValue(date);
		dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, 
				"yyyy-MM-dd  HH:mm:ss"));
		fieldsPanel.add(dateSpinner, gbc);
		
		gbc.gridy++;
		gbc.gridx--;
		JLabel coderLabel = new JLabel("coder", JLabel.RIGHT);
		fieldsPanel.add(coderLabel, gbc);
		
		gbc.gridx++;
		coderBox = new JXComboBox(Dna.data.getCoders().toArray());
		CoderComboBoxRenderer coderRenderer = new CoderComboBoxRenderer();
		coderBox.setRenderer(coderRenderer);
		coderBox.setEditable(true);
		coderBox.setSelectedItem(this.coder);
		AutoCompleteDecorator.decorate(coderBox);
		fieldsPanel.add(coderBox, gbc);
		
		gbc.gridy++;
		gbc.gridx--;
		JLabel sourceLabel = new JLabel("source", JLabel.RIGHT);
		fieldsPanel.add(sourceLabel, gbc);
		
		gbc.gridx++;
		ArrayList<String> sourceEntries = new ArrayList<String>();
		for (int i = 0; i < Dna.data.getDocuments().size(); i++) {
			if (!sourceEntries.contains(Dna.data.getDocuments().get(i).getSource())) {
				sourceEntries.add(Dna.data.getDocuments().get(i).getSource());
			}
		}
		Collections.sort(sourceEntries);
		sourceBox = new JXComboBox(sourceEntries.toArray());
		sourceBox.setEditable(true);
		sourceBox.setSelectedItem(this.source);
		AutoCompleteDecorator.decorate(sourceBox);
		fieldsPanel.add(sourceBox, gbc);

		gbc.gridy++;
		gbc.gridx--;
		JLabel sectionLabel = new JLabel("section", JLabel.RIGHT);
		fieldsPanel.add(sectionLabel, gbc);
		
		gbc.gridx++;
		//String[] sectionEntries = Dna.dna.db.getDocumentSections();
		ArrayList<String> sectionEntries = new ArrayList<String>();
		for (int i = 0; i < Dna.data.getDocuments().size(); i++) {
			if (!sectionEntries.contains(Dna.data.getDocuments().get(i).getSection())) {
				sectionEntries.add(Dna.data.getDocuments().get(i).getSection());
			}
		}
		Collections.sort(sectionEntries);
		sectionBox = new JXComboBox(sectionEntries.toArray());
		sectionBox.setEditable(true);
		sectionBox.setSelectedItem(this.section);
		AutoCompleteDecorator.decorate(sectionBox);
		fieldsPanel.add(sectionBox, gbc);
		
		gbc.gridy++;
		gbc.gridx--;
		gbc.insets = new Insets(1, 0, 3, 5);
		JLabel typeLabel = new JLabel("type", JLabel.RIGHT);
		fieldsPanel.add(typeLabel, gbc);
		
		gbc.gridx++;
		//String[] typeEntries = Dna.dna.db.getDocumentTypes();
		ArrayList<String> typeEntries = new ArrayList<String>();
		for (int i = 0; i < Dna.data.getDocuments().size(); i++) {
			if (!typeEntries.contains(Dna.data.getDocuments().get(i).getType())) {
				typeEntries.add(Dna.data.getDocuments().get(i).getType());
			}
		}
		Collections.sort(typeEntries);
		typeBox = new JXComboBox(typeEntries.toArray());
		typeBox.setEditable(true);
		typeBox.setSelectedItem(this.type);
		AutoCompleteDecorator.decorate(typeBox);
		fieldsPanel.add(typeBox, gbc);
		
		gbc.gridy = 1;
		gbc.gridx = 2;
		gbc.gridheight = 5;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(1, 0, 2, 0);
		notesArea = new JXTextArea("notes...");
		notesArea.setText(this.notes);
		notesArea.setBorder(titleField.getBorder());
		fieldsPanel.add(notesArea, gbc);
		
		newArticlePanel = new JPanel(new BorderLayout());
		newArticlePanel.add(fieldsPanel, BorderLayout.CENTER);
		
		this.add(newArticlePanel);
		this.pack();
		dateSpinner.grabFocus();
		okButton.setPreferredSize(new Dimension(81, titleField.getHeight()));
		dateSpinner.setPreferredSize(new Dimension(170, titleField.getHeight()));
		typeBox.setPreferredSize(new Dimension(170, titleField.getHeight()));
		sourceBox.setPreferredSize(new Dimension(170, titleField.getHeight()));
		sectionBox.setPreferredSize(new Dimension(170, titleField.getHeight()));
		coderBox.setPreferredSize(new Dimension(170, titleField.getHeight()));
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.pack();
	}
}