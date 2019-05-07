package visibleObjects;

import communications.KillerPad;
import game.KillerGame;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import visualEffects.Animacion;

public class KillerShip extends Controlled {

    protected String ip;
    protected String user;
    protected boolean up, down, right, left;
    protected boolean fright;
    protected boolean wup, wdown, wright, wleft;
    protected ArrayList<Shoot> shoots = new ArrayList();
    protected boolean death;

    public KillerShip(KillerGame game, Color color, String ip, String user) {
        super(game);
        this.type = "killership";
        this.ip = ip;
        this.angle = 0;

        //aspecto visual
//        this.width = 30;
//        this.height = 30;
//        this.setImage("img/nave.png");
        this.width = 70;
        this.height = 90;
        this.user = user;
        this.color = color;
        this.colorhex = String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());

        //objeto hitbox para comprobar colision
        this.hitbox = new Rectangle((int) x, (int) y, width, height);

        //movimiento y posición
        dx = 0;
        dy = 0;
        speed = 3.1;

        //posicion aleatoria en el espacio del canvas
        x = (int) (game.getViewer().getWidth() / 2 * Math.random());
        y = (int) (game.getViewer().getHeight() / 2 * Math.random());

        System.out.print("ship pos x: " + this.x);
        System.out.println(" -- ship pos y: " + this.y);

        //estados
        fright = true;
        //alive = true;
        death = false;

        // Para las colisiones con los bordes
        wup = false;
        wdown = false;
        wleft = false;
        wright = false;

        //tiempo
        time = System.nanoTime();
    }

    @Override
    public void render(Graphics2D g2d) {
        if (!death) {
            AffineTransform old = g2d.getTransform();

//            g2d.setColor(Color.WHITE);
//            g2d.drawString(user, (int) x, (int) y - (height / 2));
//            g2d.setColor(color);
//            g2d.fillOval((int) x, (int) y, height, width);
            Animacion.addRotation(g2d, this);
            Animacion.addFire(this.image);

            g2d.drawImage(this.image, (int) x, (int) y, this.width, this.height, null);
            g2d.setTransform(old);

            //drawShoots(g);
        }
    }

    @Override
    public void move() {
        double timedif = (System.nanoTime() - time) / 10000000d;

        //game.checkColision(this);
        checkMove();
        x += dx * timedif;
        y += dy * timedif;

        dx = 0;
        dy = 0;

        updateHitBox();

        time = System.nanoTime();

    }

    @Override
    public Rectangle nextMove() {
        return new Rectangle((int) (x + dx), (int) (y + dy), width, height);
    }

    @Override
    public void collision() {

    }

    @Override
    public void die() {
        alive = false;
    }

    // Actualiza el desplazamiento en cada eje x, y, segun las ordenes del mando
    // 
    public void checkMove() {
        if (up && !wup) {
            dy = -speed;
            if (wdown) {
                wdown = false;
            }
        }
        if (down && !wdown) {
            dy = speed;

            if (wup) {
                wup = false;
            }
        }
        if (right && !wright) {
            dx = speed;
            fright = true;

            if (wleft) {
                wleft = false;
            }
        }
        if (left && !wleft) {
            dx = -speed;
            fright = false;

            if (wright) {
                wright = false;
            }
        }
    }

    public void drawShoots(Graphics g) {
        if (!shoots.isEmpty()) {
            for (int i = 0; i < shoots.size(); i++) {
                Shoot current = shoots.get(i);
                g.setColor(current.getColor());
//                g.fillOval((int) current.getX(), (int) current.getY(),
//                        current.getRadius(), current.getRadius());
                g.fillOval((int) current.getX(), (int) current.getY(),
                        current.getWidth(), current.getHeight());
            }

        }
    }

    public void kill() {
        System.out.println(ip + " ha muerto.");
        KillerPad.lifeShip("death", game, ip, game.getIplocal());
        KillerPad.sendMessageToPad("ded", game, ip, game.getIplocal());
    }

    public void restart() {
        System.out.println(ip + "ha revivido.");
        KillerPad.lifeShip("replay", game, ip, game.getIplocal());
    }

    public void setDirections(String direction) {

        String dir = direction.trim().toLowerCase();

        switch (dir) {
            case "up":
                System.out.println("upppp");
                up = true;
                down = false;
                right = false;
                left = false;
                this.angle = 0;
                break;
            case "upright":
                up = true;
                down = false;
                right = true;
                left = false;
                this.angle = 45;
                break;
            case "upleft":
                up = true;
                down = false;
                right = false;
                left = true;
                this.angle = 315;
                break;
            case "left":
                up = false;
                down = false;
                right = false;
                left = true;
                this.angle = 270;
                break;
            case "right":
                up = false;
                down = false;
                right = true;
                left = false;
                this.angle = 90.0;
                break;
            case "down":
                up = false;
                down = true;
                right = false;
                left = false;
                this.angle = 180;
                break;
            case "downleft":
                up = false;
                down = true;
                right = false;
                left = true;
                this.angle = 225;
                break;
            case "downright":
                up = false;
                down = true;
                right = true;
                left = false;
                this.angle = 135;
                break;
            case "idle":
                up = false;
                down = false;
                right = false;
                left = false;
                break;
        }

    }

//    public Shoot shoot() {
//        Shoot fire = new Shoot(game, color, this);
//        this.game.getObjects().add(fire);
//        new Thread(fire).start();
//
//        return fire;
//    }
//    public Missile shootMissile() {
//        Missile fire = new Missile(game, color, this);
//        this.game.getObjects().add(fire);
//        new Thread(fire).start();
//
//        return fire;
//    }
    // ------------------> ORIGINAL
//    public void shoot() {
//        Shoot fire = new Shoot(game, color, this);
//        this.shoots.add(fire);
//        new Thread(fire).start();
//    }    
    public void shoot() {
        Shoot fire = new Shoot(game, color, this);
        this.game.getObjects().add(fire);
        new Thread(fire).start();
    }

    public void stop() {
        up = false;
        down = false;
        right = false;
        left = false;
    }

    // *********************
    // * Getters & Setters *
    // *********************
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public ArrayList<Shoot> getShoots() {
        return shoots;
    }

    public void setShoots(ArrayList<Shoot> shoots) {
        this.shoots = shoots;
    }

    public boolean isWup() {
        return wup;
    }

    public void setWup(boolean wup) {
        this.wup = wup;
    }

    public boolean isWdown() {
        return wdown;
    }

    public void setWdown(boolean wdown) {
        this.wdown = wdown;
    }

    public boolean isWright() {
        return wright;
    }

    public void setWright(boolean wright) {
        this.wright = wright;
    }

    public boolean isWleft() {
        return wleft;
    }

    public void setWleft(boolean wleft) {
        this.wleft = wleft;
    }

    public boolean isDeath() {
        return death;
    }

    public void setDeath(boolean death) {
        this.death = death;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

}
