package gameRoom;

import communications.KillerPad;
import game.KillerGame;
import java.awt.Color;
import java.awt.Font;
import java.net.Inet4Address;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import sound.KillerSound;
import visibleObjects.KillerShip;

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

    //Variable Killer Game
    private KillerRoom kr;
    private KillerGame kg;
    
    //Strings
    private String player;
    
    //Tabla
    DefaultTableModel model;

    /**
     * Creates new form NewJPanelConectar
     */
    public KillerPanelConectar(KillerRoom kr) {
        this.kr = kr;
        initComponents();
        kg = kr.getKg();
        setTextData();
        configJTable();
    }

    /**
     * Método llamado por KillerGame Recibe la lista de jugadores y la imprime
     *
     * @param players
     */
    public void updateUsers(Hashtable<String, KillerPad> players) {
        model.setRowCount(0);
        Enumeration<KillerPad> enumeration = players.elements();
        while (enumeration.hasMoreElements()) {
            player = enumeration.nextElement().getUser();
            updateJTable(player);
        }
    }
    
    /**
     * Método que actualiza la jTable de jugadores conectados
     * @param player 
     */
    public void updateJTable(String player){
        Vector row = new Vector();
        row.add(player);
        model.addRow(row);
    }
    
    /**
     * Método que configura la jTable, jScrollPanel que la contiene,
     * el modelo de la tabla y el título de esta.
     */
    public void configJTable(){
        model = (DefaultTableModel) jTablePlayers.getModel();
        jTablePlayers.setBackground(new Color(0, 0, 0, 0));
        ((DefaultTableCellRenderer) jTablePlayers.getDefaultRenderer(Object.class)).setBackground(new Color(0, 0, 0, 0));
        jTablePlayers.setGridColor(new Color(255,204,0, 255));
        jTablePlayers.setForeground(new Color(255,204,0, 255));
        jScrollPanePlayers.setOpaque(false);
        jTablePlayers.setOpaque(false);
        ((DefaultTableCellRenderer) jTablePlayers.getDefaultRenderer(Object.class)).setOpaque(false);
        jScrollPanePlayers.getViewport().setOpaque(false);
        ((DefaultTableCellRenderer) jTablePlayers.getDefaultRenderer(Object.class)).setHorizontalAlignment( JLabel.CENTER );
        jTablePlayers.setShowGrid(false);
        jTablePlayers.getTableHeader().setReorderingAllowed(false);
        jTablePlayers.getTableHeader().setForeground(new Color(255,204,0, 255));
        jTablePlayers.getTableHeader().setBackground(new Color(0, 0,0, 0));
        jTablePlayers.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 12));
        jTablePlayers.getTableHeader().setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 204, 0)));
        ((DefaultTableCellRenderer) jTablePlayers.getDefaultRenderer(Object.class)).setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 204, 0)));
        jScrollPanePlayers.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 204, 0)));
    }
    
    /**
     * Método que pinta datos de conexión locales a modo de ejemplo y ayuda
     */
    public void setTextData() {
        try {
            jTextFieldIpIzq.setText(Inet4Address.getLocalHost().getHostAddress());
            jTextFieldIpDer.setText(Inet4Address.getLocalHost().getHostAddress());
            jTextFieldPortIzq.setText("8000");
            jTextFieldPortDer.setText("8000");
        } catch (Exception e) {
        }
    }

    /**
     * Cambia el texto e imagen que dice si esta conectado ese lado izquierdo de
     * la pantalla
     *
     * @param aux
     */
    public void setFeedbackConnetionLeft2(Boolean aux) {
        if (aux == true) {
            jLabelConFeedackL.setForeground(Color.GREEN);
            jLabelConFeedackL.setText("CONECTADO");
            jLabelConFeedackL.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gameRoom/img/connected.png")));
        } else {
            jLabelConFeedackL.setForeground(Color.RED);
            jLabelConFeedackL.setText("DESCONECTADO");
            jLabelConFeedackL.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gameRoom/img/disconnected.png")));
        }
    }

    /**
     * Cambia el texto e imagen que dice si esta conectado ese lado derecho de
     * la pantalla
     *
     * @param aux
     */
    public void setFeedbackConnetionRight2(Boolean aux) {
        if (aux == true) {
            jLabelConFeedackR.setForeground(Color.GREEN);
            jLabelConFeedackR.setText("CONECTADO");
            jLabelConFeedackR.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gameRoom/img/connected.png")));
        } else {
            jLabelConFeedackR.setForeground(Color.RED);
            jLabelConFeedackR.setText("DESCONECTADO");
            jLabelConFeedackR.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gameRoom/img/disconnected.png")));
        }
    }

    /**
     * Getter de la clase
     *
     * @return
     */
    public KillerPanelConectar getKillerPanelConectar() {
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
        jButtonConectarIzq = new javax.swing.JButton();
        jLabelIpIzq = new javax.swing.JLabel();
        jLabelPortIzq = new javax.swing.JLabel();
        jTextFieldIpIzq = new javax.swing.JTextField();
        jScrollPanePlayers = new javax.swing.JScrollPane();
        jTablePlayers = new javax.swing.JTable();
        jLabel1IpDer = new javax.swing.JLabel();
        jTextFieldIpDer = new javax.swing.JTextField();
        jLabel1PortDer = new javax.swing.JLabel();
        jTextFieldPortDer = new javax.swing.JTextField();
        jTextFieldPortIzq = new javax.swing.JTextField();
        jButtonConectarDer = new javax.swing.JButton();
        jLabelConFeedackR = new javax.swing.JLabel();
        jLabelConFeedackL = new javax.swing.JLabel();
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

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gameRoom/img/Volver.png"))); // NOI18N
        jButton1.setBorderPainted(false);
        jButton1.setContentAreaFilled(false);
        jButton1.setPreferredSize(new java.awt.Dimension(100, 30));
        jButton1.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/gameRoom/img/VolverOscuro.png"))); // NOI18N
        jButton1.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/gameRoom/img/VolverBrillante.png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 440, 130, -1));

        jButtonConectarIzq.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gameRoom/img/conectarIz.png"))); // NOI18N
        jButtonConectarIzq.setBorderPainted(false);
        jButtonConectarIzq.setContentAreaFilled(false);
        jButtonConectarIzq.setMaximumSize(new java.awt.Dimension(170, 30));
        jButtonConectarIzq.setMinimumSize(new java.awt.Dimension(170, 30));
        jButtonConectarIzq.setPreferredSize(new java.awt.Dimension(140, 50));
        jButtonConectarIzq.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/gameRoom/img/conectarIzOscuro.png"))); // NOI18N
        jButtonConectarIzq.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/gameRoom/img/conectarIzBrillante.png"))); // NOI18N
        jButtonConectarIzq.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonConectarIzqActionPerformed(evt);
            }
        });
        add(jButtonConectarIzq, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 330, -1, -1));

        jLabelIpIzq.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelIpIzq.setForeground(new java.awt.Color(255, 204, 0));
        jLabelIpIzq.setText("IP izquierda");
        add(jLabelIpIzq, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 220, -1, -1));

        jLabelPortIzq.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelPortIzq.setForeground(new java.awt.Color(255, 204, 0));
        jLabelPortIzq.setText("Puerto izquierda");
        add(jLabelPortIzq, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 280, -1, -1));

        jTextFieldIpIzq.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jTextFieldIpIzq.setForeground(new java.awt.Color(255, 255, 255));
        jTextFieldIpIzq.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldIpIzq.setToolTipText("");
        jTextFieldIpIzq.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 204, 0)));
        jTextFieldIpIzq.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        jTextFieldIpIzq.setOpaque(false);
        jTextFieldIpIzq.setPreferredSize(new java.awt.Dimension(90, 20));
        add(jTextFieldIpIzq, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 240, -1, -1));

        jTablePlayers.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Jugadores Conectados"
            }
        ));
        jScrollPanePlayers.setViewportView(jTablePlayers);

        add(jScrollPanePlayers, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 120, 190, 290));

        jLabel1IpDer.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1IpDer.setForeground(new java.awt.Color(255, 204, 0));
        jLabel1IpDer.setText("IP derecha");
        add(jLabel1IpDer, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 220, -1, -1));

        jTextFieldIpDer.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jTextFieldIpDer.setForeground(new java.awt.Color(255, 255, 255));
        jTextFieldIpDer.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldIpDer.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 204, 0)));
        jTextFieldIpDer.setOpaque(false);
        jTextFieldIpDer.setPreferredSize(new java.awt.Dimension(90, 20));
        add(jTextFieldIpDer, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 240, -1, -1));

        jLabel1PortDer.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1PortDer.setForeground(new java.awt.Color(255, 204, 0));
        jLabel1PortDer.setText("Puerto derecha");
        add(jLabel1PortDer, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 280, -1, -1));

        jTextFieldPortDer.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jTextFieldPortDer.setForeground(new java.awt.Color(255, 255, 255));
        jTextFieldPortDer.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldPortDer.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 204, 0)));
        jTextFieldPortDer.setOpaque(false);
        jTextFieldPortDer.setPreferredSize(new java.awt.Dimension(40, 20));
        add(jTextFieldPortDer, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 300, -1, -1));

        jTextFieldPortIzq.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jTextFieldPortIzq.setForeground(new java.awt.Color(255, 255, 255));
        jTextFieldPortIzq.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldPortIzq.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 204, 0)));
        jTextFieldPortIzq.setOpaque(false);
        jTextFieldPortIzq.setPreferredSize(new java.awt.Dimension(40, 20));
        add(jTextFieldPortIzq, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 300, -1, -1));

        jButtonConectarDer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gameRoom/img/conectarDer.png"))); // NOI18N
        jButtonConectarDer.setBorderPainted(false);
        jButtonConectarDer.setContentAreaFilled(false);
        jButtonConectarDer.setMaximumSize(new java.awt.Dimension(170, 30));
        jButtonConectarDer.setMinimumSize(new java.awt.Dimension(170, 30));
        jButtonConectarDer.setPreferredSize(new java.awt.Dimension(140, 50));
        jButtonConectarDer.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/gameRoom/img/conectarIDerOscuro.png"))); // NOI18N
        jButtonConectarDer.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/gameRoom/img/conectarIDerBrillante.png"))); // NOI18N
        jButtonConectarDer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonConectarDerActionPerformed(evt);
            }
        });
        add(jButtonConectarDer, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 330, 140, 50));

        jLabelConFeedackR.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabelConFeedackR.setForeground(new java.awt.Color(255, 0, 0));
        jLabelConFeedackR.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gameRoom/img/disconnected.png"))); // NOI18N
        jLabelConFeedackR.setText("DESCONECTADO");
        jLabelConFeedackR.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        add(jLabelConFeedackR, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 160, -1, -1));

        jLabelConFeedackL.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabelConFeedackL.setForeground(new java.awt.Color(255, 0, 0));
        jLabelConFeedackL.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gameRoom/img/disconnected.png"))); // NOI18N
        jLabelConFeedackL.setText("DESCONECTADO");
        add(jLabelConFeedackL, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, -1, -1));

        jLabelFondo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gameRoom/img/stars.gif"))); // NOI18N
        add(jLabelFondo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Evento del botón conectar pantalla izquierda Envia los parametros de
     * conexión de la pantalla izquierda
     *
     * @param evt
     */
    private void jButtonConectarIzqActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonConectarIzqActionPerformed
        // CONECTAR IZQUIERDA
        kg.startSound(KillerSound.ClipType.PC_CLICK);
        ipIzq = jTextFieldIpIzq.getText();
        portIzq = Integer.parseInt(jTextFieldPortIzq.getText());
        kg.setPortPrev(portIzq);
        kg.setIpPrev(ipIzq);

    }//GEN-LAST:event_jButtonConectarIzqActionPerformed

    /**
     * Evento del botón volver Pone el jPanel Principal en la ventana
     *
     * @param evt
     */
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // VOLVER
        kg.startSound(KillerSound.ClipType.PC_CLICK);
        kr.setKillerPanelPrincipal();
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * Evento del botón conectar pantalla izquierda Envia los parametros de
     * conexión de la pantalla derecha
     *
     * @param evt
     */
    private void jButtonConectarDerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonConectarDerActionPerformed
        // CONECTAR  DERACHA
        kg.startSound(KillerSound.ClipType.PC_CLICK);
        ipDer = jTextFieldIpDer.getText();
        portDer = Integer.parseInt(jTextFieldPortDer.getText());
        kg.setPortNext(portDer);
        kg.setIpNext(ipDer);
    }//GEN-LAST:event_jButtonConectarDerActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButtonConectarDer;
    private javax.swing.JButton jButtonConectarIzq;
    private javax.swing.JLabel jLabel1IpDer;
    private javax.swing.JLabel jLabel1PortDer;
    private javax.swing.JLabel jLabelConFeedackL;
    private javax.swing.JLabel jLabelConFeedackR;
    private javax.swing.JLabel jLabelCopy;
    private javax.swing.JLabel jLabelFondo;
    private javax.swing.JLabel jLabelIpIzq;
    private javax.swing.JLabel jLabelPortIzq;
    private javax.swing.JLabel jLabelTitulo;
    private javax.swing.JScrollPane jScrollPanePlayers;
    private javax.swing.JTable jTablePlayers;
    private javax.swing.JTextField jTextFieldIpDer;
    private javax.swing.JTextField jTextFieldIpIzq;
    private javax.swing.JTextField jTextFieldPortDer;
    private javax.swing.JTextField jTextFieldPortIzq;
    // End of variables declaration//GEN-END:variables
}
