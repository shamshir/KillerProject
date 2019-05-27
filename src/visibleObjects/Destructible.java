package visibleObjects;

/**
 *
 * @author miaad
 */
public interface Destructible {
    public void quitarVida(int damage);
    public void onDying();
    public void die();
}
