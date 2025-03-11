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
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Diagram extends JPanel implements MouseListener, MouseMotionListener, KeyListener {
   private Window window;
   // Lista de clases a dibujar
   private List<Class> classList = new ArrayList<>();
   // Variable para almacenar la clase seleccionada para moverla
   private Class selectedClass = null;
   // Desfase entre el punto de clic y la posición superior izquierda del objeto
   private int offsetX, offsetY;
   private Class hoveredClass = null; // 🔹 Clase sobre la que está el cursor
   private boolean isDragging = false; // 🔹 Detectar si se está arrastrando una clase
   private Class highlightedClass = null; // 🔹 Clase seleccionada en azul (cyan)
   private Class associationTargetClass = null; // 🔹 Clase sobre la que podemos soltar la asociación
   private int tempX, tempY; // 🔹 Coordenadas temporales mientras se arrastra una asociación
   private boolean isCreatingAssociation = false; // 🔹 Detectar si estamos creando una asociación
   private Vector<Association> associations = new Vector<>(); // 🔹 Almacena las asociaciones







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
      window.updateNClasses(); // 🔹 Actualizar número de clases en la interfaz
      this.repaint();
   }

   public int getNClasses() {
      return this.classList.size();
   }

   public int getNAssociations() {
      // Se mantiene el valor de ejemplo
      return this.associations.size();
   }

   protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 🔹 Dibujar todas las asociaciones antes que las clases
        for (Association assoc : associations) {
            assoc.draw(g);
        }

        // 🔹 Dibujar la línea temporal si se está creando una asociación
        if (isCreatingAssociation && selectedClass != null) {
            g.setColor(Color.GRAY);
            int x1 = selectedClass.getX() + 60;
            int y1 = selectedClass.getY() + 45;
            g.drawLine(x1, y1, tempX, tempY);
        }

        // 🔹 Dibujar las clases (solo se iluminan en verde si son objetivo de asociación)
        for (Class c : classList) {
            boolean isAssociationTarget = (c == associationTargetClass);
            c.draw(g, c.getX(), c.getY(), isAssociationTarget);
        }
    }

   // --- Métodos de MouseListener y MouseMotionListener ---

   public void mousePressed(MouseEvent e) {
      int x = e.getX();
      int y = e.getY();

      if (e.getButton() == MouseEvent.BUTTON3) {
         for (int i = associations.size() - 1; i >= 0; i--) {
             Association assoc = associations.get(i);
             if (assoc.isClicked(x, y)) { // 🔹 Verificar si el clic fue sobre la línea
                 associations.remove(i); // 🔹 Eliminar la asociación
                 window.updateNAssociations(); // 🔹 Actualizar el contador de asociaciones
                 repaint();
                 return; // 🔹 Salir del método para evitar otras acciones
             }
         }
     }

      // 🔹 Recorrer la lista en orden inverso para seleccionar la clase superior
      for (int i = classList.size() - 1; i >= 0; i--) {
          Class c = classList.get(i);
          if (c.contains(x, y)) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                if (highlightedClass == c) { // 🔹 Iniciar una asociación
                    isCreatingAssociation = true;
                    selectedClass = c;
                    tempX = x;
                    tempY = y;
                } else { // 🔹 Mover clase normal
                    selectedClass = c;
                    offsetX = x - c.getX();
                    offsetY = y - c.getY();
                    classList.remove(i);
                    classList.add(c);
                    isDragging = true;
                }
            } else if (e.getButton() == MouseEvent.BUTTON3) { // 🔹 Borrar clase
               if (highlightedClass == c) {
                   highlightedClass = null;
               }

               // 🔹 Eliminar todas las asociaciones conectadas a la clase eliminada
               associations.removeIf(assoc -> assoc.getClassA() == c || assoc.getClassB() == c);

               classList.remove(i);
               window.updateNClasses();
               window.updateNAssociations(); // 🔹 Actualizar el contador de asociaciones
               repaint();
           }
            break;
        }
      }
  }

   public void mouseDragged(MouseEvent e) {
      if (isCreatingAssociation) {
         tempX = e.getX();
         tempY = e.getY();

         // 🔹 Resaltar en verde solo si es un objetivo válido para la asociación
         associationTargetClass = null;
         for (Class c : classList) {
             if (c.contains(tempX, tempY) && c != selectedClass) {
                 associationTargetClass = c;
                 break;
             }
         }
         repaint();
      }else if (selectedClass != null) {
         int newX = e.getX() - offsetX;
         int newY = e.getY() - offsetY;
         selectedClass.setPosition(newX, newY);
         repaint();
     }
   }

   public void mouseReleased(MouseEvent e) {
      if (isCreatingAssociation && selectedClass != null) {
         if (associationTargetClass != null && associationTargetClass != selectedClass) {
            // 🔹 Crear una asociación normal entre dos clases distintas
            associations.add(new Association(selectedClass, associationTargetClass));
        } else if (selectedClass.contains(tempX, tempY)) {
            // 🔹 Si el usuario suelta sobre la misma clase, crear una auto-asociación
            associations.add(new Association(selectedClass, selectedClass));
        }
        window.updateNAssociations(); // 🔹 Actualizar el contador de asociaciones

         // 🔹 Deseleccionar cualquier clase en cyan cuando se crea una asociación
         if (highlightedClass != null) {
            highlightedClass.setSelected(false);
            highlightedClass = null;
        }
     }
     selectedClass = null;
     isCreatingAssociation = false;
     isDragging = false;
     associationTargetClass = null; // 🔹 Limpiar el objetivo de asociación para borrar el verde
     repaint();
   }

   public void mouseEntered(MouseEvent e) { }
   public void mouseExited(MouseEvent e) { }
   public void mouseClicked(MouseEvent e) { }

   public void mouseMoved(MouseEvent e) {
      int x = e.getX();
      int y = e.getY();
      Class newHoveredClass = null;

      for (int i = classList.size() - 1; i >= 0; i--) {
          Class c = classList.get(i);
          if (c.contains(x, y)) {
              newHoveredClass = c;

              // 🔹 Si la clase no está al frente, traerla adelante
              if (classList.indexOf(c) != classList.size() - 1) {
                  classList.remove(c);
                  classList.add(c);
              }
              break;
          }
      }

      // 🔹 Si la clase sobre la que pasamos ha cambiado, actualizamos el estado
      if (hoveredClass != newHoveredClass) {
          if (hoveredClass != null) {
              hoveredClass.setHovered(false); // Quitamos el estado "hovered" de la anterior
          }
          if (newHoveredClass != null) {
              newHoveredClass.setHovered(true); // Activamos "hovered" en la nueva
          }
          hoveredClass = newHoveredClass;
          repaint();
      }
  }

   // --- Métodos de KeyListener ---

   public void keyTyped(KeyEvent e) { }

   public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_S) {
            if (isDragging) {
                return; // 🔹 Ignorar `S` si el usuario está arrastrando la clase
            }
            if (hoveredClass != null) {
                // 🔹 Si hay una clase bajo el cursor, seleccionar/desseleccionar
                if (highlightedClass != null && highlightedClass != hoveredClass) {
                    highlightedClass.setSelected(false); // 🔹 Desseleccionar la anterior correctamente
                }

                hoveredClass.setSelected(!hoveredClass.isSelected()); // 🔹 Cambiar selección
                highlightedClass = hoveredClass.isSelected() ? hoveredClass : null; // 🔹 Actualizar referencia
            }else {
                // 🔹 Si no hay clase bajo el cursor, deseleccionar la última seleccionada
                if (highlightedClass != null) {
                    highlightedClass.setSelected(false);
                    highlightedClass = null;
                }
            }
            repaint();
        }
        else if (e.getKeyCode() == KeyEvent.VK_A) {
            if (highlightedClass != null) {
                String attr = JOptionPane.showInputDialog("Ingrese un atributo:");
                if (attr != null && !attr.trim().isEmpty()) {
                    highlightedClass.addAttribute(attr);
                    repaint();
                }
            }
        } else if (e.getKeyCode() == KeyEvent.VK_M) {
            if (highlightedClass != null) {
                String method = JOptionPane.showInputDialog("Ingrese un método:");
                if (method != null && !method.trim().isEmpty()) {
                    highlightedClass.addMethod(method);
                    repaint();
                }
            }
        }
    }
    public void keyReleased(KeyEvent e) { }
}
