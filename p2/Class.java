import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

public class Class extends JPanel {
   private static int instanceCount = 0;
   private String className;
   private int x;
   private int y;
   // Bandera para determinar si el ratón está sobre la clase
   private boolean hovered = false;

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

   // Comprueba si un punto (x, y) está dentro del rectángulo de la clase
   public boolean contains(int x, int y) {
      int width = 120;
      int height = 90;
      return x >= this.x && x <= this.x + width && y >= this.y && y <= this.y + height;
   }

   // Dibuja la clase en la posición indicada
   public void draw(Graphics g, int x, int y) {
      this.x = x;
      this.y = y;
      Graphics2D g2d = (Graphics2D) g;
      int width = 120;
      int height = 90;

      // Si el ratón está sobre la clase se pinta un fondo verde clarito; de lo contrario, blanco
      if (hovered) {
         g2d.setColor(new Color(144, 238, 144)); // LightGreen
      } else {
         g2d.setColor(Color.WHITE);
      }
      g2d.fillRect(x, y, width, height);

      // Dibujar el contorno y líneas separadoras
      g2d.setColor(Color.BLACK);
      g2d.drawRect(x, y, width, height);
      g2d.drawLine(x, y + 30, x + width, y + 30);
      g2d.drawLine(x, y + 60, x + width, y + 60);
      
      // Dibujar textos
      g2d.drawString(this.className, x + 5, y + 20);
      g2d.drawString("Atributos...", x + 5, y + 50);
      g2d.drawString("Operaciones...", x + 5, y + 80);
   }

   // Se puede omitir la redefinición de paintComponent si se dibuja desde Diagram
   protected void paintComponent(Graphics g) {
      super.paintComponent(g);
   }
}
