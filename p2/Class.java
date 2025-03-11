import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

public class Class extends JPanel {
   private static int instanceCount = 0;
   private String className;
   private int x;
   private int y;
   // Bandera para determinar si el ratón está sobre la clase
   private boolean hovered = false;
   private boolean selected = false;
   private List<String> attributes = new ArrayList<>(); // 🔹 Lista de atributos
   private List<String> methods = new ArrayList<>(); // 🔹 Lista de métodos


   public Class() {
      instanceCount++;
      this.className = "Class " + instanceCount;
      // Posición inicial predeterminada (se actualizará al agregarla)
      this.x = 10;
      this.y = 10;
      this.setOpaque(true);
   }

   public int getX() {
      return this.x;
   }

   public int getY() {
      return this.y;
   }

   public void setPosition(int x, int y) {
      this.x = x;
      this.y = y;
   }

   // Métodos para el estado "hovered"
   public void setHovered(boolean hovered) {
      this.hovered = hovered;
   }

   public boolean isHovered() {
      return hovered;
   }
   public void setSelected(boolean selected) {
      this.selected = selected;
  }

  public boolean isSelected() {
      return selected;
  }

  public boolean contains(int x, int y) {
      int width = 120;
      int height = 90 + (attributes.size() + methods.size()) * 15; // Ajustar tamaño según contenido
      return x >= this.x && x <= this.x + width && y >= this.y && y <= this.y + height;
   }

public void addAttribute(String attribute) {
   attributes.add(attribute);
}

public void addMethod(String method) {
   methods.add(method);
}

public void draw(Graphics g, int x, int y, boolean isAssociationTarget) {
   this.x = x;
   this.y = y;
   Graphics2D g2d = (Graphics2D) g;
   int width = 120;
   int height = 75 + (attributes.size() + methods.size()) * 15; // Ajustar altura dinámicamente

   if (selected) {
       g2d.setColor(new Color(0, 255, 255)); // Cyan cuando está seleccionada
   } else if (isAssociationTarget) {
       g2d.setColor(new Color(144, 238, 144)); // 🔹 Verde cuando es un objetivo de asociación
   } else {
       g2d.setColor(Color.WHITE);
   }
   g2d.fillRect(x, y, width, height);

   g2d.setColor(Color.BLACK);
   g2d.drawRect(x, y, width, height);
   g2d.drawLine(x, y + 30, x + width, y + 30);
   g2d.drawLine(x, y + 30 + attributes.size() * 15, x + width, y + 30 + attributes.size() * 15);

   g2d.drawString(this.className, x + 5, y + 20);

   // 🔹 Dibujar los atributos
   int textY = y + 43;
   if (attributes.isEmpty()) {
       g2d.drawString("Atributos...", x + 5, textY); // Si no hay atributos, mostrar el texto predeterminado
       textY += 15;
   } else {
       for (String attr : attributes) {
           g2d.drawString(attr, x + 5, textY);
           textY += 15;
       }
   }

   g2d.drawLine(x, y + 30 + Math.max(attributes.size() * 15, 15), x + width, y + 30 + Math.max(attributes.size() * 15, 15)); // Línea divisoria


   // 🔹 Dibujar los métodos
   textY += 5; // Espacio entre atributos y métodos
   if (methods.isEmpty()) {
       g2d.drawString("Operaciones...", x + 5, textY); // Si no hay métodos, mostrar el texto predeterminado
       textY += 15;
   } else {
       for (String method : methods) {
           g2d.drawString(method, x + 5, textY);
           textY += 15;
       }
   }
}

   // Se puede omitir la redefinición de paintComponent si se dibuja desde Diagram
   protected void paintComponent(Graphics g) {
      super.paintComponent(g);
   }
}
