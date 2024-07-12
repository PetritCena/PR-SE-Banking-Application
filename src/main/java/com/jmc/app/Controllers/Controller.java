package com.jmc.app.Controllers;

/**
 * Dieses Interface enthält den Controller, der von allen Controllerklassen der Appliaktion implementiert wird.
 */
public interface Controller {
     /**
      * Diese Methode ist dazu da, um die Controller zu initialisieren.
      * @param o ist, abhängig von der Controllerklasse, entweder eine User-Instanz, eine Account-Instanz oder Card-Instanz.
      * @param o2 ist, abhängig von der Controllerklasse, entweder eine Account-Instanz oder eine User-Instanz.
      */
     void initialize(Object o, Object o2);
}
