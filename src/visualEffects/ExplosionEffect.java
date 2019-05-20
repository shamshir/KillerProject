/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visualEffects;

import java.awt.image.BufferedImage;
import visibleObjects.VisibleObject;

/**
 *
 * @author pau
 */
public class ExplosionEffect extends KillerImage{
    
    public ExplosionEffect(VisibleObject vo, BufferedImage oi) {
        super(vo, oi, 0, 0);
    }
    
}
