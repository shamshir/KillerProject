package gameRoom;

import game.KillerGame;
import java.awt.Color;
import javax.swing.JOptionPane;
import sound.KillerRadio;
import sound.KillerSound;

/**
 *
 * @author Yeray
 */
public class KillerPanelPrincipal extends javax.swing.JPanel {

    //Variables Killer
    private KillerRoom kr;
    static KillerGame kg;

    //Variables booleanas que controlan el sonido, música y aparición de Pacman
    private boolean s = false;
    static boolean m = false;
    private boolean p = false;
    private boolean sp = false;
    private int spcount = 0;

    //Variables para controlar eventos
    int[] sequence = {38, 38, 40, 40, 37, 39, 37, 39, 66, 65};
    int currentButton = 0;

    /**
     * Creates new form KillerPanelPrincipal
     *
     * @param kg
     */
    public KillerPanelPrincipal(KillerRoom kr) {
        this.kr = kr;
        kg = kr.getKg();
        initComponents();
        jButtonJugar.setEnabled(false);
    }

    /**
     * Método que setea la configuración en función de otra pantalla del juego.
     * También setea la configuración inicial para activar la múscica
     *
     * @param sound
     * @param music
     * @param pacman
     * @param sPacman
     */
    public void setNetworkConf(Boolean sound, Boolean music, Boolean pacman, Boolean sPacman) {
        s = sound;
        m = music;
        p = pacman;
        sp = sPacman;
        setNetworkSounds();
        setNetworkMusic();
        setNetworkPacman();
    }

       /**
     * Método que cambia el icono y el estado a cada clic 
     * Para reflejar cuando una configuración viene de otra pantalla
     */
    public void setNetworkSounds() {
        if (s == false) {
            jLabelSound.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gameRoom/img/noSound.png")));
            s = true;
        } else {
            jLabelSound.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gameRoom/img/sound.png")));
            s = false;
        }
    }

    /**
     * Método que cambia el icono y el estado a cada clic 
     * Para reflejar cuando una configuración viene de otra pantalla
     */
    public void setNetworkMusic() {
        if (m == false) {
            jLabelMusic.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gameRoom/img/musicFalse.png")));
            m = true;
        } else {
            jLabelMusic.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gameRoom/img/musicTrue.png")));
            m = false;
        }
    }

    /**
     * Método que cambia el icono y el estado a cada clic 
     * Para reflejar cuando una configuración viene de otra pantalla
     *
     */
    
    public void setNetworkPacman() {
        if (p == false) {
            jLabelPacman.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gameRoom/img/pacmanR.png")));
            p = true;
        } else {
            jLabelPacman.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gameRoom/img/pacman.png")));
            p = false;
        }
        if (sp == true) {
            jLabelPacman.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gameRoom/img/sPacman.png")));
        }
    }
    
    /**
     * Método que cambia el icono y el estado a cada clic y cambia la activación
     * de los sonidos del juego
     */
    public void setSounds() {
        if (s == false) {
            jLabelSound.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gameRoom/img/noSound.png")));
            s = true;
            kg.setSoundEffects(false);
        } else {
            jLabelSound.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gameRoom/img/sound.png")));
            s = false;
            kg.setSoundEffects(true);
        }
    }

    /**
     * Método que cambia el icono y el estado a cada clic y cambia la activación
     * de la música del juego
     */
    public void setMusic() {
        if (m == false) {
            jLabelMusic.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gameRoom/img/musicFalse.png")));
            m = true;
            kg.setSoundMusic(false);
            kg.stopMusic();
        } else {
            jLabelMusic.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gameRoom/img/musicTrue.png")));
            m = false;
            kg.setSoundMusic(true);
            kg.changeMusic(KillerRadio.ClipType.MENU);
        }
    }

