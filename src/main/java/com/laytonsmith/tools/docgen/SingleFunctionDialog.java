package com.laytonsmith.tools.docgen;

import com.laytonsmith.PureUtilities.Common.UIUtils;
import com.laytonsmith.core.constructs.Target;
import com.laytonsmith.core.exceptions.ConfigCompileException;
import com.laytonsmith.core.functions.Function;
import com.laytonsmith.core.functions.FunctionList;

/**
 *
 *
 */
public class SingleFunctionDialog extends javax.swing.JDialog {

	DocGenUI parent;

	/**
	 * Creates new form SingleFunctionDialog
	 */
	public SingleFunctionDialog(DocGenUI parent) {
		super(parent, true);
		this.parent = parent;
		initComponents();
		UIUtils.centerWindowOnWindow(this, parent);
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        functionNameField = new javax.swing.JTextField();
        uploadButton = new javax.swing.JButton();
        statusLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Function:");

        uploadButton.setText("Upload");
        uploadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                uploadButtonActionPerformed(evt);
            }
        });

        statusLabel.setText("Waiting...");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(functionNameField))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(uploadButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 222, Short.MAX_VALUE)
                        .addComponent(statusLabel)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(functionNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(uploadButton)
                    .addComponent(statusLabel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void uploadButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uploadButtonActionPerformed
		Function f;
		try {
			f = (Function)FunctionList.getFunction(functionNameField.getText(), Target.UNKNOWN);
			if(!f.appearInDocumentation()){
				statusLabel.setText("That function is not configured to have documentation.");
				return;
			}
			try {
				String docs = DocGen.examples(f.getName(), parent.handler.isStaged);
				parent.handler.doUpload(docs, "/API/" + f.getName(), true);
			} catch (Exception ex) {
				statusLabel.setText(ex.getMessage());
				ex.printStackTrace(System.err);
			}
		} catch (ConfigCompileException ex) {
			statusLabel.setText("That function doesn't exist.");
			return;
		}


    }//GEN-LAST:event_uploadButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField functionNameField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JButton uploadButton;
    // End of variables declaration//GEN-END:variables
}
