/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package killerproject;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

/**
 *
 * @author berna
 */
public class Controlled extends Alive {

    String ip;
    String user;
    boolean up, down, right, left;
    boolean fright;
    boolean wup, wdown, wright, wleft;
    private ArrayList<Shoot> shoots = new ArrayList();
    private boolean death;

    public Controlled(KillerGame kg, Color color, String ip, String user) {
        this.kg = kg;
        this.ip = ip;

        //aspecto visual
        HEIGHT = 30;
        WIDTH = 30;
        this.user = user;
        this.color = color;
        colorhex = String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());

        //tamaño hitbox
        hitbox = new Rectangle((int) x, (int) y, WIDTH, HEIGHT);

        //movimiento y posición
        dx = 0;
        dy = 0;
        speed = 3.1;

        //posicion aleatoria en el espacio del canvas
        x = (int) (kg.getViewer().getWidth() / 2 * Math.random());
        y = (int) (kg.getViewer().getHeight() / 2 * Math.random());

        //estados
        fright = true;
        alive = true;
        death = false;

        wup = false;
        wdown = false;
        wleft = false;
        wright = false;
        //tiempo
        time = System.nanoTime();

    }

    public void run() {

        while (alive) {
            move();
            try {
                Thread.sleep(15);
            } catch (InterruptedException ex) {

            }

        }

    }

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

    @Override
    public void collision() {

    }

    public void death() {
        alive = false;
    }

    @Override
    public void draw(Graphics g) {
        if (!death) {
            g.setColor(Color.WHITE);
            g.drawString(user, (int) x, (int) y - (HEIGHT / 2));
            g.setColor(color);
            g.fillOval((int) x, (int) y, HEIGHT, WIDTH);
            drawShoots(g);
        }
        // g.drawImage(,x, y,null);
    }

    public void drawShoots(Graphics g) {
        if (!shoots.isEmpty()) {
            for (int i = 0; i < shoots.size(); i++) {
                Shoot current = shoots.get(i);
                g.setColor(current.getColor());
                g.fillOval((int) current.getX(), (int) current.getY(),
                        current.getRadius(), current.getRadius());
            }

        }
    }

    public void kill() {
        System.out.println(ip + " ha muerto.");
        KillerPad.lifeShip("death", kg, ip, kg.getIplocal());
        KillerPad.sendMessageToPad("ded", kg, ip, kg.getIplocal());
    }

    @Override
    public void move() {
        double timedif = (System.nanoTime() - time) / 10000000d;

        kg.checkColision(this);
        checkMove();
        x += dx * timedif;
        y += dy * timedif;

        dx = 0;
        dy = 0;

        updateHitBox();

        time = System.nanoTime();

    }

    public Rectangle nextMove() {
        return new Rectangle((int) (x + dx), (int) (y + dy), WIDTH, HEIGHT);
    }

    public void restart() {
        System.out.println(ip + "ha revivido.");
        KillerPad.lifeShip("replay", kg, ip, kg.getIplocal());
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
                break;
            case "upright":
                up = true;
                down = false;
                right = true;
                left = false;
                break;
            case "upleft":
                up = true;
                down = false;
                right = false;
                left = true;
                break;
            case "left":
                up = false;
                down = false;
                right = false;
                left = true;
                break;
            case "right":
                up = false;
                down = false;
                right = true;
                left = false;
                break;
            case "down":
                up = false;
                down = true;
                right = false;
                left = false;
                break;
            case "downleft":
                up = false;
                down = true;
                right = false;
                left = true;
                break;
            case "downright":
                up = false;
                down = true;
                right = true;
                left = false;
                break;
            case "idle":
                up = false;
                down = false;
                right = false;
                left = false;
                break;
        }

    }

    public void shoot() {
        Shoot fire = new Shoot(kg, color, this);
        shoots.add(fire);
        new Thread(fire).start();
    }

    public void stop() {
        up = false;
        down = false;
        right = false;
        left = false;
    }

    public void updateHitBox() {
        hitbox.setBounds((int) x, (int) y, WIDTH, HEIGHT);
    }

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

}