    /**
     * Método que cambia el icono y el estado a cada clic y cambia la activación
     * de pacman
     *
     */
    public void setPacman() {
        if (p == false) {
            jLabelPacman.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gameRoom/img/pacmanR.png")));
            p = true;
            kg.setPacmanExistence(false);
        } else {
            jLabelPacman.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gameRoom/img/pacman.png")));
            p = false;
            spcount++;
            kg.setPacmanExistence(true);
        }
        if (spcount > 10) {
            sp = true;
        }
        if (sp == true) {
            jLabelPacman.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gameRoom/img/sPacman.png")));
            kg.setPacmanExistence(true);
            kg.enableUltrapacman();
        }
    }

    /**
     * Cambia el texto e imagen que dice si esta conectado ese lado izquierdo de
     * la pantalla
     *
     * @param aux
     */
    public void setFeedbackConnetionLeft(Boolean aux) {
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
    public void setFeedbackConnetionRight(Boolean aux) {
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
     * Recibe el código del boton pulsado y con un enumerado y un contador
     * compara los datos recibidos y si llegan los códigos correctos en orden
     * correcto crea un objeto KillerCode
     *
     * @param keyP
     */
    public void checkEvent(int keyP) {
        if (keyP == sequence[currentButton]) {
            currentButton++;
        } else {
            currentButton = 0;
        }
        if (currentButton == sequence.length) {
            currentButton = 0;
            if (s == false) {
                kg.changeMusic(KillerRadio.ClipType.ENDING);
            }
            KillerCode kc = new KillerCode(sp);
            kc.setVisible(true);
        }
    }

    /**
     * Recibe una booleana y settea el botón jugar en función de esta
     *
     * @param aux
     */
    public void setButtonPlay(Boolean aux) {
        jButtonJugar.setEnabled(aux);
    }

    /**
     * Método preparado para que al cerrar o volver de los créditos vuelva a
     * sonar la música del menú. Pero si el jugador seleccionó que no quiere
     * música esta no vuelve a sonar
     */
    public static void menuRadio() {
        if (m == false) {
            kg.changeMusic(KillerRadio.ClipType.MENU);
        }
    }

    /**
     * Getter de la clase
     *
     * @return
     */
    public KillerPanelPrincipal getKillerPanelPrincipal() {
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
        jButtonJugar = new javax.swing.JButton();
        jButtonConectar = new javax.swing.JButton();
        jButtonSalir = new javax.swing.JButton();
        jLabelConFeedackL = new javax.swing.JLabel();
        jLabelConFeedackR = new javax.swing.JLabel();
        jLabelSound = new javax.swing.JLabel();
        jLabelMusic = new javax.swing.JLabel();
        jLabelPacman = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabelFondo = new javax.swing.JLabel();
        jTextFieldSecret = new javax.swing.JTextField();

        setMaximumSize(new java.awt.Dimension(525, 525));
        setMinimumSize(new java.awt.Dimension(525, 525));
        setPreferredSize(new java.awt.Dimension(525, 525));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabelTitulo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gameRoom/img/Logo.png"))); // NOI18N
        jLabelTitulo.setText("jLabel1");
        jLabelTitulo.setPreferredSize(new java.awt.Dimension(480, 80));
        add(jLabelTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, -1, -1));

        jLabelCopy.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelCopy.setForeground(new java.awt.Color(255, 255, 255));
        jLabelCopy.setText("KillerTeam ®");
        jLabelCopy.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelCopyMouseClicked(evt);
            }
        });
        add(jLabelCopy, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 510, -1, -1));

        jButtonJugar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gameRoom/img/jugar.png"))); // NOI18N
        jButtonJugar.setBorderPainted(false);
        jButtonJugar.setContentAreaFilled(false);
        jButtonJugar.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/gameRoom/img/jugarBlocked .png"))); // NOI18N
        jButtonJugar.setPreferredSize(new java.awt.Dimension(100, 30));
        jButtonJugar.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/gameRoom/img/jugarOscuro.png"))); // NOI18N
        jButtonJugar.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/gameRoom/img/jugarBrillante.png"))); // NOI18N
        jButtonJugar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonJugarMouseClicked(evt);
            }
        });
        jButtonJugar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonJugarActionPerformed(evt);
            }
        });
        add(jButtonJugar, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 250, -1, -1));

        jButtonConectar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gameRoom/img/Conectar.png"))); // NOI18N
        jButtonConectar.setBorderPainted(false);
        jButtonConectar.setContentAreaFilled(false);
        jButtonConectar.setPreferredSize(new java.awt.Dimension(140, 50));
        jButtonConectar.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/gameRoom/img/ConectarOscuro.png"))); // NOI18N
        jButtonConectar.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/gameRoom/img/ConectarBrillante.png"))); // NOI18N
        jButtonConectar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonConectarActionPerformed(evt);
            }
        });
        add(jButtonConectar, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 290, 150, 40));

        jButtonSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gameRoom/img/salir.png"))); // NOI18N
        jButtonSalir.setBorderPainted(false);
        jButtonSalir.setContentAreaFilled(false);
        jButtonSalir.setPreferredSize(new java.awt.Dimension(100, 30));
        jButtonSalir.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/gameRoom/img/salirOscuro.png"))); // NOI18N
        jButtonSalir.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/gameRoom/img/salirBrillante.png"))); // NOI18N
        jButtonSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSalirActionPerformed(evt);
            }
        });
        add(jButtonSalir, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 390, -1, -1));

        jLabelConFeedackL.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabelConFeedackL.setForeground(new java.awt.Color(255, 0, 0));
        jLabelConFeedackL.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gameRoom/img/disconnected.png"))); // NOI18N
        jLabelConFeedackL.setText("DESCONECTADO");
        add(jLabelConFeedackL, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, -1, -1));

        jLabelConFeedackR.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabelConFeedackR.setForeground(new java.awt.Color(255, 0, 0));
        jLabelConFeedackR.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gameRoom/img/disconnected.png"))); // NOI18N
        jLabelConFeedackR.setText("DESCONECTADO");
        jLabelConFeedackR.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        add(jLabelConFeedackR, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 160, -1, -1));

        jLabelSound.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gameRoom/img/sound.png"))); // NOI18N
        jLabelSound.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelSoundMouseClicked(evt);
            }
        });
        add(jLabelSound, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 490, -1, -1));

        jLabelMusic.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gameRoom/img/musicTrue.png"))); // NOI18N
        jLabelMusic.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelMusicMouseClicked(evt);
            }
        });
        add(jLabelMusic, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 490, -1, -1));

        jLabelPacman.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gameRoom/img/pacman.png"))); // NOI18N
        jLabelPacman.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelPacmanMouseClicked(evt);
            }
        });
        add(jLabelPacman, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 490, -1, -1));

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gameRoom/img/ajustes.png"))); // NOI18N
        jButton1.setText("jButtonAjustes");
        jButton1.setBorderPainted(false);
        jButton1.setContentAreaFilled(false);
        jButton1.setPreferredSize(new java.awt.Dimension(150, 40));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 340, -1, -1));

        jLabelFondo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gameRoom/img/stars.gif"))); // NOI18N
        add(jLabelFondo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        jTextFieldSecret.setText("jTextField1");
        jTextFieldSecret.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldSecretKeyPressed(evt);
            }
        });
        add(jTextFieldSecret, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 20, -1, -1));
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Evento de botón salir. Cierra el programa
     *
     * @param evt
     */
    private void jButtonSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSalirActionPerformed
        // SALIR
        kg.startSound(KillerSound.ClipType.PC_CLICK);
        System.exit(0);
    }//GEN-LAST:event_jButtonSalirActionPerformed

    /**
     * Evento del botón conectar Pone en la ventana el KillerPanelConectar
     *
     * @param evt
     */
    private void jButtonConectarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonConectarActionPerformed
        // CONECTAR
        kg.startSound(KillerSound.ClipType.PC_CLICK);
        kr.setKillerPanelConectar();
    }//GEN-LAST:event_jButtonConectarActionPerformed

    /**
     * Evento del botón Jugar Llama al método sendStart() de la clase KillerGame
     *
     * @param evt
     */
    private void jButtonJugarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonJugarActionPerformed
        // JUGAR
        kg.startSound(KillerSound.ClipType.PC_CLICK);
        kg.sendStart();
    }//GEN-LAST:event_jButtonJugarActionPerformed

    /**
     * Evento del jLabel Copyright Al pulsarlo muestra los créditos
     *
     * @param evt
     */
    private void jLabelCopyMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelCopyMouseClicked
        // CREDITOS KILLER GAME
        kg.startSound(KillerSound.ClipType.PC_CLICK);
        if (m == false) {
            kg.changeMusic(KillerRadio.ClipType.ENDING);
        }else{
            kg.stopMusic();
        }
        String[] args = null;
        KillerCredits.main(args);
    }//GEN-LAST:event_jLabelCopyMouseClicked

    /**
     * Evento del icono de sonidos Cambia el estado de la booleana dedicada a
     * los sonidos
     *
     * @param evt
     */
    private void jLabelSoundMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelSoundMouseClicked
        // CAMBIAR ESTADO DE SONIDOS DEL JUEGO
        kg.startSound(KillerSound.ClipType.PC_CLICK);
        setSounds();
    }//GEN-LAST:event_jLabelSoundMouseClicked

    /**
     * Evento de botón jugar Easter Egg
     *
     * @param evt
     */
    private void jButtonJugarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonJugarMouseClicked
        // Shhhhh...
        if (evt.getClickCount() == 50) {
            kg.startSound(KillerSound.ClipType.SECRET);
            JOptionPane.showMessageDialog(this, "¿Crees que te vamos a dar un logro por esto?", "Esto no es un logro", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_jButtonJugarMouseClicked

    /**
     * Evento de jTextField Easter Egg
     *
     * @param evt
     */
    private void jTextFieldSecretKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldSecretKeyPressed
        checkEvent(evt.getKeyCode());
    }//GEN-LAST:event_jTextFieldSecretKeyPressed

    /**
     * Evento del icono de sonidos Cambia el estado de la booleana dedicada a la
     * música del juego
     *
     * @param evt
     */
    private void jLabelMusicMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelMusicMouseClicked
        // CAMBIAR ESTADO DE MUSICA DEL MENU Y PARTIDA
        kg.startSound(KillerSound.ClipType.PC_CLICK);
        setMusic();
    }//GEN-LAST:event_jLabelMusicMouseClicked

    /**
     * Evento del icono de sonidos Cambia el estado de la booleana dedicada a la
     * aparición de Pacman
     *
     * @param evt
     */
    private void jLabelPacmanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelPacmanMouseClicked
        // ACTIVAR Y DESACTIVAR PACMAN
        kg.startSound(KillerSound.ClipType.PC_CLICK);
        setPacman();
    }//GEN-LAST:event_jLabelPacmanMouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // AJUSTES
        kg.startSound(KillerSound.ClipType.PC_CLICK);
        kr.setKillerPanelAjustes();
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButtonConectar;
    private javax.swing.JButton jButtonJugar;
    private javax.swing.JButton jButtonSalir;
    private javax.swing.JLabel jLabelConFeedackL;
    private javax.swing.JLabel jLabelConFeedackR;
    private javax.swing.JLabel jLabelCopy;
    private javax.swing.JLabel jLabelFondo;
    private javax.swing.JLabel jLabelMusic;
    private javax.swing.JLabel jLabelPacman;
    private javax.swing.JLabel jLabelSound;
    private javax.swing.JLabel jLabelTitulo;
    static javax.swing.JTextField jTextFieldSecret;
    // End of variables declaration//GEN-END:variables
}
