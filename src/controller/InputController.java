package controller;

import java.awt.event.*;

public class InputController implements KeyListener, MouseListener, MouseMotionListener {
    private boolean left, right, jump, attack, ultimate, special;
    private boolean enter, esc, p;

    private int mouseX, mouseY;
    private boolean mousePress;
    private boolean mouseRelease;

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A -> left = true;
            case KeyEvent.VK_D -> right = true;
            case KeyEvent.VK_SPACE -> jump = true;
            case KeyEvent.VK_J -> attack = true;
            case KeyEvent.VK_K -> ultimate = true;
            case KeyEvent.VK_L -> special = true;
            case KeyEvent.VK_P -> p = true;
            case KeyEvent.VK_ENTER -> enter = true;
            case KeyEvent.VK_ESCAPE -> esc = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A -> left = false;
            case KeyEvent.VK_D -> right = false;
            case KeyEvent.VK_SPACE -> jump = false;
            case KeyEvent.VK_J -> attack = false;
            case KeyEvent.VK_K -> ultimate = false;
            case KeyEvent.VK_L -> special = false;
            case KeyEvent.VK_P -> p = false;
            case KeyEvent.VK_ENTER -> enter = false;
            case KeyEvent.VK_ESCAPE -> esc = false;
        }
    }

    public void resetKeys() {
        enter = esc = p = false;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mousePress = true;
        mouseRelease = false;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mousePress = false;
        mouseRelease = true;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public boolean isLeft() {
        return left;
    }

    public boolean isRight() {
        return right;
    }

    public boolean isJump() {
        return jump;
    }

    public boolean isAttack() {
        return attack;
    }

    public boolean isUltimate() {
        return ultimate;
    }

    public boolean isSpecial() {
        return special;
    }

    public boolean isEnter() {
        return enter;
    }

    public boolean isEsc() {
        return esc;
    }

    public boolean isP() {
        return p;
    }

    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

    public boolean isMousePress() {
        return mousePress;
    }

    public boolean isMouseRelease() {
        return mouseRelease;
    }

    public void resetMouse() {
        mouseRelease = false;
    }

}
