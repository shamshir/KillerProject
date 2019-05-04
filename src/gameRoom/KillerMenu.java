package gameRoom;

/**
 *
 * @author Yeray
 */
public class KillerMenu extends javax.swing.JFrame {

    /**
     * Creates new form KillerMenu
     */
    public KillerMenu() {
        initComponents();
        this.setLocationRelativeTo(null); 
        jButtonJugar.setEnabled(false);
    }

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabelTitulo = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButtonJugar = new javax.swing.JButton();
        jButtonConectar = new javax.swing.JButton();
        jButtonAjustes = new javax.swing.JButton();
        jButtonSalir = new javax.swing.JButton();
        jLabelCopy = new javax.swing.JLabel();
        jLabelFondo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(525, 525));
        setMinimumSize(new java.awt.Dimension(525, 525));
        setPreferredSize(new java.awt.Dimension(525, 525));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        getContentPane().add(jLabelTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 100, -1, -1));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gameRoom/img/Logo.png"))); // NOI18N
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 0, -1, -1));

        jButtonJugar.setText("JUGAR");
        jButtonJugar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonJugarActionPerformed(evt);
            }
        });
        getContentPane().add(jButtonJugar, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 260, 100, 30));

        jButtonConectar.setText("CONECTAR");
        jButtonConectar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonConectarActionPerformed(evt);
            }
        });
        getContentPane().add(jButtonConectar, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 310, 100, 30));

        jButtonAjustes.setText("AJUSTES");
        jButtonAjustes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAjustesActionPerformed(evt);
            }
        });
        getContentPane().add(jButtonAjustes, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 360, 100, 30));

        jButtonSalir.setText("SALIR");
        jButtonSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSalirActionPerformed(evt);
            }
        });
        getContentPane().add(jButtonSalir, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 410, 100, 30));

        jLabelCopy.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelCopy.setForeground(new java.awt.Color(255, 255, 255));
        jLabelCopy.setText("KillerTeam ®");
        getContentPane().add(jLabelCopy, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 510, -1, -1));

        jLabelFondo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gameRoom/img/stars.gif"))); // NOI18N
        getContentPane().add(jLabelFondo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 525, 525));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonJugarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonJugarActionPerformed
        // Cerrar conexiones
        // Iniciar juego (añadir optionpane o similar con contador 3, 2 ,1)
    }//GEN-LAST:event_jButtonJugarActionPerformed

    private void jButtonConectarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonConectarActionPerformed
        // Llamar metodo ipframe() de KillerGame.java
        //Podria manejar los metodos de KillerGame directamente en lugar de lo anterior
        jButtonJugar.setEnabled(true);
    }//GEN-LAST:event_jButtonConectarActionPerformed

    private void jButtonAjustesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAjustesActionPerformed
        // Discutir que ajustes añadir
    }//GEN-LAST:event_jButtonAjustesActionPerformed

    private void jButtonSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSalirActionPerformed
        // Cerrar aplicación
        System.exit(0);
    }//GEN-LAST:event_jButtonSalirActionPerformed

    
    public static void main(String args[]) {
        
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(KillerMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(KillerMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(KillerMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(KillerMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new KillerMenu().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAjustes;
    private javax.swing.JButton jButtonConectar;
    private javax.swing.JButton jButtonJugar;
    private javax.swing.JButton jButtonSalir;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabelCopy;
    private javax.swing.JLabel jLabelFondo;
    private javax.swing.JLabel jLabelTitulo;
    // End of variables declaration//GEN-END:variables
}
