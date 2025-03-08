import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Association {
    private Class classA;
    private Class classB;

    public Association(Class classA, Class classB) {
        this.classA = classA;
        this.classB = classB;
    }
    public boolean isClicked(int x, int y) {
        int x1 = classA.getX() + 60;
        int y1 = classA.getY() + 45;
        int x2 = classB.getX() + 60;
        int y2 = classB.getY() + 45;

        int threshold = 5; // Margen de error para hacer clic en la línea
        double distance = Math.abs((y2 - y1) * x - (x2 - x1) * y + x2 * y1 - y2 * x1)
                / Math.sqrt(Math.pow(y2 - y1, 2) + Math.pow(x2 - x1, 2));

        return distance <= threshold;
    }
    
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLACK);

        if (classA == classB) {
            int x = classA.getX();
            int y = classA.getY();
            int w = 120;   // ancho de la clase
            int h = 90;    // alto de la clase

            // Desplazamiento vertical para que el arco no toque
            // exactamente la parte superior ni la inferior.
            int offset = 10;

            // Altura de la caja del arco = alto de la clase - 2*offset
            int arcH = h - (2 * offset);  
            int arcW = arcH;  // 70 px
            int arcX = (x + w) - (arcW / 2);
            int arcY = y + offset;

            g2d.drawArc(arcX, arcY, arcW, arcH, 270, 180);

        } else {
            // Asociación normal: dibujar una línea entre el centro de ambas clases
            int x1 = classA.getX() + 60;
            int y1 = classA.getY() + 45;
            int x2 = classB.getX() + 60;
            int y2 = classB.getY() + 45;
            g2d.drawLine(x1, y1, x2, y2);
        }
    }

    public Class getClassA() {
        return classA;
    }

    public Class getClassB() {
        return classB;
    }
}
