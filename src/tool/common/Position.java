package tool.common;

public class Position {
   private final int x;
   private final int y;

   public Position(int var1, int var2) {
      this.x = var1;
      this.y = var2;
   }

   public int getRow() {
      return this.x;
   }

   public int getCol() {
      return this.y;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof Position)) {
         return false;
      } else {
         Position var2 = (Position)var1;
         return this.x == var2.x && this.y == var2.y;
      }
   }

   public int hashCode() {
      byte var1 = 3;
      int var2 = 71 * var1 + this.x;
      var2 = 71 * var2 + this.y;
      return var2;
   }

   public String toString() {
      return "[" + this.x + "@" + this.y + "]";
   }
}
