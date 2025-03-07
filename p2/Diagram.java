import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class Diagram extends JPanel implements MouseListener, MouseMotionListener, KeyListener {
   private Window window;
   // Lista de clases a dibujar
   private List<Class> classList = new ArrayList<>();
   // Variable para almacenar la clase seleccionada para moverla
   private Class selectedClass = null;
   // Desfase entre el punto de clic y la posición superior izquierda del objeto
   private int offsetX, offsetY;

   public Diagram(Window var1) {
      this.window = var1;
      this.addMouseListener(this);
      this.addMouseMotionListener(this);
      this.addKeyListener(this);
      this.setBorder(BorderFactory.createLineBorder(Color.black));
      // Aseguramos que el panel se pinte con fondo opaco
      this.setOpaque(true);
      // Permitir el enfoque para recibir eventos de teclado
      this.setFocusable(true);
      this.requestFocusInWindow();
   }

   public void addClass() {
      Class newClass = new Class();
      // Calcula una posición en cascada para que no se superpongan
      int offset = classList.size() * 20;
      newClass.setPosition(10 + offset, 10 + offset);
      newClass.setOpaque(true);
      this.classList.add(newClass);
      this.repaint();
   }

   public int getNClasses() {
      return this.classList.size();
   }

   public int getNAssociations() {
      // Se mantiene el valor de ejemplo
      return 1;
   }

   protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      // Se recorre la lista y se dibujan las clases en sus posiciones actuales
      for (Class c : this.classList) {
         c.draw(g, c.getX(), c.getY());
      }
   }

   // --- Métodos de MouseListener y MouseMotionListener ---

   public void mousePressed(MouseEvent e) {
      int x = e.getX();
      int y = e.getY();
      // Recorrer la lista en orden inverso para seleccionar el objeto "superior"
      for (int i = classList.size() - 1; i >= 0; i--) {
         Class c = classList.get(i);
         if (c.contains(x, y)) {
            selectedClass = c;
            offsetX = x - c.getX();
            offsetY = y - c.getY();
            break;
         }
      }
   }

   public void mouseDragged(MouseEvent e) {
      if (selectedClass != null) {
         int newX = e.getX() - offsetX;
         int newY = e.getY() - offsetY;
         selectedClass.setPosition(newX, newY);
         this.repaint();
      }
   }

   public void mouseReleased(MouseEvent e) {
      selectedClass = null;
   }

   public void mouseEntered(MouseEvent e) { }
   public void mouseExited(MouseEvent e) { }
   public void mouseClicked(MouseEvent e) { }

   // Actualiza el estado "hovered" de cada clase para resaltar solo la superior
   public void mouseMoved(MouseEvent e) {
      int x = e.getX();
      int y = e.getY();
      // Se determina la clase que se encuentra en la parte superior (recorriendo en orden inverso)
      Class hoveredClass = null;
      for (int i = classList.size() - 1; i >= 0; i--) {
         Class c = classList.get(i);
         if (c.contains(x, y)) {
            hoveredClass = c;
            break;
         }
      }
      // Actualizamos el estado "hovered" de todas las clases
      boolean repaintNeeded = false;
      for (Class c : classList) {
         boolean shouldBeHovered = (c == hoveredClass);
         if (shouldBeHovered != c.isHovered()) {
            c.setHovered(shouldBeHovered);
            repaintNeeded = true;
         }
      }
      if (repaintNeeded) {
         this.repaint();
      }
   }

   // --- Métodos de KeyListener ---

   public void keyTyped(KeyEvent e) { }

   public void keyPressed(KeyEvent e) {
      // Si se pulsa la tecla Supr (Delete)
      if (e.getKeyCode() == KeyEvent.VK_DELETE) {
         // Se recorre la lista en orden inverso y se elimina la primera clase que esté resaltada
         for (int i = classList.size() - 1; i >= 0; i--) {
            Class c = classList.get(i);
            if (c.isHovered()) {
               classList.remove(i);
               break;  // Eliminamos solo una clase
            }
         }
         this.repaint();
      }
   }

   public void keyReleased(KeyEvent e) { }
}
