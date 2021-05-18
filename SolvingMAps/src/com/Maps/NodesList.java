/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.Maps;
import java.util.*;


/**
 *
 * @author ariel
 */
public class NodesList extends ArrayList{
  private ArrayList list = new ArrayList();
  public void add(Nodes m) {
    list.add(m);
  }
    @Override
  public Nodes get(int index) {
    return (Nodes)list.get(index);
  }
    @Override
  public int size() {
      return list.size(); }
  public void set(Nodes a, int pos)
    {
      Object b=list.set(pos, a);
  }
    @Override
public Object remove(int index)
    {
    return list.remove(index);
    
}

}

