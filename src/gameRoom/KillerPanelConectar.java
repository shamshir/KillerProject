/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameRoom;



/**
 *
 * @author Yeray
 */
public class KillerPanelConectar extends javax.swing.JPanel {
    //Variables conexión
     private String ipIzq;
     private String ipDer;
     private int portIzq;
     private int portDer;
     
     //Variables jPanel
     KillerPanelPrincipal kpp;
     
     
    /**
     * Creates new form NewJPanelConectar
     */
    public KillerPanelConectar(KillerPanelPrincipal kpp) {
        this.kpp = kpp;
        initComponents();
    }
    
    public KillerPanelConectar getKillerPanelConectar(){
        return this;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabelTitulo = new javax.swing.JLabel();
        jLabelCopy = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabelIpIzq = new javax.swing.JLabel();
        jLabelPortIzq = new javax.swing.JLabel();
        jTextFieldIpIzq = new javax.swing.JTextField();
        jLabel1IpDer = new javax.swing.JLabel();
        jTextFieldIpDer = new javax.swing.JTextField();
        jLabel1PortDer = new javax.swing.JLabel();
        jTextFieldPortDer = new javax.swing.JTextField();
        jTextFieldPortIzq = new javax.swing.JTextField();
        jLabelFondo = new javax.swing.JLabel();

        setMaximumSize(new java.awt.Dimension(525, 525));
        setMinimumSize(new java.awt.Dimension(525, 525));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabelTitulo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gameRoom/img/Logo.png"))); // NOI18N
        jLabelTitulo.setText("jLabel1");
        jLabelTitulo.setPreferredSize(new java.awt.Dimension(480, 80));
        add(jLabelTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, -1, -1));

        jLabelCopy.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelCopy.setForeground(new java.awt.Color(255, 255, 255));
        jLabelCopy.setText("KillerTeam ®");
        add(jLabelCopy, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 510, -1, -1));

        jButton1.setText("VOLVER");
        jButton1.setPreferredSize(new java.awt.Dimension(100, 30));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 370, -1, -1));

        jButton2.setText("CONECTAR");
        jButton2.setPreferredSize(new java.awt.Dimension(100, 30));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 320, -1, -1));

        jLabelIpIzq.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelIpIzq.setForeground(new java.awt.Color(255, 255, 255));
        jLabelIpIzq.setText("IP izquierda");
        add(jLabelIpIzq, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, -1, -1));

        jLabelPortIzq.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelPortIzq.setForeground(new java.awt.Color(255, 255, 255));
        jLabelPortIzq.setText("Puerto izquierda");
        add(jLabelPortIzq, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 230, -1, -1));

        jTextFieldIpIzq.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldIpIzq.setBorder(null);
        jTextFieldIpIzq.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        jTextFieldIpIzq.setPreferredSize(new java.awt.Dimension(90, 20));
        add(jTextFieldIpIzq, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, -1, -1));

        jLabel1IpDer.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1IpDer.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1IpDer.setText("IP derecha");
        add(jLabel1IpDer, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 170, -1, -1));

        jTextFieldIpDer.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldIpDer.setPreferredSize(new java.awt.Dimension(90, 20));
        add(jTextFieldIpDer, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 190, -1, -1));

        jLabel1PortDer.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1PortDer.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1PortDer.setText("Puerto derecha");
        add(jLabel1PortDer, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 230, -1, -1));

        jTextFieldPortDer.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldPortDer.setPreferredSize(new java.awt.Dimension(40, 20));
        add(jTextFieldPortDer, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 250, -1, -1));

        jTextFieldPortIzq.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldPortIzq.setPreferredSize(new java.awt.Dimension(40, 20));
        add(jTextFieldPortIzq, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 250, -1, -1));

        jLabelFondo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gameRoom/img/stars.gif"))); // NOI18N
        add(jLabelFondo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // CONECTAR EN PANEL CONECTAR
        ipIzq = jTextFieldIpIzq.getText();
        portIzq = Integer.parseInt(jTextFieldPortIzq.getText());
        ipDer = jTextFieldIpDer.getText();
        portDer = Integer.parseInt(jTextFieldPortDer.getText());
        kg.setPortPrev();
        kg.setPortNext();
        kg.setIpPrev();
        kg.setIpNext();

    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // VOLVER
        this.setVisible(false);
        kpp.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1IpDer;
    private javax.swing.JLabel jLabel1PortDer;
    private javax.swing.JLabel jLabelCopy;
    private javax.swing.JLabel jLabelFondo;
    private javax.swing.JLabel jLabelIpIzq;
    private javax.swing.JLabel jLabelPortIzq;
    private javax.swing.JLabel jLabelTitulo;
    private javax.swing.JTextField jTextFieldIpDer;
    private javax.swing.JTextField jTextFieldIpIzq;
    private javax.swing.JTextField jTextFieldPortDer;
    private javax.swing.JTextField jTextFieldPortIzq;
    // End of variables declaration//GEN-END:variables
}
