package tool.common;

public interface Observable {
   void addObserver(Observable.Observer var1);

   void removeObserver(Observable.Observer var1);

   void notifyObservers();

   public interface Observer {
      void update(Observable var1);
   }
}
